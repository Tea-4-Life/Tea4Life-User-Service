package tea4life.user_service.config.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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
public class KeycloakAdminConfig {

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
                .realm(realmMaster)
                .clientId(clientId)
                .grantType(OAuth2Constants.PASSWORD)
                .username(userName)
                .password(password)
                .build();
    }

}
