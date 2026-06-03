package tea4life.user_service.controller.internal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.OnboardingRequest;
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

    @RequestMapping(
            value = "/{keycloakId}/role/{roleName}",
            method = {RequestMethod.PATCH, RequestMethod.POST}
    )
    public ApiResponse<Void> assignRole(
            @PathVariable String keycloakId,
            @PathVariable String roleName
    ) {
        userService.assignRoleByName(keycloakId, roleName);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/{keycloakId}/driver-role/member")
    public ApiResponse<Void> downgradeDriverRoleToMember(@PathVariable String keycloakId) {
        userService.downgradeDriverRoleToMember(keycloakId);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/{userId}/onboarding")
    public ApiResponse<Void> processOnboarding(@PathVariable Long userId,
                                               @RequestBody OnboardingRequest request) {
        userService.processOnboarding(userId, request);
        return ApiResponse.<Void>builder().build();
    }

}
