package com.themevariation.backend.controller;

import com.themevariation.backend.dto.CoursRequest;
import com.themevariation.backend.model.Cours;
import com.themevariation.backend.service.CoursService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
public class CoursController {

    private final CoursService coursService;

    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    @GetMapping
    public ResponseEntity<List<Cours>> getCours() {
        return ResponseEntity.ok(coursService.getCours());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Cours> creer(@Valid @RequestBody CoursRequest request) {
        return ResponseEntity.ok(coursService.creer(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Cours> modifier(@PathVariable Long id, @Valid @RequestBody CoursRequest request) {
        return ResponseEntity.ok(coursService.modifier(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        coursService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
