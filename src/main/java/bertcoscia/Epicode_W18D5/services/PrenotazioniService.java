package bertcoscia.Epicode_W18D5.services;

import bertcoscia.Epicode_W18D5.entities.Dipendente;
import bertcoscia.Epicode_W18D5.entities.Prenotazione;
import bertcoscia.Epicode_W18D5.entities.Viaggio;
import bertcoscia.Epicode_W18D5.exceptions.BadRequestException;
import bertcoscia.Epicode_W18D5.exceptions.NotFoundException;
import bertcoscia.Epicode_W18D5.payloads.NewPrenotazioneDTO;
import bertcoscia.Epicode_W18D5.payloads.NewPrenotazioneRespDTO;
import bertcoscia.Epicode_W18D5.repositories.PrenotazioniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PrenotazioniService {
    @Autowired
    PrenotazioniRepository repository;

    @Autowired
    DipendentiService dipendentiService;

    @Autowired
    ViaggiService viaggiService;

    public NewPrenotazioneRespDTO save(NewPrenotazioneDTO body) {
        Dipendente dFound = this.dipendentiService.findById(body.idDipendente());
        Viaggio vFound = this.viaggiService.findById(body.idViaggio());
        if (this.repository.existsByDipendenteAndData(dFound, vFound.getData()))throw new BadRequestException("L'utente " + body.idDipendente() + " ha già effettuato una prenotazione per la data " + body.dataPrenotazione());
        Prenotazione newPrenotazione = new Prenotazione(body.dataPrenotazione(), body.note(), vFound, dFound);
        return new NewPrenotazioneRespDTO(this.repository.save(newPrenotazione).getIdPrenotazione());
    }

    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.repository.findAll(pageable);
    }

    public Prenotazione findById(UUID id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Prenotazione findByIdAndUpdate(UUID id, Prenotazione body) {
        Prenotazione found = this.findById(id);
        if (this.repository.existsByDipendenteAndViaggio(found.getDipendente(), found.getViaggio()) && !found.getIdPrenotazione().equals(id)) throw new BadRequestException("L'utente " + found.getDipendente().getIdDipendente() + " ha già una prenotazione per la data " + found.getViaggio().getData());
        found.setNote(body.getNote());
        found.setDipendente(body.getDipendente());
        found.setViaggio(body.getViaggio());
        found.setDataPrenotazione(body.getDataPrenotazione());
        return this.repository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Prenotazione found = this.findById(id);
        this.repository.delete(found);
    }

    public Prenotazione changeDipendente(UUID idPrenotazione, UUID idDipendente) {
        Prenotazione pFound = this.findById(idDipendente);
        Dipendente dFound = this.dipendentiService.findById(idDipendente);
        if (this.repository.existsByDipendenteAndData(dFound, pFound.getViaggio().getData())) throw new BadRequestException("L'utente " + dFound.getNome() + dFound.getCognome() + " ha già una prenotazione per la data " + pFound.getViaggio().getData());
        pFound.setDipendente(dFound);
        pFound.setDataPrenotazione(LocalDate.now());
        return this.repository.save(pFound);
    }

}
