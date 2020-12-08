package app.web.pavelk.read1.service;


import app.web.pavelk.read1.exceptions.InvalidTokenException;
import app.web.pavelk.read1.model.RefreshToken;
import app.web.pavelk.read1.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        return refreshTokenRepository.save(RefreshToken.builder()
                .createdDate(Instant.now()).token(UUID.randomUUID().toString()).build());
    }

    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh Token"));
    }

    public ResponseEntity<String> deleteRefreshToken(String token) {
        log.info("deleteRefreshToken");
        refreshTokenRepository.deleteByToken(token);
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!");
    }
}
