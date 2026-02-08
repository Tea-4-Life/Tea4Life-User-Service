package tea4life.user_service.service;

import tea4life.user_service.dto.request.OnboardingRequest;

/**
 * Admin 2/8/2026
 *
 **/
public interface UserService {
    void processOnboarding(OnboardingRequest onboardingRequest);
}
