package com.themevariation.backend.repository;

import com.themevariation.backend.model.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoriqueRepository extends JpaRepository<Historique, Long> {
    List<Historique> findAllByOrderByOrdreAsc();
}
