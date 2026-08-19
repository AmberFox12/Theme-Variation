package com.themevariation.backend.controller;

import com.themevariation.backend.dto.EvenementDto;
import com.themevariation.backend.model.Evenement;
import com.themevariation.backend.service.AgendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public ResponseEntity<List<Evenement>> getEvenements() {
        return ResponseEntity.ok(agendaService.getEvenements());
    }

    @GetMapping("/prochain")
    public ResponseEntity<Evenement> getProchainEvenement() {
        return agendaService.getProchainEvenement()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<Evenement> creer(@RequestBody EvenementDto dto) {
        return ResponseEntity.ok(agendaService.creer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evenement> modifier(@PathVariable Long id, @RequestBody EvenementDto dto) {
        return ResponseEntity.ok(agendaService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        agendaService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
