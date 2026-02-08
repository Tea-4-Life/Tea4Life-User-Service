package tea4life.user_service.dto.response;

import tea4life.user_service.dto.response.constant.UserStatus;

/**
 * Admin 2/6/2026
 *
 **/
public record UserStatusResponse(
        UserStatus userStatus,
        boolean existed,
        boolean onboarded,
        String email,
        String role
) {
}
