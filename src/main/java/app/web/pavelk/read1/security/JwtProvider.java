package app.web.pavelk.read1.security;


import app.web.pavelk.read1.dto.AuthenticationResponse;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration}")
    private Long exp;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore", e);
        }
    }

    public AuthenticationResponse generateToken(org.springframework.security.core.userdetails.User principal) {
        Claims claims = Jwts.claims().setSubject(principal.getUsername());
        claims.put("role", principal.getAuthorities().toString());

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeNowPlus = localDateTimeNow.plusMinutes(exp);

        return AuthenticationResponse.builder()
                .authenticationToken(
                        Jwts.builder()
                                .setClaims(claims)
                                .setIssuedAt(java.sql.Timestamp.valueOf(localDateTimeNow))
                                .signWith(getPrivateKey())
                                .setExpiration(java.sql.Timestamp.valueOf(localDateTimeNowPlus))
                                .compact()
                )
                .expiresAt(localDateTimeNowPlus)
                .username(principal.getUsername()).build();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while " +
                    "retrieving public key from keystore", e);
        }
    }

    public String getUsernameAndValidateJwt(String token) {
        return Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
