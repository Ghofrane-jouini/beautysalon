package com.beautysalon.service;

import com.beautysalon.model.Client;
import com.beautysalon.repository.ClientRepository;
import com.beautysalon.repository.RendezVousRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final RendezVousRepository rendezVousRepository;

    public ClientService(ClientRepository clientRepository, RendezVousRepository rendezVousRepository) {
        this.clientRepository = clientRepository;
        this.rendezVousRepository = rendezVousRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Client introuvable"));
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void delete(Long id) {
        if (rendezVousRepository.existsByClientId(id)) {
            throw new BusinessException("Impossible de supprimer ce client : il a des rendez-vous.");
        }
        clientRepository.deleteById(id);
    }
}
