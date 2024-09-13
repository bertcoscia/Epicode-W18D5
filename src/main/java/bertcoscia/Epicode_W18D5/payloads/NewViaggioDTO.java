package bertcoscia.Epicode_W18D5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewViaggioDTO(
        @NotEmpty(message = "Destinazione obbligatoria")
        String destinazione,
        @NotNull(message = "Data obbligatoria")
        LocalDate data,
        @NotEmpty(message = "Stato obbligatorio")
        String stato
) {
}
