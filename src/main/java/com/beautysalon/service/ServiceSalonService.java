package com.beautysalon.service;

import com.beautysalon.model.ServiceSalon;
import com.beautysalon.repository.RendezVousRepository;
import com.beautysalon.repository.ServiceSalonRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceSalonService {

    private final ServiceSalonRepository serviceRepository;
    private final RendezVousRepository rendezVousRepository;

    public ServiceSalonService(ServiceSalonRepository serviceRepository, RendezVousRepository rendezVousRepository) {
        this.serviceRepository = serviceRepository;
        this.rendezVousRepository = rendezVousRepository;
    }

    public List<ServiceSalon> findAll() {
        return serviceRepository.findAll();
    }

    public ServiceSalon findById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Service introuvable"));
    }

    public ServiceSalon save(ServiceSalon service) {
        return serviceRepository.save(service);
    }

    public void delete(Long id) {
        if (rendezVousRepository.existsByServiceId(id)) {
            throw new BusinessException("Impossible de supprimer ce service : il est utilisé par des rendez-vous.");
        }
        serviceRepository.deleteById(id);
    }
}
