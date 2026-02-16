package tea4life.user_service.dto.response;

import lombok.Builder;

/**
 * Admin 2/16/2026
 *
 **/
@Builder
public record PermissionResponse(
        String id,
        String name,
        String permissionGroup,
        String description) {
}
