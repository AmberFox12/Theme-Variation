package com.themevariation.backend.security;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Protection anti brute-force en mémoire : bloque un email après plusieurs échecs de connexion.
 * Suffisant pour un seul serveur ; réinitialisé si l'application redémarre.
 */
@Component
public class LoginAttemptService {

    private static final int MAX_TENTATIVES = 5;
    private static final Duration BLOCAGE = Duration.ofMinutes(15);

    private record Suivi(AtomicInteger echecs, Instant premierEchec) {}

    private final ConcurrentHashMap<String, Suivi> tentatives = new ConcurrentHashMap<>();

    public void verifierNonBloque(String email) {
        Suivi suivi = tentatives.get(cle(email));
        if (suivi == null) {
            return;
        }
        boolean fenetreExpiree = Instant.now().isAfter(suivi.premierEchec().plus(BLOCAGE));
        if (fenetreExpiree) {
            tentatives.remove(cle(email));
            return;
        }
        if (suivi.echecs().get() >= MAX_TENTATIVES) {
            long minutesRestantes = Duration.between(Instant.now(), suivi.premierEchec().plus(BLOCAGE)).toMinutes() + 1;
            throw new TropDeTentativesException(
                    "Trop de tentatives échouées. Réessayez dans " + minutesRestantes + " minute(s).");
        }
    }

    public void enregistrerEchec(String email) {
        tentatives.compute(cle(email), (k, v) -> {
            if (v == null) {
                return new Suivi(new AtomicInteger(1), Instant.now());
            }
            v.echecs().incrementAndGet();
            return v;
        });
    }

    public void reinitialiser(String email) {
        tentatives.remove(cle(email));
    }

    private String cle(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    public static class TropDeTentativesException extends RuntimeException {
        public TropDeTentativesException(String message) {
            super(message);
        }
    }
}
