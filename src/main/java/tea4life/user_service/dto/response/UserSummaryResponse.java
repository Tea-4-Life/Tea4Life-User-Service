package tea4life.user_service.dto.response;

import lombok.Builder;

/**
 * Admin 2/21/2026
 *
 **/
@Builder
public record UserSummaryResponse(
        String id,
        String keycloakId,
        String email,
        String fullName,
        String avatarUrl
) {
}
