package com.themevariation.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class HistoriqueRequest {
    @NotBlank(message = "L'année est obligatoire")
    private String annee;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;
    private int ordre;

    public String getAnnee() { return annee; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public int getOrdre() { return ordre; }
}
