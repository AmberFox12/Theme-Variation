package com.themevariation.backend.service;

import com.themevariation.backend.dto.LoginRequest;
import com.themevariation.backend.dto.RegisterRequest;
import com.themevariation.backend.model.Compte;
import com.themevariation.backend.repository.CompteRepository;
import com.themevariation.backend.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CompteRepository compteRepository;
    private final JwtUtil jwtUtil;

    public AuthService(CompteRepository compteRepository, JwtUtil jwtUtil) {
        this.compteRepository = compteRepository;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest request) {
        Compte compte = new Compte();
        compte.setEmail(request.getEmail());
        compte.setMotDePasse(request.getMotDePasse());
        compte.setNom(request.getNom());
        compte.setPrenom(request.getPrenom());
        compte.setTelephone(request.getTelephone());
        compte.setRole("ELEVE");
        compteRepository.save(compte);
    }

    public String login(LoginRequest request) {
        Compte compte = compteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email introuvable"));

        if (!request.getMotDePasse().equals(compte.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return jwtUtil.generateToken(compte.getEmail(), compte.getRole());
    }
}
