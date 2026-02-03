package tea4life.user_service.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Admin 1/26/2026
 */
@Configuration
public class SpringContextConfig {

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }
}
