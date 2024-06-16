package org.bedrock.hospinfosysserver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Objects;

@Component
public class RequestsLoginCheckInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public RequestsLoginCheckInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        String requestUrl = request.getRequestURI();

        if (requestUrl.contains("login") || requestUrl.contains("register")) {
            return true;
        }

        String jwt = request.getHeader("Token");
        if (jwt == null) {
            ResponseEntity<String> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(Objects.requireNonNull(unauthorizedResponse.getBody()));
            return false;
        }

        if (requestUrl.contains("admin") && !tokenService.getTokenClaim(jwt, "type").equals("admin")) {
            ResponseEntity<String> forbiddenResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Admin access required.");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(Objects.requireNonNull(forbiddenResponse.getBody()));
            return false;
        }

        if (!tokenService.verifyToken(jwt)) {
            ResponseEntity<String> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(Objects.requireNonNull(unauthorizedResponse.getBody()));
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) {
        // No implementation needed
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                final Exception ex) {
        // No implementation needed
    }
}
