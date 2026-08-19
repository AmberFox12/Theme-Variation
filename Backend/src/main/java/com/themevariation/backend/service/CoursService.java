package com.themevariation.backend.service;

import com.themevariation.backend.dto.CoursRequest;
import com.themevariation.backend.model.Cours;
import com.themevariation.backend.model.TypeDanse;
import com.themevariation.backend.repository.CoursRepository;
import com.themevariation.backend.repository.TypeDanseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class CoursService {

    private final CoursRepository coursRepository;
    private final TypeDanseRepository typeDanseRepository;

    public CoursService(CoursRepository coursRepository, TypeDanseRepository typeDanseRepository) {
        this.coursRepository = coursRepository;
        this.typeDanseRepository = typeDanseRepository;
    }

    public List<Cours> getCours() {
        return coursRepository.findAll();
    }

    public Cours creer(CoursRequest request) {
        Cours cours = new Cours();
        remplir(cours, request);
        return coursRepository.save(cours);
    }

    public Cours modifier(Long id, CoursRequest request) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));
        remplir(cours, request);
        return coursRepository.save(cours);
    }

    public void supprimer(Long id) {
        coursRepository.deleteById(id);
    }

    private void remplir(Cours cours, CoursRequest request) {
        TypeDanse typeDanse = typeDanseRepository.findById(request.getTypeDanseId())
                .orElseThrow(() -> new RuntimeException("Type de danse introuvable"));
        cours.setTypeDanse(typeDanse);
        cours.setNom(request.getNom());
        cours.setJour(request.getJour());
        cours.setHeureDebut(LocalTime.parse(request.getHeureDebut()));
        cours.setDureeMinutes(request.getDureeMinutes());
        cours.setPlacesMax(request.getPlacesMax());
        cours.setPlacesDisponibles(request.getPlacesDisponibles());
        cours.setStatut(request.getStatut());
    }
}
