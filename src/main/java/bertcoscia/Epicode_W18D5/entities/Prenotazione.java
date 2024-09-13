package bertcoscia.Epicode_W18D5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private UUID idPrenotazione;
    @Column(name = "data_prenotazione", nullable = false)
    private LocalDate dataPrenotazione;
    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "id_viaggio")
    private Viaggio viaggio;

    @ManyToOne
    @JoinColumn(name = "id_dipendente")
    private Dipendente dipendente;

    public Prenotazione(LocalDate dataPrenotazione, String note, Viaggio viaggio, Dipendente dipendente) {
        this.dataPrenotazione = dataPrenotazione;
        this.note = note;
        this.viaggio = viaggio;
        this.dipendente = dipendente;
    }
}
