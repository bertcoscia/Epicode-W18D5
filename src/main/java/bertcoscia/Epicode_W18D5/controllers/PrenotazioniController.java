package bertcoscia.Epicode_W18D5.controllers;

import bertcoscia.Epicode_W18D5.entities.Prenotazione;
import bertcoscia.Epicode_W18D5.exceptions.BadRequestException;
import bertcoscia.Epicode_W18D5.payloads.ChangeDipendenteDTO;
import bertcoscia.Epicode_W18D5.payloads.NewPrenotazioneDTO;
import bertcoscia.Epicode_W18D5.payloads.NewPrenotazioneRespDTO;
import bertcoscia.Epicode_W18D5.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    @Autowired
    PrenotazioniService service;

    // GET http://localhost:3001/prenotazioni
    @GetMapping
    public Page<Prenotazione> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataPrenotazione") String sortBy) {
        return this.service.findAll(page, size, sortBy);
    }

    // GET http://localhost:3001/prenotazioni/{idPrenotazione}
    @GetMapping("/{idPrenotazione}")
    public Prenotazione findById(@PathVariable UUID idPrenotazione) {
        return this.service.findById(idPrenotazione);
    }

    // POST http://localhost:3001/prenotazioni + body
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewPrenotazioneRespDTO save(@RequestBody @Validated NewPrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewPrenotazioneRespDTO(this.service.save(body).idPrenotazione());
        }
    }

    // PUT http://localhost:3001/prenotazioni/{idPrenotazione} + body
    @PutMapping("/{idPrenotazione}")
    public Prenotazione findByIdAndUpdate(@PathVariable UUID idPrenotazione, @RequestBody @Validated NewPrenotazioneDTO body) {
        return this.service.findByIdAndUpdate(idPrenotazione, body);
    }

    // DELETE http://localhost:3001/prenotazioni/{idPrenotazione}
    @DeleteMapping("/{idPrenotazione}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID idPrenotazione) {
        this.service.findByIdAndDelete(idPrenotazione);
    }

    /*// PUT http://localhost:3001/prenotazioni/{idPrenotazione}/{idDipendente}
    @PutMapping("/{idPrenotazione}/{idDipendente}")
    public Prenotazione changeDipendente(@PathVariable UUID idPrenotazione, @PathVariable UUID idDipendente, @RequestBody ChangeDipendenteDTO body) {
        return this.service.changeDipendente(idPrenotazione, idDipendente);
    }*/
}
