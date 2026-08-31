package com.themevariation.backend.service;

import com.themevariation.backend.dto.LoginRequest;
import com.themevariation.backend.dto.MotDePasseOublieRequest;
import com.themevariation.backend.dto.ReinitialiserMotDePasseRequest;
import com.themevariation.backend.model.Compte;
import com.themevariation.backend.model.ResetPasswordToken;
import com.themevariation.backend.repository.CompteRepository;
import com.themevariation.backend.repository.ResetPasswordTokenRepository;
import com.themevariation.backend.security.JwtUtil;
import com.themevariation.backend.security.LoginAttemptService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final CompteRepository compteRepository;
    private final ResetPasswordTokenRepository resetTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:https://themeetvariations.fr}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public AuthService(CompteRepository compteRepository,
                       ResetPasswordTokenRepository resetTokenRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       LoginAttemptService loginAttemptService,
                       JavaMailSender mailSender) {
        this.compteRepository = compteRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.mailSender = mailSender;
    }

    public String login(LoginRequest request) {
        loginAttemptService.verifierNonBloque(request.getEmail());

        Compte compte = compteRepository.findByEmail(request.getEmail()).orElse(null);
        if (compte == null || !passwordEncoder.matches(request.getMotDePasse(), compte.getMotDePasse())) {
            loginAttemptService.enregistrerEchec(request.getEmail());
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        loginAttemptService.reinitialiser(request.getEmail());
        return jwtUtil.generateToken(compte.getEmail(), compte.getRole());
    }

    @Transactional
    public void demanderReset(MotDePasseOublieRequest request) {
        // On répond toujours OK pour ne pas révéler si l'email existe
        compteRepository.findByEmail(request.getEmail()).ifPresent(compte -> {
            resetTokenRepository.deleteByEmail(request.getEmail());
            resetTokenRepository.deleteByExpiryBefore(LocalDateTime.now());

            String token = UUID.randomUUID().toString();
            ResetPasswordToken resetToken = new ResetPasswordToken();
            resetToken.setToken(token);
            resetToken.setEmail(request.getEmail());
            resetToken.setExpiry(LocalDateTime.now().plusHours(1));
            resetTokenRepository.save(resetToken);

            String lien = frontendUrl + "/reinitialiser-mot-de-passe?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(request.getEmail());
            message.setSubject("Réinitialisation de votre mot de passe — Thème et Variations");
            message.setText(
                "Bonjour " + compte.getPrenom() + ",\n\n" +
                "Vous avez demandé à réinitialiser votre mot de passe.\n\n" +
                "Cliquez sur le lien suivant (valable 1 heure) :\n" +
                lien + "\n\n" +
                "Si vous n'êtes pas à l'origine de cette demande, ignorez cet email.\n\n" +
                "L'équipe Thème et Variations"
            );
            mailSender.send(message);
        });
    }

    @Transactional
    public void reinitialiserMotDePasse(ReinitialiserMotDePasseRequest request) {
        ResetPasswordToken resetToken = resetTokenRepository.findByToken(request.getToken())
            .orElseThrow(() -> new RuntimeException("Lien invalide ou expiré"));

        if (resetToken.getExpiry().isBefore(LocalDateTime.now())) {
            resetTokenRepository.delete(resetToken);
            throw new RuntimeException("Lien invalide ou expiré");
        }

        Compte compte = compteRepository.findByEmail(resetToken.getEmail())
            .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        compte.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        compteRepository.save(compte);

        resetTokenRepository.delete(resetToken);
        loginAttemptService.reinitialiser(compte.getEmail());
    }
}
