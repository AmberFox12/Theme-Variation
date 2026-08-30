package com.themevariation.backend.controller;

import com.themevariation.backend.dto.MessageContactRequest;
import com.themevariation.backend.model.MessageContact;
import com.themevariation.backend.service.MessageContactService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> envoyer(@RequestBody MessageContactRequest request) {
        service.envoyer(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MessageContact>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PatchMapping("/{id}/lu")
    public ResponseEntity<Void> marquerLu(@PathVariable Long id) {
        service.marquerLu(id);
        return ResponseEntity.ok().build();
    }
}
