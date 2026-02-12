package tea4life.user_service.service;

import tea4life.user_service.dto.request.OnboardingRequest;
import tea4life.user_service.dto.request.UpdateProfileRequest;
import tea4life.user_service.dto.response.UserProfileResponse;

/**
 * Admin 2/8/2026
 *
 **/
public interface UserService {
    void processOnboarding(OnboardingRequest onboardingRequest);

    UserProfileResponse getUserProfile();

    void updateUserProfile(UpdateProfileRequest request);
}
