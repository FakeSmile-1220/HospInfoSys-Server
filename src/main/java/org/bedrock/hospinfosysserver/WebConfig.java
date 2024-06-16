package org.bedrock.hospinfosysserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestsLoginCheckInterceptor requestsLoginCheckInterceptor;

    @Autowired
    public WebConfig(RequestsLoginCheckInterceptor requestsLoginCheckInterceptor) {
        this.requestsLoginCheckInterceptor = requestsLoginCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestsLoginCheckInterceptor)
                .addPathPatterns("/**");
    }
}
