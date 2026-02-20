package tea4life.user_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.response.UserPermissionsResponse;
import tea4life.user_service.service.UserService;

/**
 * Admin 2/20/2026
 *
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserInternalController {

    UserService userService;

    @GetMapping("/{keycloakId}/permissions")
    public ApiResponse<UserPermissionsResponse> getUserPermissions(@PathVariable String keycloakId) {
        return ApiResponse.<UserPermissionsResponse>builder()
                .data(userService.getUserPermissions(keycloakId))
                .build();
    }

}
