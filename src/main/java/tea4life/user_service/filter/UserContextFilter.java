package tea4life.user_service.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import tea4life.user_service.context.UserContext;

import java.io.IOException;

/**
 * Admin 2/7/2026
 *
 **/
@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        
        UserContext context = UserContext.builder()
                .keycloakId(httpRequest.getHeader("X-User-KeycloakId"))
                .email(httpRequest.getHeader("X-User-Email"))
                .build();

        UserContext.set(context);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.clear();
        }

    }
}
