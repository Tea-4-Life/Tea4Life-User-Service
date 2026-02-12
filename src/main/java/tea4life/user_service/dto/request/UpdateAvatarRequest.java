package tea4life.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Admin 2/12/2026
 *
 **/
public record UpdateAvatarRequest(
        @NotBlank(message = "Ảnh bìa không được để trống")
        String avatarKey
) {
}
