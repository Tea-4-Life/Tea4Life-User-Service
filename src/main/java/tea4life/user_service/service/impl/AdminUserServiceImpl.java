package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.advice.exception.BusinessException;
import tea4life.user_service.client.OrderInternalClient;
import tea4life.user_service.dto.request.UserRoleAssign;
import tea4life.user_service.dto.request.driver.UpsertDriverRequest;
import tea4life.user_service.dto.response.UserResponse;
import tea4life.user_service.dto.response.UserSummaryResponse;
import tea4life.user_service.model.Role;
import tea4life.user_service.model.User;
import tea4life.user_service.model.constant.RoleName;
import tea4life.user_service.repository.RoleRepository;
import tea4life.user_service.repository.UserRepository;
import tea4life.user_service.service.AdminUserService;

/**
 * Admin 2/21/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    OrderInternalClient orderInternalClient;

    @Override
    @Transactional(readOnly = true)
    public Page<@NonNull UserSummaryResponse> findAllUsers(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(this::mapToUserSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByKeycloakId(String keycloakId) {
        User user = userRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        return mapToUserResponse(user);
    }

    @Override
    public void assignRole(UserRoleAssign request) {
        User user = userRepository
                .findByKeycloakId(request.keycloakId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        Role role = roleRepository
                .findById(Long.parseLong(request.roleId()))
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ"));

        user.setRole(role);
        if (RoleName.DRIVER.equalsIgnoreCase(role.getName())) {
            syncDriverToOrderService(user);
        }
        userRepository.save(user);
    }

    private void syncDriverToOrderService(User user) {
        String fullName = requireText(user.getFullName(), "Người dùng cần cập nhật họ tên trước khi gán role DRIVER");
        String phone = requireText(user.getPhone(), "Người dùng cần cập nhật số điện thoại trước khi gán role DRIVER");

        try {
            var response = orderInternalClient.syncDriver(new UpsertDriverRequest(
                    user.getKeycloakId(),
                    fullName,
                    phone
            ));

            if (response != null && response.getErrorCode() != null) {
                throw new BusinessException(response.getErrorMessage() == null
                        ? "Không thể đồng bộ tài xế sang Order Service"
                        : response.getErrorMessage());
            }
        } catch (FeignException ex) {
            log.error(
                    "Không đồng bộ được driver sang Order Service cho keycloakId={}. Order status={}, body={}",
                    user.getKeycloakId(),
                    ex.status(),
                    ex.contentUTF8(),
                    ex
            );
            throw new BusinessException("Đã gán role DRIVER thất bại vì không đồng bộ được tài xế sang Order Service");
        }
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(message);
        }
        return value.trim();
    }

    private UserSummaryResponse mapToUserSummaryResponse(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId() == null ? null : user.getId().toString())
                .keycloakId(user.getKeycloakId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        Role role = user.getRole();
        String roleName = role != null ? role.getName() : null;

        return UserResponse.builder()
                .id(user.getId() == null ? null : user.getId().toString())
                .keycloakId(user.getKeycloakId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .dob(user.getDob())
                .gender(user.getGender())
                .roleName(roleName)
                .build();
    }
}
