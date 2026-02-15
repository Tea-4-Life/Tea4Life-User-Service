package tea4life.user_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.OnboardingRequest;
import tea4life.user_service.dto.request.UpdateAvatarRequest;
import tea4life.user_service.dto.request.UpdatePasswordRequest;
import tea4life.user_service.dto.request.UpdateProfileRequest;
import tea4life.user_service.dto.response.UserProfileResponse;
import tea4life.user_service.service.UserService;

/**
 * Admin 2/8/2026
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceController {

    UserService userService;

    @GetMapping("/users/me")
    public ApiResponse<UserProfileResponse> getUserProfile() {
        return new ApiResponse<>(userService.getUserProfile());
    }

    @PostMapping("/users/me/onboarding")
    public ApiResponse<@NonNull Void> processOnboarding(
            @RequestBody @Valid OnboardingRequest request
    ) {
        userService.processOnboarding(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/users/me/profile")
    public ApiResponse<@NonNull Void> updateUserProfile(
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        userService.updateUserProfile(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/users/me/avatar")
    public ApiResponse<@NonNull Void> updateUserAvatar(
            @RequestBody @Valid UpdateAvatarRequest request
    ) {
        userService.updateUserAvatar(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/users/me/password")
    public ApiResponse<@NonNull Void> updateUserPassword(
            @RequestBody @Valid UpdatePasswordRequest request
    ) {
        userService.updateUserPassword(request);
        return ApiResponse.<Void>builder().build();
    }


}
