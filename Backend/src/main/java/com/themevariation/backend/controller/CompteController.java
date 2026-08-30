package com.themevariation.backend.controller;

import com.themevariation.backend.model.Compte;
import com.themevariation.backend.model.Eleve;
import com.themevariation.backend.repository.CompteRepository;
import com.themevariation.backend.repository.EleveRepository;
import com.themevariation.backend.repository.InscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteRepository compteRepository;
    private final EleveRepository eleveRepository;
    private final InscriptionRepository inscriptionRepository;

    public CompteController(CompteRepository compteRepository,
                            EleveRepository eleveRepository,
                            InscriptionRepository inscriptionRepository) {
        this.compteRepository = compteRepository;
        this.eleveRepository = eleveRepository;
        this.inscriptionRepository = inscriptionRepository;
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
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        List<Eleve> eleves = eleveRepository.findByCompte(compte);
        for (Eleve eleve : eleves) {
            inscriptionRepository.deleteByEleve(eleve);
        }
        eleveRepository.deleteAll(eleves);
        compteRepository.delete(compte);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Compte> changerRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return compteRepository.findById(id).map(c -> {
            String role = body.get("role");
            if (!role.equals("ADMIN") && !role.equals("ELEVE")) {
                throw new RuntimeException("Rôle invalide : " + role);
            }
            c.setRole(role);
            return ResponseEntity.ok(compteRepository.save(c));
        }).orElseThrow(() -> new RuntimeException("Compte introuvable"));
    }
}
