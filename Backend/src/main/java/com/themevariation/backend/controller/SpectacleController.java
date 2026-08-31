package com.themevariation.backend.controller;

import com.themevariation.backend.dto.SpectacleRequest;
import com.themevariation.backend.model.Spectacle;
import com.themevariation.backend.service.SpectacleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/spectacles")
public class SpectacleController {

    private final SpectacleService spectacleService;

    public SpectacleController(SpectacleService spectacleService) {
        this.spectacleService = spectacleService;
    }

    @GetMapping
    public ResponseEntity<List<Spectacle>> getAll() {
        return ResponseEntity.ok(spectacleService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Spectacle> creer(@Valid @RequestBody SpectacleRequest request) {
        return ResponseEntity.ok(spectacleService.creer(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Spectacle> modifier(@PathVariable Long id,
                                              @Valid @RequestBody SpectacleRequest request) {
        return ResponseEntity.ok(spectacleService.modifier(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        spectacleService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/image")
    public ResponseEntity<Spectacle> uploadImage(@PathVariable Long id,
                                                  @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(spectacleService.uploadImage(id, file));
    }
}
