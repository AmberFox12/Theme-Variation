package com.themevariation.backend.service;

import com.themevariation.backend.dto.MessageContactRequest;
import com.themevariation.backend.model.MessageContact;
import com.themevariation.backend.model.Parametre;
import com.themevariation.backend.repository.MessageContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageContactService {

    private final MessageContactRepository repo;
    private final ParametreService parametreService;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    public MessageContactService(MessageContactRepository repo,
                                 ParametreService parametreService) {
        this.repo = repo;
        this.parametreService = parametreService;
    }

    public MessageContact envoyer(MessageContactRequest request) {
        MessageContact msg = new MessageContact();
        msg.setNom(request.getNom());
        msg.setEmail(request.getEmail());
        msg.setTelephone(request.getTelephone());
        msg.setMessage(request.getMessage());
        MessageContact saved = repo.save(msg);

        envoyerEmail(saved);

        return saved;
    }

    private void envoyerEmail(MessageContact msg) {
        try {
            if (mailSender == null) return;
            Parametre p = parametreService.get();
            String dest = p.getEmailNotification();
            if (dest == null || dest.isBlank()) return;

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(mailFrom);
            mail.setTo(dest);
            mail.setSubject("Nouveau message de " + msg.getNom() + " — Thème et Variations");
            mail.setText(
                "De : " + msg.getNom() + " <" + msg.getEmail() + ">\n" +
                (msg.getTelephone() != null ? "Tél : " + msg.getTelephone() + "\n" : "") +
                "\n" + msg.getMessage()
            );
            mailSender.send(mail);
        } catch (Exception e) {
            System.err.println("Envoi email échoué (message sauvegardé en BDD) : " + e.getMessage());
        }
    }

    public List<MessageContact> getAll() {
        return repo.findAllByOrderByEnvoyeLeDesc();
    }

    public void marquerLu(Long id) {
        repo.findById(id).ifPresent(m -> {
            m.setLu(true);
            repo.save(m);
        });
    }
}
