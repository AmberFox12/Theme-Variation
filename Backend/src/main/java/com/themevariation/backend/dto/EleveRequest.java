package com.themevariation.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EleveRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private String dateNaissance; // format yyyy-MM-dd, optionnel

    @Email(message = "Format d'email invalide")
    private String email;

    private String telephone;

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getDateNaissance() { return dateNaissance; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
}
