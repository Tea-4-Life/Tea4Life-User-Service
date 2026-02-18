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
        return new PermissionResponse(
                permission.getId().toString(),
                permission.getName(),
                permission.getPermissionGroup(),
                permission.getDescription()
        );
    }

    public static Permission mapToPermission(UpsertPermissionRequest upsertPermissionRequest) {
        String permissionGroup = upsertPermissionRequest.permissionGroup();
        String description =
                upsertPermissionRequest.description() != null && !upsertPermissionRequest.description().isBlank()
                        ? upsertPermissionRequest.description()
                        : null;

        return new Permission(
                null,
                upsertPermissionRequest.name(),
                permissionGroup,
                description
        );
    }

}
