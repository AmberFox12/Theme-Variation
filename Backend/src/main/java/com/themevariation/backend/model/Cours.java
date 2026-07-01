package com.themevariation.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cours")
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_danse_id", nullable = false)
    private TypeDanse typeDanse;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String jour;

    private LocalTime heureDebut;

    private int dureeMinutes;

    private int placesMax;

    private int placesDisponibles;

    @Column(nullable = false)
    private String statut; // ex : "OUVERT", "COMPLET", "ANNULE"
}
