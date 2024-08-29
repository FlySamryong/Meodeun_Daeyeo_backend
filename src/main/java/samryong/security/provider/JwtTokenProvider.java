package samryong.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidityMilliSeconds;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidityMilliSeconds;

    private Key key;

    @PostConstruct
    public void initKey() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long memberId) {
        return generateToken(memberId, accessTokenValidityMilliSeconds);
    }

    public String generateRefreshToken(Long memberId) {
        return generateToken(memberId, refreshTokenValidityMilliSeconds);
    }

    public String generateToken(Long memberId, long validity) {

        long now = (new Date()).getTime();
        Date validityDate = new Date(now + validity);
        Claims claims = Jwts.claims();
        claims.put("id", memberId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(validityDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException
                | MalformedJwtException
                | IllegalArgumentException
                | SignatureException e) {
            throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new GlobalException(GlobalErrorCode.AUTH_EXPIRED_TOKEN);
        }
    }

    public String resolveBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Long getMemberId(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .get("id")
                        .toString());
    }
}
