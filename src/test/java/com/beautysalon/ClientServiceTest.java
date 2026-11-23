package com.beautysalon;

import com.beautysalon.model.Client;
import com.beautysalon.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    void ajouterEtRetrouverUnClient() {
        Client saved = clientService.save(new Client("Ben Ali", "Sarra", "20123456", "sarra@mail.com"));

        Client trouve = clientService.findById(saved.getId());

        assertThat(trouve.getNom()).isEqualTo("Ben Ali");
        assertThat(clientService.findAll()).extracting(Client::getId).contains(saved.getId());
    }
}
