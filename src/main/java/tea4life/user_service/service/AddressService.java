package tea4life.user_service.service;

import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.CreateAddressRequest;
import tea4life.user_service.dto.response.AddressResponse;

import java.util.List;

/**
 * Admin 2/24/2026
 *
 **/
public interface AddressService {
    AddressResponse createAddress(CreateAddressRequest request);

    @Transactional(readOnly = true)
    List<AddressResponse> findMyAddresses();

    @Transactional(readOnly = true)
    AddressResponse findMyAddressById(Long id);

    AddressResponse updateMyAddress(Long id, CreateAddressRequest request);

    void deleteMyAddress(Long id);
}
