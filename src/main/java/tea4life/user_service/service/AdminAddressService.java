package tea4life.user_service.service;

import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.CreateAddressRequest;
import tea4life.user_service.dto.response.AddressResponse;

import java.util.List;

/**
 * Admin 2/24/2026
 *
 **/
public interface AdminAddressService {
    AddressResponse createAddress(String keycloakId, CreateAddressRequest request);

    @Transactional(readOnly = true)
    List<AddressResponse> findAddressesByKeycloakId(String keycloakId);

    @Transactional(readOnly = true)
    AddressResponse findAddressByKeycloakIdAndAddressId(String keycloakId, Long addressId);

    AddressResponse updateAddressByKeycloakId(String keycloakId, Long addressId, CreateAddressRequest request);

    void deleteAddressByKeycloakId(String keycloakId, Long addressId);
}
