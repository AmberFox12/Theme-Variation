package com.themevariation.backend.service;

import com.themevariation.backend.dto.ElevePubliqueDto;
import com.themevariation.backend.dto.InscriptionPubliqueRequest;
import com.themevariation.backend.dto.InscriptionRequest;
import com.themevariation.backend.model.Compte;
import com.themevariation.backend.model.Cours;
import com.themevariation.backend.model.Eleve;
import com.themevariation.backend.model.Inscription;
import com.themevariation.backend.repository.CompteRepository;
import com.themevariation.backend.repository.CoursRepository;
import com.themevariation.backend.repository.EleveRepository;
import com.themevariation.backend.repository.InscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EleveRepository eleveRepository;
    private final CoursRepository coursRepository;
    private final CompteRepository compteRepository;

    public InscriptionService(InscriptionRepository inscriptionRepository,
                              EleveRepository eleveRepository,
                              CoursRepository coursRepository,
                              CompteRepository compteRepository) {
        this.inscriptionRepository = inscriptionRepository;
        this.eleveRepository = eleveRepository;
        this.coursRepository = coursRepository;
        this.compteRepository = compteRepository;
    }

    public List<Inscription> getAll() {
        return inscriptionRepository.findAll();
    }

    public Inscription creer(InscriptionRequest request) {
        Eleve eleve = eleveRepository.findById(request.getEleveId())
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
        Cours cours = coursRepository.findById(request.getCoursId())
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));

        Inscription inscription = new Inscription();
        inscription.setEleve(eleve);
        inscription.setCours(cours);
        inscription.setStatut(request.getStatut() != null ? request.getStatut() : "EN_COURS");
        inscription.setDateInscription(LocalDateTime.now());
        return inscriptionRepository.save(inscription);
    }

    @Transactional
    public Map<String, Object> creerPublique(InscriptionPubliqueRequest request) {
        if (compteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé pour un autre compte.");
        }

        Compte compte = new Compte();
        compte.setEmail(request.getEmail());
        compte.setNom(request.getNom());
        compte.setPrenom(request.getPrenom());
        compte.setTelephone(request.getTelephone());
        compte.setAdresse(request.getAdresse());
        compte.setMotDePasse(UUID.randomUUID().toString());
        compte.setRole("ELEVE");
        compteRepository.save(compte);

        int nbInscriptions = 0;
        List<ElevePubliqueDto> elevesDto = request.getEleves();
        if (elevesDto != null) {
            for (ElevePubliqueDto dto : elevesDto) {
                Eleve eleve = new Eleve();
                eleve.setNom(dto.getNom());
                eleve.setPrenom(dto.getPrenom());
                eleve.setCompte(compte);
                if (dto.getDateNaissance() != null && !dto.getDateNaissance().isBlank()) {
                    eleve.setDateNaissance(LocalDate.parse(dto.getDateNaissance()));
                }
                eleveRepository.save(eleve);

                if (dto.getCoursIds() != null) {
                    for (Long coursId : dto.getCoursIds()) {
                        Cours cours = coursRepository.findById(coursId)
                                .orElseThrow(() -> new RuntimeException("Cours introuvable : " + coursId));
                        Inscription inscription = new Inscription();
                        inscription.setEleve(eleve);
                        inscription.setCours(cours);
                        inscription.setStatut("EN_COURS");
                        inscription.setDateInscription(LocalDateTime.now());
                        inscriptionRepository.save(inscription);
                        nbInscriptions++;
                    }
                }
            }
        }

        return Map.of(
                "message", "Inscription enregistrée avec succès",
                "nombreEleves", elevesDto != null ? elevesDto.size() : 0,
                "nombreInscriptions", nbInscriptions
        );
    }

    public Inscription updateStatut(Long id, String statut) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable"));
        inscription.setStatut(statut);
        return inscriptionRepository.save(inscription);
    }

    public void supprimer(Long id) {
        inscriptionRepository.deleteById(id);
    }
}
