package com.themevariation.backend.controller;

import com.themevariation.backend.model.Compte;
import com.themevariation.backend.repository.CompteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteRepository compteRepository;

    public CompteController(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @GetMapping("/eleves")
    public ResponseEntity<List<Compte>> getEleves() {
        return ResponseEntity.ok(compteRepository.findByRole("ELEVE"));
    }
}
