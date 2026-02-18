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
        return new Role(
                null,
                upsertRoleRequest.name(),
                upsertRoleRequest.description(),
                null
        );
    }

}
