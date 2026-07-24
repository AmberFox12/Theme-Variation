package com.themevariation.backend.controller;

import com.themevariation.backend.model.Cours;
import com.themevariation.backend.service.CoursService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/cours")
public class CoursController {
    private final CoursService coursService;

    public CoursController(CoursService coursService){
        this.coursService = coursService;
    }

    @GetMapping
    public ResponseEntity<List<Cours>> getCours() {
        return ResponseEntity.ok(coursService.getCours());
    }
    
}
