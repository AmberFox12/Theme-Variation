package com.themevariation.backend.repository;

import com.themevariation.backend.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);
    void deleteByEmail(String email);
    void deleteByExpiryBefore(LocalDateTime date);
}
