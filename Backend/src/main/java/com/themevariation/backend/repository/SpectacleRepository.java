package com.themevariation.backend.repository;

import com.themevariation.backend.model.Spectacle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpectacleRepository extends JpaRepository<Spectacle, Long> {
    List<Spectacle> findAllByOrderByAnneeDesc();
}
