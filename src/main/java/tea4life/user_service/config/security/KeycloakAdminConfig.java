package tea4life.user_service.config.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Admin 2/15/2026
 *
 **/
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KeycloakAdminConfig {

    private static final String DEFAULT_REALM_MASTER = "master";
    private static final String DEFAULT_CLIENT_ID = "admin-cli";

    @Value("${keycloak.server-url}")
    @NonFinal
    String serverUrl;

    @Value("${keycloak.admin.user-name}")
    @NonFinal
    String userName;

    @Value("${keycloak.admin.password}")
    @NonFinal
    String password;

    @Value("${keycloak.realm-master}")
    @NonFinal
    String realmMaster;

    @Value("${keycloak.client-id}")
    @NonFinal
    String clientId;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder
                .builder()
                .serverUrl(serverUrl)
                .realm(resolveRealmMaster())
                .clientId(resolveClientId())
                .grantType(OAuth2Constants.PASSWORD)
                .username(userName)
                .password(password)
                .build();
    }

    private String resolveRealmMaster() {
        if (realmMaster != null && !realmMaster.isBlank()) {
            return realmMaster.trim();
        }

        log.warn("keycloak.realm-master is blank. Falling back to {}", DEFAULT_REALM_MASTER);
        return DEFAULT_REALM_MASTER;
    }

    private String resolveClientId() {
        if (clientId != null && !clientId.isBlank()) {
            return clientId.trim();
        }

        log.warn("keycloak.client-id is blank. Falling back to {}", DEFAULT_CLIENT_ID);
        return DEFAULT_CLIENT_ID;
    }

}
