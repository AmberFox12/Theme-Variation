package com.themevariation.backend.repository;

import com.themevariation.backend.model.Eleve;
import com.themevariation.backend.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByEleve(Eleve eleve);
    void deleteByEleve(Eleve eleve);
}
