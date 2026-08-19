package com.themevariation.backend.dto;

public class CoursRequest {
    private Long typeDanseId;
    private String nom;
    private String jour;
    private String heureDebut;
    private int dureeMinutes;
    private int placesMax;
    private int placesDisponibles;
    private String statut;

    public Long getTypeDanseId() { return typeDanseId; }
    public String getNom() { return nom; }
    public String getJour() { return jour; }
    public String getHeureDebut() { return heureDebut; }
    public int getDureeMinutes() { return dureeMinutes; }
    public int getPlacesMax() { return placesMax; }
    public int getPlacesDisponibles() { return placesDisponibles; }
    public String getStatut() { return statut; }
}
