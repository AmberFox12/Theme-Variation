package com.themevariation.backend.dto;

public class EleveRequest {
    private String nom;
    private String prenom;
    private String dateNaissance; // format yyyy-MM-dd, optionnel
    private String email;
    private String telephone;

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getDateNaissance() { return dateNaissance; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
}
