package tea4life.user_service.service;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.UserRoleAssign;
import tea4life.user_service.dto.response.UserResponse;
import tea4life.user_service.dto.response.UserSummaryResponse;

/**
 * Admin 2/21/2026
 *
 **/
public interface AdminUserService {

    @Transactional(readOnly = true)
    Page<@NonNull UserSummaryResponse> findAllUsers(Pageable pageable);

    @Transactional(readOnly = true)
    UserResponse findByKeycloakId(String keycloakId);

    void assignRole(UserRoleAssign request);
}
