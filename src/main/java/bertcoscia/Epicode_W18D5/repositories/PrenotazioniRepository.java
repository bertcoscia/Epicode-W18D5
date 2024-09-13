package bertcoscia.Epicode_W18D5.repositories;

import bertcoscia.Epicode_W18D5.entities.Dipendente;
import bertcoscia.Epicode_W18D5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {

    boolean existsByDipendente(Dipendente dipendente);

}
