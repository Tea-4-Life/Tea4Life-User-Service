package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.CreateAddressRequest;
import tea4life.user_service.dto.response.AddressResponse;
import tea4life.user_service.model.Address;
import tea4life.user_service.model.User;
import tea4life.user_service.repository.AddressRepository;
import tea4life.user_service.repository.UserRepository;
import tea4life.user_service.service.AdminAddressService;

import java.util.List;

/**
 * Admin 2/24/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AddressAdminServiceImpl implements AdminAddressService {

    int MAX_ADDRESS_PER_USER = 5;

    UserRepository userRepository;
    AddressRepository addressRepository;

    @Override
    public AddressResponse createAddress(String keycloakId, CreateAddressRequest request) {
        User user = findUserByKeycloakId(keycloakId);
        long totalAddresses = addressRepository.countByUserId(user.getId());
        if (totalAddresses >= MAX_ADDRESS_PER_USER) {
            throw new DataIntegrityViolationException("Mỗi người dùng chỉ có tối đa 5 địa chỉ");
        }

        Address address = new Address();
        address.setUser(user);
        applyRequestToAddress(address, request);

        if (request.isDefault() || totalAddresses == 0) {
            setAllAddressesNonDefault(user.getId());
            address.setDefault(true);
        }

        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> findAddressesByKeycloakId(String keycloakId) {
        User user = findUserByKeycloakId(keycloakId);
        return addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse findAddressByKeycloakIdAndAddressId(String keycloakId, Long addressId) {
        User user = findUserByKeycloakId(keycloakId);
        Address address = findAddressByIdAndUserId(addressId, user.getId());
        return toResponse(address);
    }

    @Override
    public AddressResponse updateAddressByKeycloakId(String keycloakId, Long addressId, CreateAddressRequest request) {
        User user = findUserByKeycloakId(keycloakId);
        Address address = findAddressByIdAndUserId(addressId, user.getId());

        applyRequestToAddress(address, request);
        if (request.isDefault()) {
            setAllAddressesNonDefault(user.getId());
            address.setDefault(true);
        } else if (!hasAnotherDefaultAddress(user.getId(), address.getId())) {
            address.setDefault(true);
        }

        return toResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddressByKeycloakId(String keycloakId, Long addressId) {
        User user = findUserByKeycloakId(keycloakId);
        Address address = findAddressByIdAndUserId(addressId, user.getId());
        boolean wasDefault = address.isDefault();

        addressRepository.delete(address);

        if (wasDefault) {
            List<Address> remaining = addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(user.getId());
            if (!remaining.isEmpty()) {
                Address first = remaining.getFirst();
                first.setDefault(true);
                addressRepository.save(first);
            }
        }
    }

    private User findUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));
    }

    private Address findAddressByIdAndUserId(Long addressId, Long userId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy địa chỉ"));
    }

    private void setAllAddressesNonDefault(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(userId);
        for (Address item : addresses) {
            item.setDefault(false);
        }
        addressRepository.saveAll(addresses);
    }

    private boolean hasAnotherDefaultAddress(Long userId, Long currentAddressId) {
        List<Address> addresses = addressRepository.findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(userId);
        return addresses.stream().anyMatch(item -> !item.getId().equals(currentAddressId) && item.isDefault());
    }

    private void applyRequestToAddress(Address address, CreateAddressRequest request) {
        address.setReceiverName(request.receiverName());
        address.setPhone(request.phone());
        address.setProvince(request.province());
        address.setWard(request.ward());
        address.setDetail(request.detail());
        address.setLatitude(request.latitude());
        address.setLongitude(request.longitude());
        address.setAddressType(request.addressType());
        address.setDefault(request.isDefault());
    }

    private AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId() == null ? null : address.getId().toString())
                .receiverName(address.getReceiverName())
                .phone(address.getPhone())
                .province(address.getProvince())
                .ward(address.getWard())
                .detail(address.getDetail())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .addressType(address.getAddressType())
                .isDefault(address.isDefault())
                .build();
    }
}
