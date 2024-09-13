package bertcoscia.Epicode_W18D5.repositories;

import bertcoscia.Epicode_W18D5.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ViaggiRepository extends JpaRepository<Viaggio, UUID> {

    boolean existsByDestinazioneAndData(String destinazione, LocalDate data);



}
