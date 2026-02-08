package tea4life.user_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.OnboardingRequest;
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

    @PostMapping("/users/find")
    public ApiResponse<@NonNull Void> processOnboarding(
            @RequestBody OnboardingRequest onboardingRequest
    ) {
        userService.processOnboarding(onboardingRequest);
        return ApiResponse.<Void>builder().build();
    }

}
