package com.themevariation.backend.repository;

import com.themevariation.backend.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestAttribute;
import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByEmail(String email);
}
