package moheng.auth.presentation.initauthentication;

import moheng.auth.presentation.authentication.AuthenticationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class InitAuthenticationWebConfig implements WebMvcConfigurer {
    private final InitAuthenticationArgumentResolver initAuthenticationArgumentResolver;

    public InitAuthenticationWebConfig(final InitAuthenticationArgumentResolver initAuthenticationArgumentResolver) {
        this.initAuthenticationArgumentResolver = initAuthenticationArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(initAuthenticationArgumentResolver);
    }
}
