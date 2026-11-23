package com.beautysalon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class ServiceSalon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Size(max = 255, message = "255 caractères maximum")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @PositiveOrZero(message = "Le prix doit être positif")
    private Double prix;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être d'au moins 1 minute")
    private Integer dureeMinutes;

    public ServiceSalon() {
    }

    public ServiceSalon(String nom, String description, Double prix, Integer dureeMinutes) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.dureeMinutes = dureeMinutes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    public Integer getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(Integer dureeMinutes) { this.dureeMinutes = dureeMinutes; }
}
