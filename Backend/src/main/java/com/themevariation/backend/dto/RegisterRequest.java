package com.themevariation.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String motDePasse;
    private String nom;
    private String prenom;
    private String telephone;
}