package tea4life.user_service.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tea4life.user_service.model.User;
import tea4life.user_service.repository.UserRepository;

/**
 * Admin 2/3/2026
 *
 **/
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSyncConsumer {

    UserRepository userRepository;
    ObjectMapper objectMapper;

    @KafkaListener(topics = "user-registration", groupId = "tea4life-user-group")
    public void listenUserRegistration(String message) {
        try {
            log.info("Received user registration event from Kafka. Payload: {}", message);

            JsonNode payload = objectMapper.readTree(message);
            String keycloakId = payload.get("userId").asText();
            String email = payload.get("email").asText();

            if (userRepository.existsByKeycloakId(keycloakId)) {
                log.warn("User with Keycloak ID: {} already exists. Skipping synchronization.", keycloakId);
                return;
            }


            User newUser = new User();
            newUser.setKeycloakId(keycloakId);
            newUser.setEmail(email);

            userRepository.save(newUser);
            log.info("Successfully synchronized user [{}] to the local database.", email);

        } catch (Exception e) {
            log.error("Failed to synchronize user from Kafka. Error: {}", e.getMessage(), e);
        }
    }


}
