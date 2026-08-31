package com.themevariation.backend.controller;

import com.themevariation.backend.dto.HistoriqueRequest;
import com.themevariation.backend.model.Historique;
import com.themevariation.backend.service.HistoriqueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Endpoint additionnel, prêt pour le jour où la page Archives aura besoin de pagination.
    // N'est appelé par rien pour l'instant : getAll() reste inchangé.
    @GetMapping("/page")
    public ResponseEntity<Page<Historique>> getPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(historiqueService.getPage(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Historique> creer(@Valid @RequestBody HistoriqueRequest request) {
        return ResponseEntity.ok(historiqueService.creer(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Historique> modifier(@PathVariable Long id,
                                               @Valid @RequestBody HistoriqueRequest request) {
        return ResponseEntity.ok(historiqueService.modifier(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        historiqueService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
