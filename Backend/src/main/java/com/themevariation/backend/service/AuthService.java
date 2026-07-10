package com.themevariation.backend.service;

import com.themevariation.backend.dto.RegisterRequest;
import com.themevariation.backend.repository.CompteRepository;
import com.themevariation.backend.model.Compte;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CompteRepository compteRepository;

    public AuthService(CompteRepository compteRepository){
        this.compteRepository = compteRepository;
    }

    public void RegisterRequest(RegisterRequest request){
        Compte compte = new Compte();
        compte.setEmail(request.getEmail());
        compte.setMotDePasse(request.getMotDePasse());
        compte.setNom(request.getNom());
        compte.setPrenom(request.getPrénom());
        compte.setTelephone(request.getTéléphone());
        compte.setRole("ELEVE");
        compteRepository.save(compte);
    }
}
