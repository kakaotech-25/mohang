package moheng.auth.presentation.authentication;

import jakarta.servlet.http.HttpServletRequest;
import moheng.auth.domain.token.JwtTokenProvider;
import moheng.auth.dto.Accessor;
import moheng.auth.exception.BadRequestException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationBearerExtractor authenticationBearerExtractor;

    public AuthenticationArgumentResolver(final JwtTokenProvider jwtTokenProvider, final AuthenticationBearerExtractor authenticationBearerExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationBearerExtractor = authenticationBearerExtractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(Authentication.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer modelAndViewContainer,
                                  final NativeWebRequest nativeWebRequest, final WebDataBinderFactory webDataBinderFactory) {

        System.out.println("nqweqwekqwenqwleknqwen");
        final HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new BadRequestException("잘못된 HTTP 요청입니다.");
        }

        final String accessToken = authenticationBearerExtractor.extract(request);
        jwtTokenProvider.validateToken(accessToken);
        final Long id = jwtTokenProvider.getMemberId(accessToken);

        return new Accessor(id);
    }
}
