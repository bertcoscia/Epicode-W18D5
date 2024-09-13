package bertcoscia.Epicode_W18D5.services;

import bertcoscia.Epicode_W18D5.entities.Viaggio;
import bertcoscia.Epicode_W18D5.enums.ViaggioStato;
import bertcoscia.Epicode_W18D5.exceptions.BadRequestException;
import bertcoscia.Epicode_W18D5.exceptions.NotFoundException;
import bertcoscia.Epicode_W18D5.payloads.NewViaggioDTO;
import bertcoscia.Epicode_W18D5.repositories.ViaggiRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ViaggiService {
    @Autowired
    private ViaggiRepository viaggiRepository;

    @Autowired
    private Cloudinary cloudinary;

    public Viaggio save(NewViaggioDTO body) {
        if (this.viaggiRepository.existsByDestinazioneAndData(body.destinazione(), body.data())) throw new BadRequestException("Esiste già una viaggio con destinazione " + body.destinazione() + " per la data " + body.data());
        ViaggioStato stato;
        if (body.stato().equalsIgnoreCase("in_programma")) {
            stato = ViaggioStato.IN_PROGRAMMA;
        } else if (body.stato().equalsIgnoreCase("completato")) {
            stato = ViaggioStato.COMPLETATO;
        } else {
            throw new BadRequestException("Il valore inserito per 'stato viaggio' non è valido.");
        }
        return this.viaggiRepository.save(new Viaggio(body.destinazione(), body.data(), stato));
    }

    public Page<Viaggio> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggiRepository.findAll(pageable);
    }

    public Viaggio findById(UUID id) {
        return this.viaggiRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Viaggio findByIdAndUpdate(UUID id, Viaggio body) {
        Viaggio found = this.findById(id);
        if (this.viaggiRepository.existsByDestinazioneAndData(body.getDestinazione(), body.getData()) && !found.getIdViaggio().equals(id)) throw new BadRequestException("Esiste già una viaggio con destinazione " + body.getDestinazione() + " per la data " + body.getData());
        found.setData(body.getData());
        found.setStato(body.getStato());
        found.setDestinazione(body.getDestinazione());
        return this.viaggiRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Viaggio found = this.findById(id);
        this.viaggiRepository.delete(found);
    }

    public Viaggio changeViaggioStato(UUID id) {
        Viaggio found = this.findById(id);
        if (found.getStato().equals(ViaggioStato.IN_PROGRAMMA)) {
            found.setStato(ViaggioStato.COMPLETATO);
        } else {
            found.setStato(ViaggioStato.IN_PROGRAMMA);
        }
        return this.viaggiRepository.save(found);
    }

    /*public void uploadImage(MultipartFile file, UUID idViaggio) throws IOException {
        Viaggio found = this.findById(idViaggio);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setUrlCover(url);
        this.viaggiRepository.save(found);
    }*/


}
