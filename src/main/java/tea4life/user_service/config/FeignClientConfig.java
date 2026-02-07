package tea4life.user_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tea4life.user_service.context.UserContext;

/**
 * Admin 2/8/2026
 *
 **/
@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (RequestTemplate template) -> {
            UserContext context = UserContext.get();

            if (context != null) {
                template.header("X-User-KeycloakId", context.getKeycloakId());
                template.header("X-User-Email", context.getEmail());
            }
        };
    }

}
