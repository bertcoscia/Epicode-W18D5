package bertcoscia.Epicode_W18D5.controllers;

import bertcoscia.Epicode_W18D5.entities.Dipendente;
import bertcoscia.Epicode_W18D5.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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


}
