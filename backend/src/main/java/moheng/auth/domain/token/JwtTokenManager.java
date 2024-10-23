package moheng.auth.domain.token;

import moheng.auth.exception.InvalidTokenException;
import moheng.auth.exception.NoExistMemberTokenException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager implements TokenManager {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenGenerator tokenProvider;

    public JwtTokenManager(final RefreshTokenRepository refreshTokenRepository,
                           final TokenGenerator tokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public MemberToken createMemberToken(final long memberId) {
        final String accessToken = tokenProvider.createAccessToken(memberId);
        final String refreshToken = createRefreshToken(memberId);
        return new MemberToken(accessToken, refreshToken);
    }

    private String createRefreshToken(final long memberId) {
        if (refreshTokenRepository.existsById(memberId)) {
            return refreshTokenRepository.findById(memberId);
        }
        final String refreshToken = tokenProvider.createRefreshToken(memberId);
        refreshTokenRepository.save(memberId, refreshToken);
        return refreshToken;
    }

    @Override
    public RenewalToken generateRenewalAccessToken(final String refreshToken) {
        deleteExpiredRefreshToken(refreshToken);

        final Long memberId = tokenProvider.getMemberId(refreshToken);
        if(!refreshTokenRepository.existsById(memberId)) {
            throw new NoExistMemberTokenException("존재하지 않는 유저의 토큰입니다.");
        }

        final String accessTokenForRenew = tokenProvider.createAccessToken(memberId);
        final String refreshTokenForRenew = refreshTokenRepository.findById(memberId);
        final RenewalToken renewalToken = new RenewalToken(accessTokenForRenew, refreshTokenForRenew);
        renewalToken.validateHasSameRefreshToken(refreshToken);

        return renewalToken;
    }

    private void deleteExpiredRefreshToken(final String refreshToken) {
        if(tokenProvider.isRefreshTokenExpired(refreshToken)) {
            refreshTokenRepository.deleteByRefreshToken(refreshToken);
            throw new InvalidTokenException("변조되었거나 만료된 토큰 입니다.");
        }
    }

    @Override
    public Long getMemberId(final String token) {
        tokenProvider.validateToken(token);
        return tokenProvider.getMemberId(token);
    }

    @Override
    public void removeRefreshToken(final String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        final long memberId = getMemberId(refreshToken);

        if(!refreshTokenRepository.existsById(memberId)) {
            throw new NoExistMemberTokenException("존재하지 않는 유저의 토큰입니다.");
        }
        refreshTokenRepository.deleteById(memberId);
    }
}

