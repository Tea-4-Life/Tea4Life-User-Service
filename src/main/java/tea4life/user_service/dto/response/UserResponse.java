package tea4life.user_service.dto.response;

import lombok.Builder;
import tea4life.user_service.model.Role;
import tea4life.user_service.model.constant.Gender;

import java.time.LocalDate;

/**
 * Admin 2/21/2026
 *
 **/
@Builder
public record UserResponse(
        String id,
        String keycloakId,
        String email,
        String fullName,
        String avatarUrl,
        String phone,
        LocalDate dob,
        Gender gender,
        String roleName
) {
}
