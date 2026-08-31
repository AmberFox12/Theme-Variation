package com.themevariation.backend.repository;

import com.themevariation.backend.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoursRepository extends JpaRepository<Cours, Long> {

    // typeDanse est en FetchType.LAZY : on le charge ici en une seule requête (JOIN FETCH)
    // plutôt que de laisser Hibernate faire une requête par cours.
    @Query("SELECT c FROM Cours c JOIN FETCH c.typeDanse")
    List<Cours> findAllWithTypeDanse();
}
