package tea4life.user_service.utils;

import tea4life.user_service.dto.request.UpsertPermissionRequest;
import tea4life.user_service.dto.response.PermissionResponse;
import tea4life.user_service.model.Permission;

/**
 * Admin 2/18/2026
 *
 **/
public class PermissionMapper {

    public static PermissionResponse mapToPermissionResponse(Permission permission) {
        return PermissionResponse
                .builder()
                .id(permission.getId().toString())
                .name(permission.getName())
                .permissionGroup(permission.getPermissionGroup())
                .description(permission.getDescription())
                .build();
    }

    public static Permission mapToPermission(UpsertPermissionRequest upsertPermissionRequest) {
        String name = upsertPermissionRequest.name().toUpperCase();
        String permissionGroup = upsertPermissionRequest.permissionGroup();
        String description =
                upsertPermissionRequest.description() != null && !upsertPermissionRequest.description().isBlank()
                        ? upsertPermissionRequest.description()
                        : null;

        return Permission
                .builder()
                .name(name)
                .permissionGroup(permissionGroup)
                .description(description)
                .build();
    }

}
