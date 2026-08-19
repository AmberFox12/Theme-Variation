package com.themevariation.backend.repository;

import com.themevariation.backend.model.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    @Query("SELECT e FROM Evenement e WHERE e.dateHeure > :maintenant ORDER BY e.dateHeure ASC LIMIT 1")
    List<Evenement> findProchainEvenement(LocalDateTime maintenant);
}
