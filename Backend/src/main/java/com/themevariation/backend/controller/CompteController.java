package com.themevariation.backend.controller;

import com.themevariation.backend.exception.ResourceNotFoundException;
import com.themevariation.backend.model.Compte;
import com.themevariation.backend.model.Eleve;
import com.themevariation.backend.repository.CompteRepository;
import com.themevariation.backend.repository.EleveRepository;
import com.themevariation.backend.repository.InscriptionRepository;
import com.themevariation.backend.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comptes")
@PreAuthorize("hasRole('ADMIN')")
public class CompteController {

    private final CompteRepository compteRepository;
    private final EleveRepository eleveRepository;
    private final InscriptionRepository inscriptionRepository;
    private final AuthService authService;

    public CompteController(CompteRepository compteRepository,
                            EleveRepository eleveRepository,
                            InscriptionRepository inscriptionRepository,
                            AuthService authService) {
        this.compteRepository = compteRepository;
        this.eleveRepository = eleveRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<Compte>> getAll() {
        return ResponseEntity.ok(compteRepository.findAll());
    }

    @GetMapping("/eleves")
    public ResponseEntity<List<Compte>> getEleves() {
        return ResponseEntity.ok(compteRepository.findByRole("ELEVE"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable"));
        List<Eleve> eleves = eleveRepository.findByCompte(compte);
        for (Eleve eleve : eleves) {
            inscriptionRepository.deleteByEleve(eleve);
        }
        eleveRepository.deleteAll(eleves);
        compteRepository.delete(compte);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/role")
    @Transactional
    public ResponseEntity<Compte> changerRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable"));

        String role = body.get("role");
        if (!role.equals("ADMIN") && !role.equals("ELEVE")) {
            throw new RuntimeException("Rôle invalide : " + role);
        }

        boolean promouVersAdmin = role.equals("ADMIN") && !compte.getRole().equals("ADMIN");
        compte.setRole(role);
        compteRepository.save(compte);

        if (promouVersAdmin) {
            try {
                authService.envoyerLienDefinitionMotDePasse(compte);
            } catch (Exception e) {
                // L'email échoue silencieusement pour ne pas bloquer le changement de rôle
            }
        }

        return ResponseEntity.ok(compte);
    }
}
