package com.themevariation.backend.dto;

import java.util.List;

public class ElevePubliqueDto {
    private String nom;
    private String prenom;
    private String dateNaissance; // format yyyy-MM-dd, optionnel
    private List<Long> coursIds;

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getDateNaissance() { return dateNaissance; }
    public List<Long> getCoursIds() { return coursIds; }
}
