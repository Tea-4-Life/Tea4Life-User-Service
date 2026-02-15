package tea4life.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Admin 2/15/2026
 *
 **/
public record UpdatePasswordRequest(
        @NotBlank(message = "Mật khẩu cũ không được để trống")
        String oldPassword,

        @NotBlank(message = "Mật khẩu mới không được để trống")
        @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
                message = "Mật khẩu phải bao gồm ít nhất 1 chữ hoa, 1 chữ số và 1 ký tự đặc biệt"
        )
        String newPassword
) {
}
