package com.themevariation.backend.controller;

import com.themevariation.backend.dto.RegisterRequest;
import com.themevariation.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request){
        authService.RegisterRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
