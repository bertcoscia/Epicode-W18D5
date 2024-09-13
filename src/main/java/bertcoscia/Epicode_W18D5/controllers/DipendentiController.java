package bertcoscia.Epicode_W18D5.controllers;

import bertcoscia.Epicode_W18D5.entities.Dipendente;
import bertcoscia.Epicode_W18D5.exceptions.BadRequestException;
import bertcoscia.Epicode_W18D5.payloads.NewDipendenteDTO;
import bertcoscia.Epicode_W18D5.payloads.NewDipendenteRespDTO;
import bertcoscia.Epicode_W18D5.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {
    @Autowired
    private DipendentiService dipendentiService;

    // GET http://localhost:3001/dipendenti
    @GetMapping
    public Page<Dipendente> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendentiService.findAll(page, size, sortBy);
    }

    // GET http://localhost:3001/dipendenti/{idDipendente}
    @GetMapping("/{idDipendente}")
    public Dipendente findById(@PathVariable UUID idDipendente) {
        return this.dipendentiService.findById(idDipendente);
    }

    // POST http://localhost:3001/dipendenti + body
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewDipendenteRespDTO save(@RequestBody @Validated NewDipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewDipendenteRespDTO(this.dipendentiService.save(body).getIdDipendente());
        }
    }

    // PUT http://localhost:3001/dipendenti/{idDipendente} + body
    @PutMapping("/{idDipendente}")
    public Dipendente findByIdAndUpdate(@PathVariable UUID idDipendente, @RequestBody Dipendente body) {
        return this.dipendentiService.findByIdAndUpdate(idDipendente, body);
    }

    // DELETE http://localhost:3001/dipendenti/{idDipendente}
    @DeleteMapping("/{idDipendente}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID idDipendente) {
        this.dipendentiService.findByIdAndDelete(idDipendente);
    }

    // POST http://localhost:3001/dipendenti/{idDipendente}/avatar
    @PostMapping("/{idDipendente}/avatar")
    public void uploadImage(@RequestParam("avatar") MultipartFile image, @PathVariable UUID idDipendente) throws IOException {
        this.dipendentiService.uploadImage(image, idDipendente);
    }


}
