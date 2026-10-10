package com.campusnaksha.identity.token;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(
            String tokenHash
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select rt
            from RefreshToken rt
            join fetch rt.user
            where rt.tokenHash = :tokenHash
            """)
    Optional<RefreshToken> findByTokenHashForUpdate(
            @Param("tokenHash")
            String tokenHash
    );
}