package bertcoscia.Epicode_W18D5.services;

import bertcoscia.Epicode_W18D5.entities.Dipendente;
import bertcoscia.Epicode_W18D5.entities.Prenotazione;
import bertcoscia.Epicode_W18D5.entities.Viaggio;
import bertcoscia.Epicode_W18D5.exceptions.BadRequestException;
import bertcoscia.Epicode_W18D5.exceptions.NotFoundException;
import bertcoscia.Epicode_W18D5.payloads.ChangeDipendenteDTO;
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
        if (this.repository.existsByDipendenteAndViaggioData(dFound, vFound.getData()))throw new BadRequestException("L'utente " + body.idDipendente() + " ha già effettuato una prenotazione per la data " + body.dataPrenotazione());
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

    public Prenotazione findByIdAndUpdate(UUID id, NewPrenotazioneDTO body) {
        Prenotazione pFound = this.findById(id);
        Dipendente dFound = this.dipendentiService.findById(body.idDipendente());
        Viaggio vFound = this.viaggiService.findById(body.idViaggio());
        if (this.repository.existsByDipendenteAndViaggio(pFound.getDipendente(), pFound.getViaggio()) && !pFound.getIdPrenotazione().equals(id)) throw new BadRequestException("L'utente " + pFound.getDipendente().getIdDipendente() + " ha già una prenotazione per la data " + pFound.getViaggio().getData());
        pFound.setNote(body.note());
        pFound.setDipendente(dFound);
        pFound.setViaggio(vFound);
        pFound.setDataPrenotazione(body.dataPrenotazione());
        return this.repository.save(pFound);
    }

    public void findByIdAndDelete(UUID id) {
        Prenotazione found = this.findById(id);
        this.repository.delete(found);
    }

    public Prenotazione changeDipendente(UUID idPrenotazione, ChangeDipendenteDTO body) {
        Prenotazione pFound = this.findById(idPrenotazione);
        Dipendente dFound = this.dipendentiService.findById(body.idDipendente());
        if (this.repository.existsByDipendenteAndViaggioData(dFound, pFound.getViaggio().getData())) throw new BadRequestException("L'utente " + dFound.getNome() + dFound.getCognome() + " ha già una prenotazione per la data " + pFound.getViaggio().getData());
        pFound.setDipendente(dFound);
        pFound.setDataPrenotazione(LocalDate.now());
        return this.repository.save(pFound);
    }

}
