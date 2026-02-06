package tea4life.user_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.response.UserStatusResponse;
import tea4life.user_service.service.UserStatusService;

/**
 * Admin 2/6/2026
 *
 **/
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStatusController {

    UserStatusService userStatusService;

    @GetMapping("/exists/{email}")
    public ApiResponse<UserStatusResponse> exists(@PathVariable String email) {
        return new ApiResponse<>(userStatusService.checkUserStatus(email));
    }


}
