package qubiqule.cheqr.cheQR.security.jwt;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class JwtUtils {

    @Value("${cheqr.app.encryption_secret_key}")
    private String secretKey;

    @Value("${cheqr.app.jwtExpirationMs}")
    private int expiration;

    public String generateToken(String username) {

        SecretKey key = this.getSigningKey();

        return builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(key, SIG.HS256)
                .compact();
    }

    public String refreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return generateToken(claims.getSubject());
    }

    public Claims extractAllClaims(String token) {

        SecretKey key = getSigningKey();

        //sign the parser builder
        JwtParserBuilder parserBuilder = Jwts.parser()
                .verifyWith(key);

        //build the parser
        JwtParser parser = parserBuilder.build();

        //extract the claims
        Claims claim = parser.parseSignedClaims(token).getPayload();

        //return claims
        return claim;
    }

    private SecretKey getSigningKey() {
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String getUsernameFromToken(String token){
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        System.out.println("Token received: " + token);
        SecretKey key = getSigningKey();

        JwtParser parser = parser()
                .verifyWith(key)
                .build();

        try {
            Jws<Claims> result = parser.parseSignedClaims(token);
            result.getPayload().forEach((key1, value1) -> log.info("key: {}, value: {}", key1, value1));
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            if (e instanceof SignatureException) {
                log.error("Jwt token invalid exception", e);
                return false;
            } else if (e instanceof ExpiredJwtException) {
                log.error("Jwt token expired exception", e);
                return false;
            } else {
                log.error("Jwt exception", e);
                return false;
            }
        }
    }

}
