package tea4life.user_service.utils;

import tea4life.user_service.dto.request.UpsertRoleRequest;
import tea4life.user_service.dto.response.RoleResponse;
import tea4life.user_service.model.Role;

import java.util.stream.Collectors;

/**
 * Admin 2/18/2026
 *
 **/
public class RoleMapper {

    public static RoleResponse mapToRoleResponse(Role role) {
        return RoleResponse
                .builder()
                .id(role.getId().toString())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getPermissions()
                        .stream()
                        .map(PermissionMapper::mapToPermissionResponse)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public static Role mapToRole(UpsertRoleRequest upsertRoleRequest) {
        String name = upsertRoleRequest.name().toUpperCase();
        String description =
                upsertRoleRequest.description() != null && !upsertRoleRequest.description().isBlank()
                        ? upsertRoleRequest.description()
                        : null;

        return Role
                .builder()
                .name(name)
                .description(description)
                .build();
    }

    public static void updateRoleFromRequest(Role role, UpsertRoleRequest upsertRoleRequest) {
        String name = upsertRoleRequest.name().toUpperCase();
        String description = upsertRoleRequest.description() != null && !upsertRoleRequest.description().isBlank()
                ? upsertRoleRequest.description()
                : null;

        role.setName(name);
        role.setDescription(description);
    }

}
