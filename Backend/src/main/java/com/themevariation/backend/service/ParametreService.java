package com.themevariation.backend.service;

import com.themevariation.backend.model.Parametre;
import com.themevariation.backend.repository.ParametreRepository;
import org.springframework.stereotype.Service;

@Service
public class ParametreService {

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
        p.setCarteEmbedUrl(request.getCarteEmbedUrl());
        return parametreRepository.save(p);
    }
}
