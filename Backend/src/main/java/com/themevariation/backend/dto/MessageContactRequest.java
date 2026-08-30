package com.themevariation.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageContactRequest {
    private String nom;
    private String email;
    private String telephone;
    private String message;
}
