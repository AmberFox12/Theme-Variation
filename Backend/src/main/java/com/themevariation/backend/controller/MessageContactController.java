package com.themevariation.backend.controller;

import com.themevariation.backend.dto.MessageContactRequest;
import com.themevariation.backend.model.MessageContact;
import com.themevariation.backend.service.MessageContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class MessageContactController {

    private final MessageContactService service;

    public MessageContactController(MessageContactService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> envoyer(@Valid @RequestBody MessageContactRequest request) {
        service.envoyer(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<MessageContact>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/lu")
    public ResponseEntity<Void> marquerLu(@PathVariable Long id) {
        service.marquerLu(id);
        return ResponseEntity.ok().build();
    }
}
