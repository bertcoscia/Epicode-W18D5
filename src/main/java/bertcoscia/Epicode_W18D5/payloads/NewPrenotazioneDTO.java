package bertcoscia.Epicode_W18D5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record NewPrenotazioneDTO(
        @NotNull(message = "Data prenotazione obbligatoria")
        LocalDate dataPrenotazione,
        String note,
        @NotNull(message = "Id viaggio obbligatorio")
        UUID idViaggio,
        @NotNull(message = "Id dipendente obbligatorio")
        UUID idDipendente
) {
}
