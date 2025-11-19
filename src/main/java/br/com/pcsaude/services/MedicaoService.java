package br.com.pcsaude.services;

import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Medicao;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.exceptions.ResourceNotFoundException;
import br.com.pcsaude.repositories.MedicaoRepository;
import br.com.pcsaude.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MedicaoService {

    private final MedicaoRepository repository;


    public MedicaoService(MedicaoRepository repository) {
        this.repository = repository;
    }

    public Medicao save(Medicao medicao){
        return repository.save(medicao);
    }

    public Page<Medicao> findAll(int page, int size){

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Dispositivo dispositivo = usuario.getDispositivo();

        return this.repository
                .findAllByDispositivo_Uuid(
                        dispositivo.getUuid(),
                        Pageable
                        .ofSize(size)
                        .withPage(page));
    }

    public Medicao findUltimaMedicao(){

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Dispositivo dispositivo = usuario.getDispositivo();

        try {
            return this.repository
                    .findFisrtByDispositivo_UuidOrderByMomentoMedicaoDesc(dispositivo.getUuid())
                    .orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException("Não foi possível encontrar medições para seu dispositivo. ");
        }

    }
}
