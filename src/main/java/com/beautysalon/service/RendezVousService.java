package com.beautysalon.service;

import com.beautysalon.model.*;
import com.beautysalon.repository.ClientRepository;
import com.beautysalon.repository.RendezVousRepository;
import com.beautysalon.repository.ServiceSalonRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final ClientRepository clientRepository;
    private final ServiceSalonRepository serviceRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository,
                             ClientRepository clientRepository,
                             ServiceSalonRepository serviceRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<RendezVous> findAll() {
        return rendezVousRepository.findAllByOrderByDateAscHeureAsc();
    }

    public RendezVous findById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Rendez-vous introuvable"));
    }

    public RendezVous create(RendezVousForm form) {
        return enregistrer(new RendezVous(), form);
    }

    public RendezVous update(Long id, RendezVousForm form) {
        return enregistrer(findById(id), form);
    }

    public RendezVousForm toForm(Long id) {
        RendezVous rdv = findById(id);
        RendezVousForm form = new RendezVousForm();
        form.setClientId(rdv.getClient().getId());
        form.setServiceId(rdv.getService().getId());
        form.setDate(rdv.getDate());
        form.setHeure(rdv.getHeure());
        return form;
    }

    public void confirmer(Long id) {
        RendezVous rdv = findById(id);
        rdv.setStatut(StatutRendezVous.CONFIRME);
        rendezVousRepository.save(rdv);
    }

    public void annuler(Long id) {
        RendezVous rdv = findById(id);
        rdv.setStatut(StatutRendezVous.ANNULE);
        rendezVousRepository.save(rdv);
    }

    public void delete(Long id) {
        rendezVousRepository.deleteById(id);
    }

    /** Applique les règles de gestion puis enregistre. */
    private RendezVous enregistrer(RendezVous rdv, RendezVousForm form) {
        // Règles 1 et 2 : client et service doivent exister
        Client client = clientRepository.findById(form.getClientId())
                .orElseThrow(() -> new BusinessException("Ce client n'existe pas."));
        ServiceSalon service = serviceRepository.findById(form.getServiceId())
                .orElseThrow(() -> new BusinessException("Ce service n'existe pas."));

        LocalDateTime debut = LocalDateTime.of(form.getDate(), form.getHeure());
        LocalDateTime fin = debut.plusMinutes(service.getDureeMinutes());

        // Règle 3 : pas de rendez-vous dans le passé
        if (debut.isBefore(LocalDateTime.now())) {
            throw new BusinessException("La date et l'heure ne peuvent pas être dans le passé.");
        }

        // Règle 4 : pas de chevauchement avec un autre rendez-vous non annulé
        for (RendezVous autre : rendezVousRepository.findByDate(form.getDate())) {
            if (autre.getId().equals(rdv.getId()) || autre.getStatut() == StatutRendezVous.ANNULE) {
                continue;
            }
            LocalDateTime autreDebut = LocalDateTime.of(autre.getDate(), autre.getHeure());
            LocalDateTime autreFin = autreDebut.plusMinutes(autre.getService().getDureeMinutes());
            if (debut.isBefore(autreFin) && autreDebut.isBefore(fin)) {
                throw new BusinessException("Ce créneau est déjà occupé par un autre rendez-vous.");
            }
        }

        rdv.setClient(client);
        rdv.setService(service);
        rdv.setDate(form.getDate());
        rdv.setHeure(form.getHeure());
        return rendezVousRepository.save(rdv);
    }
}
