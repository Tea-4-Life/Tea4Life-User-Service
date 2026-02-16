package tea4life.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Admin 2/16/2026
 *
 **/
public record UpsertPermissionRequest(

        @NotBlank(message = "Tên quyền không được để trống")
        String name,

        @NotBlank(message = "Nhóm quyền không được để trống")
        String group,

        String description
) {
}
