package com.themevariation.backend.repository;

import com.themevariation.backend.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByEmail(String email);
    List<Compte> findByRole(String role);
}
