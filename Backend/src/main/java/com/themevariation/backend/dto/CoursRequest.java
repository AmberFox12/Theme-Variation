package com.themevariation.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class CoursRequest {
    @NotNull(message = "Le type de danse est obligatoire")
    private Long typeDanseId;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le jour est obligatoire")
    private String jour;

    @NotBlank(message = "L'heure de début est obligatoire")
    private String heureDebut;

    @Min(value = 1, message = "La durée doit être d'au moins 1 minute")
    private int dureeMinutes;

    @Min(value = 1, message = "Le nombre de places doit être d'au moins 1")
    private int placesMax;

    @PositiveOrZero(message = "Le nombre de places disponibles ne peut pas être négatif")
    private int placesDisponibles;

    @NotBlank(message = "Le statut est obligatoire")
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
