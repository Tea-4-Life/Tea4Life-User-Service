package tea4life.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Admin 2/21/2026
 *
 **/
public record UserRoleAssign(
        @NotNull(message = "Chức vụ không được để trống")
        Long roleId,

        @NotBlank(message = "KeycloakId không được để trống")
        String keycloakId
) {
}
