package com.themevariation.backend.repository;

import com.themevariation.backend.model.Eleve;
import com.themevariation.backend.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByEleve(Eleve eleve);
    void deleteByEleve(Eleve eleve);

    // eleve et cours sont en FetchType.LAZY : on les charge ici en une seule requête
    // (JOIN FETCH) plutôt que de laisser Hibernate faire une requête par ligne et par relation.
    @Query("SELECT i FROM Inscription i JOIN FETCH i.eleve JOIN FETCH i.cours")
    List<Inscription> findAllWithEleveEtCours();
}
