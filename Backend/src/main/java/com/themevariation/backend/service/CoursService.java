package com.themevariation.backend.service;

import com.themevariation.backend.model.Cours;
import com.themevariation.backend.repository.CoursRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoursService {

    private final CoursRepository coursRepository;

    public CoursService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    public List<Cours> getCours() {
        return coursRepository.findAll();
    }
}
