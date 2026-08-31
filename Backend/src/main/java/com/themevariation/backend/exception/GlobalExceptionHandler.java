package com.themevariation.backend.exception;

import com.themevariation.backend.security.LoginAttemptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("erreur", e.getMessage()));
    }

    @ExceptionHandler(LoginAttemptService.TropDeTentativesException.class)
    public ResponseEntity<Map<String, String>> handleTropDeTentatives(LoginAttemptService.TropDeTentativesException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("erreur", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> champs = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err ->
                champs.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of(
                "erreur", "Données invalides",
                "champs", champs
        ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpected(Exception e) {
        return ResponseEntity.internalServerError().body(Map.of("erreur", "Une erreur inattendue est survenue."));
    }
}
