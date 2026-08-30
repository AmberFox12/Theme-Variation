package com.themevariation.backend.dto;

public class HistoriqueRequest {
    private String annee;
    private String titre;
    private String description;
    private int ordre;

    public String getAnnee() { return annee; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public int getOrdre() { return ordre; }
}
