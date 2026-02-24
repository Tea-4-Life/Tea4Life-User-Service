package tea4life.user_service.dto.response;

import lombok.Builder;
import tea4life.user_service.model.constant.AddressType;

/**
 * Admin 2/24/2026
 *
 **/
@Builder
public record AddressResponse(
        String id,
        String receiverName,
        String phone,
        String province,
        String ward,
        String detail,
        Double latitude,
        Double longitude,
        AddressType addressType,
        boolean isDefault
) {
}
