package bertcoscia.Epicode_W18D5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Dipendente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idDipendente;
    private String username;
    private String nome;
    private String cognome;
    private String email;

    public Dipendente(String username, String nome, String cognome, String email) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
}
