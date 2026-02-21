package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.UserRoleAssign;
import tea4life.user_service.dto.response.UserResponse;
import tea4life.user_service.dto.response.UserSummaryResponse;
import tea4life.user_service.model.Role;
import tea4life.user_service.model.User;
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
public class AdminUserServiceImpl implements AdminUserService {

    UserRepository userRepository;
    RoleRepository roleRepository;

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
                .findById(request.roleId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ"));

        user.setRole(role);
        userRepository.save(user);
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
