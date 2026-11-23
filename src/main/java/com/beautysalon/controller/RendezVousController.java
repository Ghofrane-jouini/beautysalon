package com.beautysalon.controller;

import com.beautysalon.model.RendezVous;
import com.beautysalon.model.RendezVousForm;
import com.beautysalon.service.BusinessException;
import com.beautysalon.service.ClientService;
import com.beautysalon.service.RendezVousService;
import com.beautysalon.service.ServiceSalonService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/rendezvous")
public class RendezVousController {

    private final RendezVousService rendezVousService;
    private final ClientService clientService;
    private final ServiceSalonService serviceSalonService;

    public RendezVousController(RendezVousService rendezVousService,
                                ClientService clientService,
                                ServiceSalonService serviceSalonService) {
        this.rendezVousService = rendezVousService;
        this.clientService = clientService;
        this.serviceSalonService = serviceSalonService;
    }

    /** Liste, avec recherche optionnelle par nom ou prénom du client (?q=...). */
    @GetMapping
    public String liste(@RequestParam(required = false) String q, Model model) {
        List<RendezVous> liste = rendezVousService.findAll();
        if (q != null && !q.isBlank()) {
            String recherche = q.trim().toLowerCase();
            liste = liste.stream()
                    .filter(r -> r.getClient().getNomComplet().toLowerCase().contains(recherche))
                    .toList();
        }
        model.addAttribute("rendezVousList", liste);
        model.addAttribute("q", q);
        return "rendezvous/list";
    }

    @GetMapping("/new")
    public String nouveau(Model model) {
        model.addAttribute("form", new RendezVousForm());
        return preparerFormulaire(model, "/rendezvous");
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("form") RendezVousForm form, BindingResult result,
                        Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return preparerFormulaire(model, "/rendezvous");
        }
        try {
            rendezVousService.create(form);
        } catch (BusinessException e) {
            model.addAttribute("erreur", e.getMessage());
            return preparerFormulaire(model, "/rendezvous");
        }
        redirect.addFlashAttribute("succes", "Rendez-vous ajouté.");
        return "redirect:/rendezvous";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("rdv", rendezVousService.findById(id));
        return "rendezvous/detail";
    }

    @GetMapping("/{id}/edit")
    public String modifier(@PathVariable Long id, Model model) {
        model.addAttribute("form", rendezVousService.toForm(id));
        return preparerFormulaire(model, "/rendezvous/" + id);
    }

    @PostMapping("/{id}")
    public String mettreAJour(@PathVariable Long id, @Valid @ModelAttribute("form") RendezVousForm form,
                              BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return preparerFormulaire(model, "/rendezvous/" + id);
        }
        try {
            rendezVousService.update(id, form);
        } catch (BusinessException e) {
            model.addAttribute("erreur", e.getMessage());
            return preparerFormulaire(model, "/rendezvous/" + id);
        }
        redirect.addFlashAttribute("succes", "Rendez-vous modifié.");
        return "redirect:/rendezvous";
    }

    @PostMapping("/{id}/confirmer")
    public String confirmer(@PathVariable Long id, RedirectAttributes redirect) {
        rendezVousService.confirmer(id);
        redirect.addFlashAttribute("succes", "Rendez-vous confirmé.");
        return "redirect:/rendezvous";
    }

    @PostMapping("/{id}/annuler")
    public String annuler(@PathVariable Long id, RedirectAttributes redirect) {
        rendezVousService.annuler(id);
        redirect.addFlashAttribute("succes", "Rendez-vous annulé.");
        return "redirect:/rendezvous";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirect) {
        rendezVousService.delete(id);
        redirect.addFlashAttribute("succes", "Rendez-vous supprimé.");
        return "redirect:/rendezvous";
    }

    private String preparerFormulaire(Model model, String action) {
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("services", serviceSalonService.findAll());
        model.addAttribute("action", action);
        return "rendezvous/form";
    }
}
