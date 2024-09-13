package bertcoscia.Epicode_W18D5.services;

import bertcoscia.Epicode_W18D5.entities.Dipendente;
import bertcoscia.Epicode_W18D5.exceptions.BadRequestException;
import bertcoscia.Epicode_W18D5.exceptions.NotFoundException;
import bertcoscia.Epicode_W18D5.payloads.NewDipendenteDTO;
import bertcoscia.Epicode_W18D5.repositories.DipendentiRepository;
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
public class DipendentiService {
    @Autowired
    private DipendentiRepository dipendentiRepository;

    @Autowired
    private Cloudinary cloudinary;

    public Dipendente save(NewDipendenteDTO body) {
        if (this.dipendentiRepository.existsByUsername(body.username())) throw new BadRequestException("Username già esistente");
        if (this.dipendentiRepository.existsByEmail(body.email())) throw new BadRequestException("Email già esistente");
        Dipendente newDipendente = new Dipendente(body.username(), body.nome(), body.cognome(), body.email());
        newDipendente.setUrlAvatar("https://ui-avatars.com/api/?name=" + newDipendente.getNome() + "+" + newDipendente.getCognome());
        return this.dipendentiRepository.save(newDipendente);
    }

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendentiRepository.findAll(pageable);
    }

    public Dipendente findById(UUID id) {
        return this.dipendentiRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Dipendente findByIdAndUpdate(UUID id, Dipendente body) {
        Dipendente found = this.findById(id);
        if (this.dipendentiRepository.existsByEmail(body.getEmail()) && !found.getIdDipendente().equals(id)) throw new BadRequestException("Email già esistente");
        if (this.dipendentiRepository.existsByUsername(body.getUsername()) && !found.getIdDipendente().equals(id)) throw new BadRequestException("Username già esistente");
        found.setEmail(body.getEmail());
        found.setNome(body.getNome());
        found.setCognome(body.getCognome());
        found.setUsername(body.getUsername());
        return this.dipendentiRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Dipendente found = this.findById(id);
        this.dipendentiRepository.delete(found);
    }

    public void uploadImage(MultipartFile file, UUID idDipendente) throws IOException {
        Dipendente found = this.findById(idDipendente);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setUrlAvatar(url);
        this.dipendentiRepository.save(found);
    }

}
