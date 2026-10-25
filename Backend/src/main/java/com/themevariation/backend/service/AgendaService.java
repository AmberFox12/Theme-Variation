package com.themevariation.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.themevariation.backend.dto.EvenementDto;
import com.themevariation.backend.exception.ResourceNotFoundException;
import com.themevariation.backend.model.Evenement;
import com.themevariation.backend.repository.EvenementRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    private static final DateTimeFormatter FMT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter FMT_DATE     = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public Optional<Evenement> getEvenementEnCours() {
        List<Evenement> resultats = evenementRepository.findEvenementEnCours(LocalDateTime.now());
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

    public Evenement uploadImage(Long id, MultipartFile file) {
        Evenement e = evenementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement introuvable"));
        try {
            byte[] contenu = file.getBytes();
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(contenu));
            if (image == null) {
                throw new IllegalArgumentException("Le fichier envoyé n'est pas une image valide.");
            }
            String ext = switch (file.getContentType() != null ? file.getContentType() : "") {
                case "image/png" -> ".png";
                case "image/webp" -> ".webp";
                default -> ".jpg";
            };
            Path uploadDir = Paths.get("uploads/evenements");
            Files.createDirectories(uploadDir);
            String filename = id + "_" + System.currentTimeMillis() + ext;
            Files.write(uploadDir.resolve(filename), contenu,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            e.setImageUrl("/uploads/evenements/" + filename);
            return evenementRepository.save(e);
        } catch (IOException ex) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", ex);
        }
    }

    private void remplir(Evenement evenement, EvenementDto dto) {
        evenement.setTitre(dto.getTitre());
        evenement.setDescription(dto.getDescription());
        evenement.setLieu(dto.getLieu());
        evenement.setDateHeure(parseDateTime(dto.getDateHeure(), false));
        if (dto.getDateFin() != null && !dto.getDateFin().isBlank()) {
            evenement.setDateFin(parseDateTime(dto.getDateFin(), true));
        } else {
            evenement.setDateFin(null);
        }
    }

    private LocalDateTime parseDateTime(String value, boolean finDeJournee) {
        try {
            return LocalDateTime.parse(value, FMT_DATETIME);
        } catch (DateTimeParseException e1) {
            try {
                // Date seule "yyyy-MM-dd" : début = 00:00, fin = 23:59
                var date = java.time.LocalDate.parse(value, FMT_DATE);
                return finDeJournee
                        ? date.atTime(23, 59, 59)
                        : date.atStartOfDay();
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Format de date invalide : " + value);
            }
        }
    }
}
