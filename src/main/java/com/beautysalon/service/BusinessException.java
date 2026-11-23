package com.beautysalon.service;

/** Erreur métier : message lisible affiché à l'administrateur. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
