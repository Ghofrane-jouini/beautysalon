package com.beautysalon.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

/** Données saisies dans le formulaire de rendez-vous (on y met les ID choisis). */
public class RendezVousForm {

    @NotNull(message = "Choisissez un client")
    private Long clientId;

    @NotNull(message = "Choisissez un service")
    private Long serviceId;

    @NotNull(message = "La date est obligatoire")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull(message = "L'heure est obligatoire")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime heure;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }
}
