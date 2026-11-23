package com.beautysalon.controller;

import com.beautysalon.model.StatutRendezVous;
import com.beautysalon.repository.ClientRepository;
import com.beautysalon.repository.RendezVousRepository;
import com.beautysalon.repository.ServiceSalonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ClientRepository clientRepository;
    private final ServiceSalonRepository serviceRepository;
    private final RendezVousRepository rendezVousRepository;

    public HomeController(ClientRepository clientRepository,
                          ServiceSalonRepository serviceRepository,
                          RendezVousRepository rendezVousRepository) {
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
        this.rendezVousRepository = rendezVousRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("nbClients", clientRepository.count());
        model.addAttribute("nbServices", serviceRepository.count());
        model.addAttribute("nbRendezVous", rendezVousRepository.count());
        model.addAttribute("nbEnAttente", rendezVousRepository.countByStatut(StatutRendezVous.EN_ATTENTE));
        return "index";
    }
}
