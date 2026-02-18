
package tea4life.user_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.base.PageResponse;
import tea4life.user_service.dto.request.UpsertRoleRequest;
import tea4life.user_service.dto.response.RoleResponse;
import tea4life.user_service.service.RoleService;

/**
 * Admin 2/16/2026
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping("/roles")
    public ApiResponse<@NonNull Void> createPermission(
            @RequestBody UpsertRoleRequest upsertRoleRequest
    ) {
        roleService.createRole(upsertRoleRequest);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/roles")
    public ApiResponse<PageResponse<RoleResponse>> findAllPermissions(
            @PageableDefault(value = 10) Pageable pageable
    ) {
        PageResponse<RoleResponse> page = new PageResponse<>(
                roleService.findAllRoles(pageable)
        );

        return new ApiResponse<>(page);
    }

    @PostMapping("/roles/{id}")
    public ApiResponse<@NonNull Void> updatePermission(
            @RequestBody UpsertRoleRequest upsertRoleRequest,
            @PathVariable("id") Long id
    ) {
        roleService.updateRole(upsertRoleRequest, id);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/roles/{id}")
    public ApiResponse<@NonNull Void> deletePermission(
            @PathVariable("id") Long id
    ) {
        roleService.deleteRoleById(id);
        return ApiResponse.<Void>builder().build();
    }


}
