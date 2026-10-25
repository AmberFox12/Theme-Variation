package com.themevariation.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class EvenementDto {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotBlank(message = "La date de début est obligatoire")
    private String dateHeure;

    private String dateFin;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getDateHeure() { return dateHeure; }
    public String getDateFin() { return dateFin; }
    public String getLieu() { return lieu; }
}
