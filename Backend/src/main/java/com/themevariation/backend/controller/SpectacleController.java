package com.themevariation.backend.controller;

import com.themevariation.backend.dto.SpectacleRequest;
import com.themevariation.backend.model.Spectacle;
import com.themevariation.backend.service.SpectacleService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Spectacle> creer(@RequestBody SpectacleRequest request) {
        return ResponseEntity.ok(spectacleService.creer(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spectacle> modifier(@PathVariable Long id,
                                              @RequestBody SpectacleRequest request) {
        return ResponseEntity.ok(spectacleService.modifier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        spectacleService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Spectacle> uploadImage(@PathVariable Long id,
                                                  @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(spectacleService.uploadImage(id, file));
    }
}
