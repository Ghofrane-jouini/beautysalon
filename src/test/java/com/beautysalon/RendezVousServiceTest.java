package com.beautysalon;

import com.beautysalon.model.*;
import com.beautysalon.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RendezVousServiceTest {

    @Autowired private ClientService clientService;
    @Autowired private ServiceSalonService serviceSalonService;
    @Autowired private RendezVousService rendezVousService;

    private Client client;
    private ServiceSalon coupe; // dure 60 minutes

    @BeforeEach
    void preparer() {
        client = clientService.save(new Client("Trabelsi", "Amel", "22111222", "amel@mail.com"));
        coupe = serviceSalonService.save(new ServiceSalon("Coupe", "Coupe de cheveux", 25.0, 60));
    }

    private RendezVousForm form(LocalDate date, LocalTime heure) {
        RendezVousForm f = new RendezVousForm();
        f.setClientId(client.getId());
        f.setServiceId(coupe.getId());
        f.setDate(date);
        f.setHeure(heure);
        return f;
    }

    @Test
    void creerUnRendezVousFutur_statutEnAttente() {
        RendezVous rdv = rendezVousService.create(form(LocalDate.now().plusDays(3), LocalTime.of(10, 0)));

        assertThat(rdv.getId()).isNotNull();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.EN_ATTENTE);
    }

    @Test
    void refuserUnRendezVousDansLePasse() {
        RendezVousForm passe = form(LocalDate.now().minusDays(1), LocalTime.of(10, 0));

        assertThatThrownBy(() -> rendezVousService.create(passe))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("passé");
    }

    @Test
    void refuserUnCreneauQuiSeChevauche() {
        LocalDate jour = LocalDate.now().plusDays(3);
        rendezVousService.create(form(jour, LocalTime.of(10, 0)));   // occupe 10h-11h

        RendezVousForm chevauche = form(jour, LocalTime.of(10, 30)); // 10h30 : conflit
        assertThatThrownBy(() -> rendezVousService.create(chevauche))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("occupé");

        // 11h00 pile : accepté
        assertThat(rendezVousService.create(form(jour, LocalTime.of(11, 0))).getId()).isNotNull();
    }

    @Test
    void annulerLiberLeCreneau() {
        LocalDate jour = LocalDate.now().plusDays(4);
        RendezVous premier = rendezVousService.create(form(jour, LocalTime.of(14, 0)));
        rendezVousService.annuler(premier.getId());

        assertThat(rendezVousService.create(form(jour, LocalTime.of(14, 0))).getId()).isNotNull();
    }
}
