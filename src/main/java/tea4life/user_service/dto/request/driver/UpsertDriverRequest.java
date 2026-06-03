package tea4life.user_service.dto.request.driver;

import jakarta.validation.constraints.NotBlank;

public record UpsertDriverRequest(
        @NotBlank(message = "keycloakId không được để trống")
        String keycloakId,

        @NotBlank(message = "fullName không được để trống")
        String fullName,

        @NotBlank(message = "phone không được để trống")
        String phone
) {
}
