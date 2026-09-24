package com.campusnaksha.identity.token;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusnaksha.identity.exception.InvalidRefreshTokenException;
import com.campusnaksha.identity.user.User;

@Service
public class RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;

    private final Duration refreshTokenTtl;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${security.jwt.refresh-token-ttl}")
            Duration refreshTokenTtl
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenTtl = refreshTokenTtl;
    }

    @Transactional
    public IssuedRefreshToken create(User user) {

        String rawToken = generateRandomToken();

        String tokenHash = hash(rawToken);

        Instant now = Instant.now();

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID(),
                user,
                tokenHash,
                now.plus(refreshTokenTtl),
                null,
                now
        );

        refreshTokenRepository.save(refreshToken);

        return new IssuedRefreshToken(
                rawToken,
                refreshTokenTtl.toSeconds()
        );
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String rawToken) {

        if (rawToken == null || rawToken.isBlank()) {
            throw new InvalidRefreshTokenException();
        }

        String tokenHash = hash(rawToken);

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (refreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException();
        }

        if (refreshToken.isExpired()) {
            throw new InvalidRefreshTokenException();
        }

        if (!refreshToken.getUser().isEnabled()) {
            throw new InvalidRefreshTokenException();
        }

        return refreshToken;
    }

    @Transactional
    public void revoke(String rawToken) {

        RefreshToken refreshToken = validate(rawToken);

        refreshToken.revoke(Instant.now());

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public IssuedRefreshToken rotate(String rawToken) {

        RefreshToken existingToken = validate(rawToken);

        User user = existingToken.getUser();

        existingToken.revoke(Instant.now());

        refreshTokenRepository.save(existingToken);

        return create(user);
    }

    private String generateRandomToken() {

        byte[] bytes = new byte[64];

        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hash(String rawToken) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {

            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable",
                    exception
            );
        }
    }
}