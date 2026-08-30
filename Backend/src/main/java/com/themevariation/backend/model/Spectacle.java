package com.themevariation.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "spectacles")
public class Spectacle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String annee;

    private String lieu;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String statut; // PASSE ou A_VENIR

    private String imageUrl;
}
