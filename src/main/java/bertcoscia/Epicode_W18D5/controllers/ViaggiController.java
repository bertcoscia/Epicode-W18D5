package bertcoscia.Epicode_W18D5.controllers;

import bertcoscia.Epicode_W18D5.entities.Viaggio;
import bertcoscia.Epicode_W18D5.exceptions.BadRequestException;
import bertcoscia.Epicode_W18D5.payloads.NewViaggioDTO;
import bertcoscia.Epicode_W18D5.payloads.NewViaggioRespDTO;
import bertcoscia.Epicode_W18D5.services.ViaggiService;
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
@RequestMapping("/viaggi")
public class ViaggiController {
    @Autowired
    private ViaggiService service;

    // GET http://localhost:3001/viaggi
    @GetMapping
    public Page<Viaggio> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy) {
        return this.service.findAll(page, size, sortBy);
    }

    // GET http://localhost:3001/viaggi/{idViaggio}
    @GetMapping("/{idViaggio}")
    public Viaggio findById(@RequestParam UUID idViaggio) {
        return this.service.findById(idViaggio);
    }

    // POST http://localhost:3001/viaggi + body
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewViaggioRespDTO save(@RequestBody @Validated NewViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewViaggioRespDTO(this.service.save(body).getIdViaggio());
        }
    }

    // PUT http://localhost:3001/viaggi/{idViaggio} + body
    @PutMapping("/{idViaggio}")
    public Viaggio findByIdAndUpdate(@PathVariable UUID idViaggio, @RequestBody Viaggio body) {
        return this.service.findByIdAndUpdate(idViaggio, body);
    }

    // DELETE http://localhost:3001/viaggi/{idViaggio}
    @DeleteMapping("/{idViaggio}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID idViaggio) {
        this.service.findByIdAndDelete(idViaggio);
    }


}
