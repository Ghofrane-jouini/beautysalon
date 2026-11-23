package com.beautysalon.controller;

import com.beautysalon.model.Client;
import com.beautysalon.service.BusinessException;
import com.beautysalon.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients/list";
    }

    @GetMapping("/new")
    public String nouveau(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("action", "/clients");
        return "clients/form";
    }

    @PostMapping
    public String creer(@Valid @ModelAttribute("client") Client client, BindingResult result,
                        Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/clients");
            return "clients/form";
        }
        clientService.save(client);
        redirect.addFlashAttribute("succes", "Client ajouté.");
        return "redirect:/clients";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        return "clients/detail";
    }

    @GetMapping("/{id}/edit")
    public String modifier(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        model.addAttribute("action", "/clients/" + id);
        return "clients/form";
    }

    @PostMapping("/{id}")
    public String mettreAJour(@PathVariable Long id, @Valid @ModelAttribute("client") Client client,
                              BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/clients/" + id);
            return "clients/form";
        }
        client.setId(id);
        clientService.save(client);
        redirect.addFlashAttribute("succes", "Client modifié.");
        return "redirect:/clients";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            clientService.delete(id);
            redirect.addFlashAttribute("succes", "Client supprimé.");
        } catch (BusinessException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/clients";
    }
}
