package bertcoscia.Epicode_W18D5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChangeDipendenteDTO(
        @NotNull(message = "Id dipendente obbligatorio")
        UUID idDipendente
) {
}
