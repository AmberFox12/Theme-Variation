package com.themevariation.backend.controller;

import com.themevariation.backend.dto.InscriptionPubliqueRequest;
import com.themevariation.backend.dto.InscriptionRequest;
import com.themevariation.backend.model.Inscription;
import com.themevariation.backend.service.InscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionController {

    private final InscriptionService inscriptionService;

    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> getAll() {
        return ResponseEntity.ok(inscriptionService.getAll());
    }

    @PostMapping
    public ResponseEntity<Inscription> creer(@RequestBody InscriptionRequest request) {
        return ResponseEntity.ok(inscriptionService.creer(request));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Inscription> updateStatut(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(inscriptionService.updateStatut(id, body.get("statut")));
    }

    @PostMapping("/publique")
    public ResponseEntity<?> creerPublique(@RequestBody InscriptionPubliqueRequest request) {
        try {
            return ResponseEntity.ok(inscriptionService.creerPublique(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        inscriptionService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
