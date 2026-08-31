package com.themevariation.backend.service;

import com.themevariation.backend.dto.LoginRequest;
import com.themevariation.backend.model.Compte;
import com.themevariation.backend.repository.CompteRepository;
import com.themevariation.backend.security.JwtUtil;
import com.themevariation.backend.security.LoginAttemptService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CompteRepository compteRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public AuthService(CompteRepository compteRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
                       LoginAttemptService loginAttemptService) {
        this.compteRepository = compteRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
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
}
