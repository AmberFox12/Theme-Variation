package com.themevariation.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "evenements")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Les autres champs seront définis à l'étape suivante
}
