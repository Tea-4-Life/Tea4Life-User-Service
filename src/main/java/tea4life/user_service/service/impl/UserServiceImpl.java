package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.client.StorageClient;
import tea4life.user_service.context.UserContext;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.FileMoveRequest;
import tea4life.user_service.dto.request.OnboardingRequest;
import tea4life.user_service.dto.request.UpdateAvatarRequest;
import tea4life.user_service.dto.request.UpdateProfileRequest;
import tea4life.user_service.dto.response.UserProfileResponse;
import tea4life.user_service.model.User;
import tea4life.user_service.repository.UserRepository;
import tea4life.user_service.service.UserService;

/**
 * Admin 2/8/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    StorageClient storageClient;

    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void processOnboarding(OnboardingRequest onboardingRequest) {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));


        try {
            if (onboardingRequest.avatarKey() != null && !onboardingRequest.avatarKey().isBlank()) {
                String destinationPath = "users/avatars/" + user.getId();
                ApiResponse<String> storageResponse = storageClient.confirmFile(
                        new FileMoveRequest(
                                onboardingRequest.avatarKey(),
                                destinationPath
                        )
                );

                if (storageResponse.getErrorCode() != null)
                    throw new RuntimeException("Lỗi di chuyển file: " + storageResponse.getErrorMessage());
                user.setAvatarUrl(storageResponse.getData());
            }


            user.setFullName(onboardingRequest.fullName());
            user.setPhone(onboardingRequest.phone());
            user.setDob(onboardingRequest.dob());
            user.setGender(onboardingRequest.gender());
            user.setOnBoarded(true);


            userRepository.save(user);
            log.info("Onboarding thành công cho user: {}", user.getId());

        } catch (Exception e) {
            log.error("Onboarding thất bại cho user {}: {}", user.getId(), e.getMessage());
            throw e;
        }

    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile() {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        return UserProfileResponse
                .builder()
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .dob(user.getDob())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .id(user.getId().toString())
                .build();
    }

    @Override
    public void updateUserProfile(UpdateProfileRequest request) {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setDob(request.dob());
        user.setGender(request.gender());
    }

    @Override
    public void updateUserAvatar(UpdateAvatarRequest request) {
        String email = UserContext.get().getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        if (request.avatarKey() != null && !request.avatarKey().isBlank()) {
            String oldAvatarUrl = user.getAvatarUrl();
            String destinationPath = "users/avatars/" + user.getId();

            ApiResponse<String> storageResponse = storageClient.confirmFile(
                    new FileMoveRequest(
                            request.avatarKey(),
                            destinationPath
                    )
            );

            if (storageResponse.getErrorCode() != null)
                throw new RuntimeException("Lỗi di chuyển file: " + storageResponse.getErrorMessage());
            user.setAvatarUrl(storageResponse.getData());

            kafkaTemplate.send("storage-delete-file-topic", oldAvatarUrl);
        }
    }

}
