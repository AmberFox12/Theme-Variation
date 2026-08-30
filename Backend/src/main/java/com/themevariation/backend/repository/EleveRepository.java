package com.themevariation.backend.repository;

import com.themevariation.backend.model.Compte;
import com.themevariation.backend.model.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EleveRepository extends JpaRepository<Eleve, Long> {
    List<Eleve> findByCompte(Compte compte);
}
