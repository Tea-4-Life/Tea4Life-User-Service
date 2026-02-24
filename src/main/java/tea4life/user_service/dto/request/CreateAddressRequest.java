package tea4life.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import tea4life.user_service.model.constant.AddressType;

/**
 * Admin 2/16/2026
 *
 **/
public record CreateAddressRequest(
        @NotBlank(message = "Tên người nhận không được để trống")
        String receiverName,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại không đúng định dạng (10 chữ số)")
        String phone,

        @NotBlank(message = "Tỉnh/Thành phố không được để trống")
        String province,

        @NotBlank(message = "Phường/Xã không được để trống")
        String ward,

        @NotBlank(message = "Địa chỉ chi tiết không được để trống")
        String detail,

        @NotNull(message = "Vĩ độ (Latitude) là bắt buộc từ bản đồ")
        Double latitude,

        @NotNull(message = "Kinh độ (Longitude) là bắt buộc từ bản đồ")
        Double longitude,

        @NotNull(message = "Vui lòng chọn loại địa chỉ (Nhà riêng/Văn phòng)")
        AddressType addressType,

        boolean isDefault
) {
}