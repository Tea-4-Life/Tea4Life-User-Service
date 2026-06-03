package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.BadRequestException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.advice.exception.BusinessException;
import tea4life.user_service.client.StorageClient;
import tea4life.user_service.context.UserContext;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.*;
import tea4life.user_service.dto.response.UserPermissionsResponse;
import tea4life.user_service.dto.response.UserProfileResponse;
import tea4life.user_service.model.Permission;
import tea4life.user_service.model.Role;
import tea4life.user_service.model.User;
import tea4life.user_service.model.constant.RoleName;
import tea4life.user_service.repository.RoleRepository;
import tea4life.user_service.repository.UserRepository;
import tea4life.user_service.service.UserService;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Admin 2/8/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_CURRENT_REALM = "Tea4Life";
    private static final String DEFAULT_REALM_MASTER = "master";
    private static final String DEFAULT_VERIFY_CLIENT_ID = "admin-cli";

    UserRepository userRepository;
    StorageClient storageClient;
    RoleRepository roleRepository;

    KafkaTemplate<@NonNull String, @NonNull String> kafkaTemplate;

    @Value("${keycloak.server-url}")
    @NonFinal
    String serverUrl;

    @Value("${keycloak.admin.user-name}")
    @NonFinal
    String adminUserName;

    @Value("${keycloak.admin.password}")
    @NonFinal
    String adminPassword;

    @Value("${keycloak.realm-master}")
    @NonFinal
    String realmMaster;

    @Value("${keycloak.current-realm}")
    @NonFinal
    String currentRealm;

    @Value("${keycloak.client-id}")
    @NonFinal
    String clientId;

    @Value("${keycloak.verify-client-id}")
    @NonFinal
    String verifyClientId;

    @Value("${spring.kafka.topic.storage-delete-file}")
    @NonFinal
    String storageDeleteFileTopic;

    @Override
    public void processOnboarding(OnboardingRequest onboardingRequest) {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        applyOnboarding(user, onboardingRequest);
    }

    @Override
    public void processOnboarding(Long userId, OnboardingRequest onboardingRequest) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        applyOnboarding(user, onboardingRequest);
    }

    private void applyOnboarding(User user, OnboardingRequest onboardingRequest) {
        try {
            if (onboardingRequest.avatarKey() != null && !onboardingRequest.avatarKey().isBlank()) {
                String destinationPath = "users/avatars/" + user.getId();
                ApiResponse<String> storageResponse = storageClient.confirmFile(
                        new FileMoveRequest(
                                onboardingRequest.avatarKey(),
                                destinationPath
                        )
                );

                if (storageResponse.getErrorCode() != null)
                    throw new RuntimeException("Lỗi di chuyển file: " + storageResponse.getErrorMessage());
                user.setAvatarUrl(storageResponse.getData());
            }


            user.setFullName(onboardingRequest.fullName());
            user.setPhone(onboardingRequest.phone());
            user.setDob(onboardingRequest.dob());
            user.setGender(onboardingRequest.gender());
            user.setOnBoarded(true);


            userRepository.save(user);
            log.info("Onboarding thành công cho user: {}", user.getId());

        } catch (Exception e) {
            log.error("Onboarding thất bại cho user {}: {}", user.getId(), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile() {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        return UserProfileResponse
                .builder()
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .dob(user.getDob())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .id(user.getId().toString())
                .build();
    }

    @Override
    public void updateUserProfile(UpdateProfileRequest request) {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setDob(request.dob());
        user.setGender(request.gender());
    }

    @Override
    public void updateUserAvatar(UpdateAvatarRequest request) {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        if (request.avatarKey() != null && !request.avatarKey().isBlank()) {
            String oldAvatarUrl = user.getAvatarUrl();
            String destinationPath = "users/avatars/" + user.getId();

            ApiResponse<String> storageResponse = storageClient.confirmFile(
                    new FileMoveRequest(
                            request.avatarKey(),
                            destinationPath
                    )
            );

            if (storageResponse.getErrorCode() != null)
                throw new RuntimeException("Lỗi di chuyển file: " + storageResponse.getErrorMessage());
            user.setAvatarUrl(storageResponse.getData());

            kafkaTemplate.send(storageDeleteFileTopic, oldAvatarUrl);
        }
    }

    @Override
    public void updateUserPassword(UpdatePasswordRequest request) {
        String keycloakId = UserContext.get().getKeycloakId();
        String realm = resolveCurrentRealm();

        try (Keycloak adminKeycloak = createAdminKeycloak()) {
            verifyOldPassword(adminKeycloak, keycloakId, request.oldPassword(), realm);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.newPassword());
            credential.setTemporary(false);

            adminKeycloak.realm(realm)
                    .users()
                    .get(keycloakId)
                    .resetPassword(credential);

            log.info("Successfully updated password for user: {}", keycloakId);
        } catch (BusinessException e) {
            throw e;
        } catch (BadRequestException e) {
            log.error("Password policy violation for user {}: {}", keycloakId, e.getMessage());
            throw new BusinessException("Mật khẩu của bạn không đạt chuẩn!");
        } catch (Exception e) {
            log.error("Unexpected error during password update for user {}: {}", keycloakId, e.getMessage());
            throw new RuntimeException("Có lỗi đã xảy ra khi cố cập nhật mật khẩu.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserPermissionsResponse getUserPermissions(String keycloakId) {
        User user = userRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        Set<String> permissions = (user.getRole() != null)
                ? user.getRole().getPermissions().stream().map(Permission::getName).collect(Collectors.toSet())
                : Collections.emptySet();

        String role = user.getRole() != null ? user.getRole().getName() : "";

        return new UserPermissionsResponse(user.getEmail(), role, permissions);
    }

    @Override
    public void assignRoleByName(String keycloakId, String roleName) {
        User user = userRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        Role role = roleRepository
                .findByName(roleName.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy role: " + roleName));

        user.setRole(role);
        userRepository.save(user);
    }


    @Override
    public void downgradeDriverRoleToMember(String keycloakId) {
        User user = userRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        Role currentRole = user.getRole();
        if (currentRole == null || !RoleName.DRIVER.equalsIgnoreCase(currentRole.getName())) {
            return;
        }

        Role memberRole = roleRepository
                .findByName(RoleName.MEMBER)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy role: " + RoleName.MEMBER));

        user.setRole(memberRole);
        userRepository.save(user);
    }


    private void verifyOldPassword(Keycloak adminKeycloak, String keycloakId, String oldPassword, String realm) {
        String username = adminKeycloak.realm(realm)
                .users()
                .get(keycloakId)
                .toRepresentation()
                .getUsername();

        if (username == null || username.isBlank()) {
            log.warn("Cannot verify old password because Keycloak username is blank for user: {}", keycloakId);
            throw new BusinessException("Mật khẩu cũ không chính xác!");
        }

        try (Keycloak tempKeycloak = KeycloakBuilder
                .builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(resolveVerifyClientId())
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(oldPassword)
                .build()) {

            tempKeycloak.tokenManager().getAccessToken();
        } catch (Exception e) {
            log.warn(
                    "Failed old password verification for keycloakId={}, username={}, client={}, serverUrl={}. Reason: {}",
                    keycloakId,
                    username,
                    resolveVerifyClientId(),
                    serverUrl,
                    e.getMessage()
            );
            throw new BusinessException("Mật khẩu cũ không chính xác!");
        }
    }

    private Keycloak createAdminKeycloak() {
        return KeycloakBuilder
                .builder()
                .serverUrl(requireConfig(serverUrl, "keycloak.server-url"))
                .realm(resolveRealmMaster())
                .clientId(resolveClientId())
                .grantType(OAuth2Constants.PASSWORD)
                .username(requireConfig(adminUserName, "keycloak.admin.user-name"))
                .password(requireConfig(adminPassword, "keycloak.admin.password"))
                .build();
    }

    private String resolveCurrentRealm() {
        if (currentRealm != null && !currentRealm.isBlank()) {
            return currentRealm.trim();
        }

        log.warn("keycloak.current-realm is blank. Falling back to {}", DEFAULT_CURRENT_REALM);
        return DEFAULT_CURRENT_REALM;
    }

    private String resolveRealmMaster() {
        if (realmMaster != null && !realmMaster.isBlank()) {
            return realmMaster.trim();
        }

        log.warn("keycloak.realm-master is blank. Falling back to {}", DEFAULT_REALM_MASTER);
        return DEFAULT_REALM_MASTER;
    }

    private String resolveClientId() {
        if (clientId != null && !clientId.isBlank()) {
            return clientId.trim();
        }

        return DEFAULT_VERIFY_CLIENT_ID;
    }

    private String resolveVerifyClientId() {
        if (verifyClientId != null && !verifyClientId.isBlank()) {
            return verifyClientId.trim();
        }

        if (clientId != null && !clientId.isBlank()) {
            return clientId.trim();
        }

        return DEFAULT_VERIFY_CLIENT_ID;
    }

    private String requireConfig(String value, String propertyName) {
        if (value != null && !value.isBlank()) {
            return value.trim();
        }

        throw new IllegalStateException("Missing Keycloak config: " + propertyName);
    }

}
