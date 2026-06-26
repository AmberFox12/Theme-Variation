package com.themevariation.backend.repository;

import com.themevariation.backend.model.Billet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BilletRepository extends JpaRepository<Billet, Long> {
}
