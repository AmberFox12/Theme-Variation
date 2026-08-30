package com.themevariation.backend.service;

import com.themevariation.backend.dto.HistoriqueRequest;
import com.themevariation.backend.model.Historique;
import com.themevariation.backend.repository.HistoriqueRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistoriqueService {

    private final HistoriqueRepository historiqueRepository;

    public HistoriqueService(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    public List<Historique> getAll() {
        return historiqueRepository.findAllByOrderByOrdreAsc();
    }

    public Historique creer(HistoriqueRequest request) {
        Historique h = new Historique();
        h.setAnnee(request.getAnnee());
        h.setTitre(request.getTitre());
        h.setDescription(request.getDescription());
        h.setOrdre(request.getOrdre());
        return historiqueRepository.save(h);
    }

    public Historique modifier(Long id, HistoriqueRequest request) {
        Historique h = historiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrée introuvable"));
        h.setAnnee(request.getAnnee());
        h.setTitre(request.getTitre());
        h.setDescription(request.getDescription());
        h.setOrdre(request.getOrdre());
        return historiqueRepository.save(h);
    }

    public void supprimer(Long id) {
        historiqueRepository.deleteById(id);
    }
}
