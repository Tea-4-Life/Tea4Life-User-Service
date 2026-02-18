package tea4life.user_service.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Set;

/**
 * Admin 2/18/2026
 *
 **/
@Builder
public record RoleResponse(
        String id,
        String name,
        String description,
        Set<PermissionResponse> permissions
) {
}
