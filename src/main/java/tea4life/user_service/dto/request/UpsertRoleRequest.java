package tea4life.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

/**
 * Admin 2/16/2026
 *
 **/
public record UpsertRoleRequest(

        @NotBlank(message = "Tên chức vụ không được để trống")
        String name,

        String description,

        Set<Long> permissionIdList
) {
}
