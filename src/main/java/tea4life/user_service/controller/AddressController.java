package tea4life.user_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.*;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.CreateAddressRequest;
import tea4life.user_service.dto.response.AddressResponse;
import tea4life.user_service.service.AddressService;

import java.util.List;

/**
 * Admin 2/24/2026
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users/me/addresses")
public class AddressController {

    AddressService addressService;

    @PostMapping()
    public ApiResponse<AddressResponse> createAddress(
            @RequestBody @Valid CreateAddressRequest request
    ) {
        return new ApiResponse<>(addressService.createAddress(request));
    }

    @GetMapping()
    public ApiResponse<List<AddressResponse>> findMyAddresses() {
        return new ApiResponse<>(addressService.findMyAddresses());
    }

    @GetMapping("/{id}")
    public ApiResponse<AddressResponse> findMyAddressById(@PathVariable("id") Long id) {
        return new ApiResponse<>(addressService.findMyAddressById(id));
    }

    @PostMapping("/{id}")
    public ApiResponse<AddressResponse> updateMyAddress(
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateAddressRequest request
    ) {
        return new ApiResponse<>(addressService.updateMyAddress(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<@NonNull Void> deleteMyAddress(@PathVariable("id") Long id) {
        addressService.deleteMyAddress(id);
        return ApiResponse.<Void>builder().build();
    }
}
