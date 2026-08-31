package com.themevariation.backend.service;

import org.springframework.stereotype.Service;
import com.themevariation.backend.dto.EvenementDto;
import com.themevariation.backend.exception.ResourceNotFoundException;
import com.themevariation.backend.model.Evenement;
import com.themevariation.backend.repository.EvenementRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final EvenementRepository evenementRepository;

    public AgendaService(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    public List<Evenement> getEvenements() {
        return evenementRepository.findAll();
    }

    public Optional<Evenement> getProchainEvenement() {
        List<Evenement> resultats = evenementRepository.findProchainEvenement(LocalDateTime.now());
        return resultats.isEmpty() ? Optional.empty() : Optional.of(resultats.get(0));
    }

    public Evenement creer(EvenementDto dto) {
        Evenement evenement = new Evenement();
        remplir(evenement, dto);
        return evenementRepository.save(evenement);
    }

    public Evenement modifier(Long id, EvenementDto dto) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement introuvable"));
        remplir(evenement, dto);
        return evenementRepository.save(evenement);
    }

    public void supprimer(Long id) {
        evenementRepository.deleteById(id);
    }

    private void remplir(Evenement evenement, EvenementDto dto) {
        evenement.setTitre(dto.getTitre());
        evenement.setDescription(dto.getDescription());
        evenement.setLieu(dto.getLieu());
        evenement.setDateHeure(LocalDateTime.parse(dto.getDateHeure(), FORMATTER));
    }
}
