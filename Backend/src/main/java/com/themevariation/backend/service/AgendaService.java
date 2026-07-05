package com.themevariation.backend.service;

import org.springframework.stereotype.Service;
import com.themevariation.backend.model.Evenement;
import com.themevariation.backend.repository.EvenementRepository;
import java.util.List;

@Service
public class AgendaService {
    private final EvenementRepository evenementRepository;

    public AgendaService(EvenementRepository evenementRepository){
        this.evenementRepository = evenementRepository;
    }

    public List<Evenement> getEvenements(){
        return evenementRepository.findAll();
    }
}
