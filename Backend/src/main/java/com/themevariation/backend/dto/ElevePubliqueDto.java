package com.themevariation.backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ElevePubliqueDto {
    @NotBlank(message = "Le nom de l'élève est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom de l'élève est obligatoire")
    private String prenom;

    private String dateNaissance; // format yyyy-MM-dd, optionnel
    private List<Long> coursIds;

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getDateNaissance() { return dateNaissance; }
    public List<Long> getCoursIds() { return coursIds; }
}
