package com.themevariation.backend.service;

import com.themevariation.backend.dto.HistoriqueRequest;
import com.themevariation.backend.exception.ResourceNotFoundException;
import com.themevariation.backend.model.Historique;
import com.themevariation.backend.repository.HistoriqueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    // Support de la pagination, prêt côté API : le frontend peut continuer à tout
    // demander d'un coup via getAll(), ou passer à ce mode le jour où le volume
    // d'historique cumulé le justifiera (archives qui grossissent chaque année).
    public Page<Historique> getPage(int page, int size) {
        return historiqueRepository.findAll(PageRequest.of(page, size, Sort.by("ordre").ascending()));
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
                .orElseThrow(() -> new ResourceNotFoundException("Entrée introuvable"));
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
