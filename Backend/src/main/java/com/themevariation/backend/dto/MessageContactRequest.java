package com.themevariation.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageContactRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    private String telephone;

    @NotBlank(message = "Le message est obligatoire")
    @Size(max = 5000, message = "Le message est trop long (5000 caractères maximum)")
    private String message;
}
