package tea4life.user_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.response.UserStatusResponse;
import tea4life.user_service.service.UserStatusService;

/**
 * Admin 2/6/2026
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStatusController {

    UserStatusService userStatusService;

    @GetMapping("/users/exists")
    public ApiResponse<UserStatusResponse> checkUserStatus() {
        return new ApiResponse<>(userStatusService.checkUserStatus());
    }


}
