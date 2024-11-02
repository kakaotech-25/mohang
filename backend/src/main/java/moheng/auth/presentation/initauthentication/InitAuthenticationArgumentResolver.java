package moheng.auth.presentation.initauthentication;

import jakarta.servlet.http.HttpServletRequest;
import moheng.auth.application.AuthService;
import moheng.auth.domain.oauth.Authority;
import moheng.auth.dto.Accessor;
import moheng.auth.exception.BadRequestException;
import moheng.auth.exception.InvalidInitAuthorityException;
import moheng.auth.presentation.authentication.AuthenticationBearerExtractor;
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
public class InitAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final AuthenticationBearerExtractor authenticationBearerExtractor;
    private final MemberRepository memberRepository;

    public InitAuthenticationArgumentResolver(final AuthService authService,
                                              final AuthenticationBearerExtractor authenticationBearerExtractor,
                                              final MemberRepository memberRepository) {
        this.authService = authService;
        this.authenticationBearerExtractor = authenticationBearerExtractor;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(InitAuthentication.class);
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
        final Long id = authService.extractMemberId(accessToken);

        final Member member = memberRepository.findById(id)
                .orElseThrow(NoExistMemberException::new);

        if(!member.getAuthority().equals(Authority.INIT_MEMBER)) {
            throw new InvalidInitAuthorityException("초기 회원가입 기능에 대한 접근 권한이 없습니다.");
        }

        return new Accessor(id);
    }
}
