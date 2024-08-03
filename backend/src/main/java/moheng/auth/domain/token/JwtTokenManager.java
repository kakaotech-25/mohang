package moheng.auth.domain.token;

import io.jsonwebtoken.ExpiredJwtException;
import moheng.auth.exception.InvalidTokenException;
import moheng.auth.exception.NoExistMemberTokenException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager implements TokenManager {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public JwtTokenManager(final RefreshTokenRepository refreshTokenRepository,
                           final TokenProvider tokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public MemberToken createMemberToken(final long memberId) {
        String accessToken = tokenProvider.createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);
        return new MemberToken(accessToken, refreshToken);
    }

    private String createRefreshToken(final long memberId) {
        if (refreshTokenRepository.existsById(memberId)) {
            return refreshTokenRepository.findById(memberId);
        }
        String refreshToken = tokenProvider.createRefreshToken(memberId);
        refreshTokenRepository.save(memberId, refreshToken);
        return refreshToken;
    }

    @Override
    public String generateRenewalAccessToken(final String refreshToken) {
        if(tokenProvider.isRefreshTokenExpired(refreshToken)) {
            refreshTokenRepository.deleteByRefreshToken(refreshToken);
            throw new InvalidTokenException("Invalid refresh token");
        }

        Long memberId = tokenProvider.getMemberId(refreshToken);
        if(!refreshTokenRepository.existsById(memberId)) {
            throw new NoExistMemberTokenException("존재하지 않는 유저의 토큰입니다.");
        }

        return tokenProvider.createAccessToken(memberId);
    }

    @Override
    public Long getMemberId(final String token) {
        tokenProvider.validateToken(token);
        return tokenProvider.getMemberId(token);
    }

    @Override
    public void removeRefreshToken(final String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        long memberId = getMemberId(refreshToken);

        if(!refreshTokenRepository.existsById(memberId)) {
            throw new NoExistMemberTokenException("존재하지 않는 유저의 토큰입니다.");
        }
        refreshTokenRepository.deleteById(memberId);
    }
}

