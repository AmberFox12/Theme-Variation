package com.themevariation.backend.controller;

import com.themevariation.backend.model.Parametre;
import com.themevariation.backend.service.ParametreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parametres")
public class ParametreController {

    private final ParametreService parametreService;

    public ParametreController(ParametreService parametreService) {
        this.parametreService = parametreService;
    }

    @GetMapping
    public ResponseEntity<Parametre> get() {
        return ResponseEntity.ok(parametreService.get());
    }

    @PutMapping
    public ResponseEntity<Parametre> update(@RequestBody Parametre request) {
        return ResponseEntity.ok(parametreService.update(request));
    }
}
