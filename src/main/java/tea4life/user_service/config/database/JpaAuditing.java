package tea4life.user_service.config.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class JpaAuditing {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("anonymous");
    }

}
