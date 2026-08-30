package com.themevariation.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "parametres")
public class Parametre {

    @Id
    private Long id = 1L;

    private String email;
    private String telephone;

    @Column(columnDefinition = "TEXT")
    private String adresse;

    private String instagram;
    private String facebook;

    private String emailNotification;

    @Column(columnDefinition = "TEXT")
    private String carteEmbedUrl;
}
