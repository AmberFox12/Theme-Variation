package com.themevariation.backend.controller;

import com.themevariation.backend.dto.EleveRequest;
import com.themevariation.backend.model.Eleve;
import com.themevariation.backend.repository.EleveRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eleves")
public class EleveController {

    private final EleveRepository eleveRepository;

    public EleveController(EleveRepository eleveRepository) {
        this.eleveRepository = eleveRepository;
    }

    @GetMapping
    public ResponseEntity<List<Eleve>> getAll() {
        return ResponseEntity.ok(eleveRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Eleve> creer(@RequestBody EleveRequest request) {
        Eleve eleve = new Eleve();
        eleve.setNom(request.getNom());
        eleve.setPrenom(request.getPrenom());
        eleve.setEmail(request.getEmail());
        eleve.setTelephone(request.getTelephone());
        if (request.getDateNaissance() != null && !request.getDateNaissance().isBlank()) {
            eleve.setDateNaissance(LocalDate.parse(request.getDateNaissance()));
        }
        return ResponseEntity.ok(eleveRepository.save(eleve));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        eleveRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
