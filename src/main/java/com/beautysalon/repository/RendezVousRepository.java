package com.beautysalon.repository;

import com.beautysalon.model.RendezVous;
import com.beautysalon.model.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    List<RendezVous> findAllByOrderByDateAscHeureAsc();

    List<RendezVous> findByDate(LocalDate date);

    long countByStatut(StatutRendezVous statut);

    boolean existsByClientId(Long clientId);

    boolean existsByServiceId(Long serviceId);
}
