package moheng.auth.domain;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import moheng.auth.exception.InvalidTokenException;
import moheng.auth.exception.NoExistMemberTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenProvider {
    private final SecretKey secretKey;
    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret_key}") final String secretKey,
            @Value("${security.jwt.token.expire_length.access_token}") final long accessTokenValidityInSeconds,
            @Value("${security.jwt.token.expire_length.refresh_token}") final long refreshTokenValidityInSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
    }

    @Override
    public String createAccessToken(final long memberId) {
        return createToken(String.valueOf(memberId), accessTokenValidityInSeconds);
    }

    @Override
    public String createRefreshToken(final long memberId) {
        return createToken(String.valueOf(memberId), refreshTokenValidityInSeconds);
    }

    @Override
    public Long getMemberId(final String token) {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    @Override
    public void validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("변조되었거나 만료된 토큰 입니다.");
        }
    }

    public MemberToken createMemberToken(final long memberId) {
        String accessToken = createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);
        return new MemberToken(accessToken, refreshToken);
    }

    public String createToken(final String payload, final long tokenValidityInSeconds) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}


