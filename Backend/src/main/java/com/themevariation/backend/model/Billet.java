package com.themevariation.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "billets")
public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Les autres champs seront définis à l'étape suivante
}
