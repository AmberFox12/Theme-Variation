package com.themevariation.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class EvenementDto {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotBlank(message = "La date/heure est obligatoire")
    private String dateHeure;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getDateHeure() { return dateHeure; }
    public String getLieu() { return lieu; }
}
