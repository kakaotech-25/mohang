package moheng.auth.presentation.authentication;

import jakarta.servlet.http.HttpServletRequest;
import moheng.auth.domain.oauth.Authority;
import moheng.auth.domain.token.JwtTokenProvider;
import moheng.auth.dto.Accessor;
import moheng.auth.exception.BadRequestException;
import moheng.auth.exception.InvalidRegularAuthorityException;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
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
    private final MemberRepository memberRepository;

    public AuthenticationArgumentResolver(final JwtTokenProvider jwtTokenProvider,
                                          final AuthenticationBearerExtractor authenticationBearerExtractor,
                                          final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationBearerExtractor = authenticationBearerExtractor;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authentication.class);
    }

    @Override
    public Accessor resolveArgument(
            final MethodParameter methodParameter,
            final ModelAndViewContainer modelAndViewContainer,
            final NativeWebRequest nativeWebRequest,
            final WebDataBinderFactory webDataBinderFactory) {
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
