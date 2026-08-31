package com.themevariation.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cours")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_danse_id", nullable = false)
    private TypeDanse typeDanse;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String jour;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime heureDebut;

    private int dureeMinutes;

    private int placesMax;

    private int placesDisponibles;

    @Column(nullable = false)
    private String statut; // ex : "OUVERT", "COMPLET", "ANNULE"
}
