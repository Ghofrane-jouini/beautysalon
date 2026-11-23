package com.beautysalon.controller;

import com.beautysalon.model.ServiceSalon;
import com.beautysalon.service.BusinessException;
import com.beautysalon.service.ServiceSalonService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/services")
public class ServiceSalonController {

    private final ServiceSalonService serviceSalonService;

    public ServiceSalonController(ServiceSalonService serviceSalonService) {
        this.serviceSalonService = serviceSalonService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("services", serviceSalonService.findAll());
        return "services/list";
    }

    @GetMapping("/new")
    public String nouveau(Model model) {
        model.addAttribute("service", new ServiceSalon());
        model.addAttribute("action", "/services");
        return "services/form";
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("service") ServiceSalon service, BindingResult result,
                        Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/services");
            return "services/form";
        }
        serviceSalonService.save(service);
        redirect.addFlashAttribute("succes", "Service ajouté.");
        return "redirect:/services";
    }

    @GetMapping("/{id}/edit")
    public String modifier(@PathVariable Long id, Model model) {
        model.addAttribute("service", serviceSalonService.findById(id));
        model.addAttribute("action", "/services/" + id);
        return "services/form";
    }

    @PostMapping("/{id}")
    public String mettreAJour(@PathVariable Long id, @Valid @ModelAttribute("service") ServiceSalon service,
                              BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/services/" + id);
            return "services/form";
        }
        service.setId(id);
        serviceSalonService.save(service);
        redirect.addFlashAttribute("succes", "Service modifié.");
        return "redirect:/services";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            serviceSalonService.delete(id);
            redirect.addFlashAttribute("succes", "Service supprimé.");
        } catch (BusinessException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/services";
    }
}
