package com.beautysalon.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Client client;

    @ManyToOne(optional = false)
    private ServiceSalon service;

    // "date" est un mot réservé en SQL : on nomme les colonnes autrement
    @Column(name = "date_rdv", nullable = false)
    private LocalDate date;

    @Column(name = "heure_rdv", nullable = false)
    private LocalTime heure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRendezVous statut = StatutRendezVous.EN_ATTENTE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public ServiceSalon getService() { return service; }
    public void setService(ServiceSalon service) { this.service = service; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }
    public StatutRendezVous getStatut() { return statut; }
    public void setStatut(StatutRendezVous statut) { this.statut = statut; }
}
