package com.themevariation.backend.service;

import com.themevariation.backend.dto.SpectacleRequest;
import com.themevariation.backend.exception.ResourceNotFoundException;
import com.themevariation.backend.model.Spectacle;
import com.themevariation.backend.repository.SpectacleRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class SpectacleService {

    private final SpectacleRepository spectacleRepository;

    public SpectacleService(SpectacleRepository spectacleRepository) {
        this.spectacleRepository = spectacleRepository;
    }

    public List<Spectacle> getAll() {
        return spectacleRepository.findAllByOrderByAnneeDesc();
    }

    public Spectacle creer(SpectacleRequest request) {
        Spectacle s = new Spectacle();
        s.setTitre(request.getTitre());
        s.setAnnee(request.getAnnee());
        s.setLieu(request.getLieu());
        s.setDescription(request.getDescription());
        s.setStatut(request.getStatut() != null ? request.getStatut() : "A_VENIR");
        return spectacleRepository.save(s);
    }

    public Spectacle modifier(Long id, SpectacleRequest request) {
        Spectacle s = spectacleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spectacle introuvable"));
        s.setTitre(request.getTitre());
        s.setAnnee(request.getAnnee());
        s.setLieu(request.getLieu());
        s.setDescription(request.getDescription());
        s.setStatut(request.getStatut());
        return spectacleRepository.save(s);
    }

    public void supprimer(Long id) {
        spectacleRepository.deleteById(id);
    }

    public Spectacle uploadImage(Long id, MultipartFile file) {
        Spectacle s = spectacleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spectacle introuvable"));
        try {
            byte[] contenu = file.getBytes();

            // On vérifie que le fichier est réellement une image décodable,
            // pas seulement que son nom se termine par une extension d'image.
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(contenu));
            if (image == null) {
                throw new IllegalArgumentException("Le fichier envoyé n'est pas une image valide (jpg, png ou webp).");
            }

            String ext = switch (file.getContentType() != null ? file.getContentType() : "") {
                case "image/png" -> ".png";
                case "image/webp" -> ".webp";
                default -> ".jpg";
            };

            Path uploadDir = Paths.get("uploads/spectacles");
            Files.createDirectories(uploadDir);
            String filename = id + "_" + System.currentTimeMillis() + ext;
            Files.write(uploadDir.resolve(filename), contenu,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            s.setImageUrl("/uploads/spectacles/" + filename);
            return spectacleRepository.save(s);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }
}
