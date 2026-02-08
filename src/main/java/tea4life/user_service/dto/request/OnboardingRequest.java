package tea4life.user_service.dto.request;

import jakarta.validation.constraints.*;
import tea4life.user_service.model.constant.Gender;

import java.time.LocalDate;

/**
 * Admin 2/4/2026
 *
 **/
public record OnboardingRequest(
        @NotBlank(message = "Họ tên không được để trống")
        @Size(min = 2, max = 50, message = "Họ tên phải từ 2-50 ký tự")
        String fullName,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^[0-9]{10,15}$", message = "Số điện thoại phải từ 10-11 số")
        String phone,

        @NotNull(message = "Ngày sinh không được để trống")
        @Past(message = "Ngày sinh phải là một ngày trong quá khứ")
        LocalDate dob,

        @NotNull(message = "Giới tính không được để trống")
        Gender gender,

        @NotBlank(message = "Thiếu mã xác nhận ảnh đại diện")
        String avatarKey
) {
}
