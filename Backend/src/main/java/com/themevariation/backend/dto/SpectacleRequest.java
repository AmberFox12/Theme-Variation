package com.themevariation.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class SpectacleRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "L'année est obligatoire")
    private String annee;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    private String description;
    private String statut;

    public String getTitre() { return titre; }
    public String getAnnee() { return annee; }
    public String getLieu() { return lieu; }
    public String getDescription() { return description; }
    public String getStatut() { return statut; }
}
