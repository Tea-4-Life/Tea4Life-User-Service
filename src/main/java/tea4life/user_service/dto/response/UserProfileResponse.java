package tea4life.user_service.dto.response;

import lombok.Builder;
import tea4life.user_service.model.constant.Gender;

import java.time.LocalDate;

/**
 * Admin 2/12/2026
 *
 **/
@Builder
public record UserProfileResponse(
        String fullName,
        String phone,
        LocalDate dob,
        Gender gender,
        String avatarUrl,
        String id
) {
}
