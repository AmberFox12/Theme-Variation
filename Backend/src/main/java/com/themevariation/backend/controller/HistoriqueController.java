package com.themevariation.backend.controller;

import com.themevariation.backend.dto.HistoriqueRequest;
import com.themevariation.backend.model.Historique;
import com.themevariation.backend.service.HistoriqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historique")
public class HistoriqueController {

    private final HistoriqueService historiqueService;

    public HistoriqueController(HistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @GetMapping
    public ResponseEntity<List<Historique>> getAll() {
        return ResponseEntity.ok(historiqueService.getAll());
    }

    @PostMapping
    public ResponseEntity<Historique> creer(@RequestBody HistoriqueRequest request) {
        return ResponseEntity.ok(historiqueService.creer(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Historique> modifier(@PathVariable Long id,
                                               @RequestBody HistoriqueRequest request) {
        return ResponseEntity.ok(historiqueService.modifier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        historiqueService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
