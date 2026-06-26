package com.themevariation.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Définira quelles routes sont publiques et lesquelles nécessitent une connexion
}
