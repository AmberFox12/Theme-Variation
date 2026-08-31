package com.themevariation.backend.service;

import com.themevariation.backend.model.Parametre;
import com.themevariation.backend.repository.ParametreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParametreService {

    private static final Pattern SRC_IFRAME = Pattern.compile("src=\"([^\"]+)\"");
    private static final List<String> HOTES_CARTE_AUTORISES = List.of(
            "https://www.google.com/maps",
            "https://maps.google.com",
            "https://www.google.fr/maps"
    );

    private final ParametreRepository parametreRepository;

    public ParametreService(ParametreRepository parametreRepository) {
        this.parametreRepository = parametreRepository;
    }

    public Parametre get() {
        return parametreRepository.findById(1L).orElseGet(() -> {
            Parametre p = new Parametre();
            p.setEmail("association.themeetvariations@gmail.com");
            p.setTelephone("07.65.23.31.96");
            p.setAdresse("338, route de Francheville\n27130 Verneuil d'Avre et d'Iton");
            p.setInstagram("https://www.instagram.com/associationthemeetvariations");
            p.setFacebook("https://www.facebook.com/share/1EXKRf9bcT/");
            return parametreRepository.save(p);
        });
    }

    public Parametre update(Parametre request) {
        Parametre p = get();
        p.setEmail(request.getEmail());
        p.setTelephone(request.getTelephone());
        p.setAdresse(request.getAdresse());
        p.setInstagram(request.getInstagram());
        p.setFacebook(request.getFacebook());
        p.setEmailNotification(request.getEmailNotification());
        p.setCarteEmbedUrl(validerCarteEmbedUrl(request.getCarteEmbedUrl()));
        return parametreRepository.save(p);
    }

    /**
     * N'autorise que des cartes Google Maps (URL directe ou code &lt;iframe&gt;) :
     * ce champ est ensuite rendu de confiance côté Angular (bypassSecurityTrustResourceUrl),
     * donc une valeur non contrôlée ici deviendrait une faille XSS pour tous les visiteurs.
     */
    private String validerCarteEmbedUrl(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return valeur;
        }
        String url = valeur.trim();
        if (url.startsWith("<iframe")) {
            Matcher m = SRC_IFRAME.matcher(url);
            if (!m.find()) {
                throw new IllegalArgumentException("Code d'intégration invalide : attribut src introuvable.");
            }
            url = m.group(1);
        }
        boolean autorise = HOTES_CARTE_AUTORISES.stream().anyMatch(url::startsWith);
        if (!autorise) {
            throw new IllegalArgumentException("Seules les URL Google Maps sont autorisées pour la carte.");
        }
        return valeur;
    }
}
