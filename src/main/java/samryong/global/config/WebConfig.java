package samryong.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import samryong.security.resolver.AuthMemberArgumentResolver;
import samryong.security.resolver.GetTokenArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final GetTokenArgumentResolver getTokenArgumentResolver;
    private final AuthMemberArgumentResolver authMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(getTokenArgumentResolver);
        resolvers.add(authMemberArgumentResolver);
    }
}
