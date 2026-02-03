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
            JsonNode payload = objectMapper.readTree(message);
            String action = payload.path("action").asText("CREATE");
            String source = payload.path("source").asText("UNKNOWN");
            String userId = payload.path("userId").asText();

            log.info(
                    ">>>> [KAFKA-IN] [{}] Source: {} | UserID: {} | Email: {}",
                    String.format("%-6s", action), String.format("%-5s", source),
                    userId, payload.path("email").asText()
            );

            switch (action) {
                case "CREATE" -> handleCreateUser(payload, userId);

                case "DELETE" -> {

                }

                case "UPDATE" -> {

                }

                default -> {
                    log.warn("Unrecognized action: {}", action);
                }
            }

        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}", e.getMessage());
        }
    }

    private void handleCreateUser(JsonNode payload, String keycloakId) {
        if (userRepository.existsByKeycloakId(keycloakId)) {
            log.warn("User {} already exists. Skipping.", keycloakId);
            return;
        }

        User newUser = new User();
        newUser.setKeycloakId(keycloakId);
        newUser.setEmail(payload.path("email").asText("no-email"));

        userRepository.save(newUser);

        log.info("Successfully persisted new user to MariaDB.");
    }


}
