package bertcoscia.Epicode_W18D5.entities;

import bertcoscia.Epicode_W18D5.enums.ViaggioStato;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "viaggi")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Viaggio {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idViaggio;
    private String destinazione;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private ViaggioStato stato;
}
