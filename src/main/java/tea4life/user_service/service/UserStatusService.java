package tea4life.user_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tea4life.user_service.context.UserContext;
import tea4life.user_service.dto.response.UserStatusResponse;
import tea4life.user_service.dto.response.constant.UserStatus;
import tea4life.user_service.repository.UserRepository;

/**
 * Admin 2/6/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStatusService {

    UserRepository userRepository;

    StringRedisTemplate stringRedisTemplate;

    public UserStatusResponse checkUserStatus() {
        String email = UserContext.get().getEmail();

        return userRepository
                .findByEmail(email)
                .map(user -> new UserStatusResponse(
                        UserStatus.SUCCESS,
                        true,
                        user.getOnBoarded())
                )
                .orElseGet(() -> {
                    String pending = stringRedisTemplate.opsForValue().get("PENDING_USER:" + email);

                    if (pending != null)
                        return new UserStatusResponse(UserStatus.PROCESSING, false, false);

                    return new UserStatusResponse(
                            UserStatus.NOT_FOUND,
                            false,
                            false);
                });
    }

}
