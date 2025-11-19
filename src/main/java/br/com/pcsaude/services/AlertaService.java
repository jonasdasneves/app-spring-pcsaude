package br.com.pcsaude.services;

import br.com.pcsaude.entities.Alerta;
import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Suporte;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.exceptions.ResourceNotFoundException;
import br.com.pcsaude.repositories.AlertaRepository;
import br.com.pcsaude.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AlertaService {

    private final AlertaRepository repository;

    public AlertaService(AlertaRepository repository) {
        this.repository = repository;
    }

    public Alerta findById(Long id) {
        try {
            return this.repository.findById(id).orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException("Não foi possível encontrar um alerta com o id " + id);
        }
    }

    public Page<Alerta> findAll(int page, int size) {

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.repository
                .findAllByUsuario_Id(
                        usuario.getId(),
                        Pageable
                                .ofSize(size)
                                .withPage(page));
    }

    public Alerta save(Alerta alerta) {
        return this.repository.save(alerta);
    }
}
