package com.themevariation.backend.controller;

import com.themevariation.backend.dto.LoginRequest;
import com.themevariation.backend.dto.MotDePasseOublieRequest;
import com.themevariation.backend.dto.ReinitialiserMotDePasseRequest;
import com.themevariation.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/mot-de-passe-oublie")
    public ResponseEntity<Map<String, String>> motDePasseOublie(@Valid @RequestBody MotDePasseOublieRequest request) {
        authService.demanderReset(request);
        return ResponseEntity.ok(Map.of("message", "Si cet email est associé à un compte, un lien de réinitialisation a été envoyé."));
    }

    @PostMapping("/reinitialiser-mot-de-passe")
    public ResponseEntity<Map<String, String>> reinitialiserMotDePasse(@Valid @RequestBody ReinitialiserMotDePasseRequest request) {
        authService.reinitialiserMotDePasse(request);
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès."));
    }
}
