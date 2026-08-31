package com.themevariation.backend.controller;

import com.themevariation.backend.dto.InscriptionPubliqueRequest;
import com.themevariation.backend.dto.InscriptionRequest;
import com.themevariation.backend.model.Inscription;
import com.themevariation.backend.service.InscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Inscription>> getAll() {
        return ResponseEntity.ok(inscriptionService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Inscription> creer(@Valid @RequestBody InscriptionRequest request) {
        return ResponseEntity.ok(inscriptionService.creer(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/statut")
    public ResponseEntity<Inscription> updateStatut(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(inscriptionService.updateStatut(id, body.get("statut")));
    }

    @PostMapping("/publique")
    public ResponseEntity<Map<String, Object>> creerPublique(@Valid @RequestBody InscriptionPubliqueRequest request) {
        return ResponseEntity.ok(inscriptionService.creerPublique(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        inscriptionService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
