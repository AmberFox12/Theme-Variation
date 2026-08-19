package com.themevariation.backend.controller;

import com.themevariation.backend.dto.CoursRequest;
import com.themevariation.backend.model.Cours;
import com.themevariation.backend.service.CoursService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Cours> creer(@RequestBody CoursRequest request) {
        return ResponseEntity.ok(coursService.creer(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cours> modifier(@PathVariable Long id, @RequestBody CoursRequest request) {
        return ResponseEntity.ok(coursService.modifier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        coursService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
