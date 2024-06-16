package org.bedrock.hospinfosysserver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestsLoginCheckInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public RequestsLoginCheckInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        try {
            String requestUrl = request.getRequestURI();
            if (requestUrl.contains("login")) {
                return true;
            }

            String jwt = request.getHeader("token");
            if (jwt == null) {
                return false;
            }

            if (requestUrl.contains("admin") && !tokenService.getTokenClaim(jwt, "type").equals("admin")) {
                return false;
            }

            return tokenService.verifyToken(jwt);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
    }

}
