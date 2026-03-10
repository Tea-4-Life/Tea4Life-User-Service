package tea4life.user_service.config.feign;

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
                if (hasText(context.getKeycloakId())) {
                    template.header("X-User-KeycloakId", context.getKeycloakId());
                }
                if (hasText(context.getEmail())) {
                    template.header("X-User-Email", context.getEmail());
                }
            }
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
