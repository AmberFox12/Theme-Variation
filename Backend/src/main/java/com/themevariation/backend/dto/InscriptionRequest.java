package com.themevariation.backend.dto;

import jakarta.validation.constraints.NotNull;

public class InscriptionRequest {
    @NotNull(message = "L'élève est obligatoire")
    private Long eleveId;

    @NotNull(message = "Le cours est obligatoire")
    private Long coursId;

    private String statut;

    public Long getEleveId() { return eleveId; }
    public Long getCoursId() { return coursId; }
    public String getStatut() { return statut; }
}
