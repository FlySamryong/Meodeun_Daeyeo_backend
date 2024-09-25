package samryong.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import samryong.global.convert.MultipartJackson2HttpMessageConverter;
import samryong.security.resolver.AuthMemberArgumentResolver;
import samryong.security.resolver.GetTokenArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final GetTokenArgumentResolver getTokenArgumentResolver;
    private final AuthMemberArgumentResolver authMemberArgumentResolver;
    private final MultipartJackson2HttpMessageConverter multipartJackson2HttpMessageConverter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(getTokenArgumentResolver);
        resolvers.add(authMemberArgumentResolver);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(multipartJackson2HttpMessageConverter);
    }
}
