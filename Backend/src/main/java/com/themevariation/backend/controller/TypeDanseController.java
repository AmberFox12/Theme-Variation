package com.themevariation.backend.controller;

import com.themevariation.backend.model.TypeDanse;
import com.themevariation.backend.repository.TypeDanseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types-danse")
public class TypeDanseController {

    private final TypeDanseRepository typeDanseRepository;

    public TypeDanseController(TypeDanseRepository typeDanseRepository) {
        this.typeDanseRepository = typeDanseRepository;
    }

    @GetMapping
    public ResponseEntity<List<TypeDanse>> getAll() {
        return ResponseEntity.ok(typeDanseRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<TypeDanse> create(@RequestBody TypeDanse typeDanse) {
        return ResponseEntity.ok(typeDanseRepository.save(typeDanse));
    }
}
