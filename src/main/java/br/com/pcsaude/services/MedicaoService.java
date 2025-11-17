package br.com.pcsaude.services;

import br.com.pcsaude.entities.Medicao;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.repositories.MedicaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class MedicaoService {

    private final MedicaoRepository repository;

    public MedicaoService(MedicaoRepository repository) {
        this.repository = repository;
    }

    public Medicao save(Medicao medicao){
        return repository.save(medicao);
    }

    public Page<Medicao> findAll(int page, int size){

        //TO DO Usuário deve ser definido por contexto
        Usuario usuario = new Usuario();

        return this.repository
                .findAllByUsuario_Id(
                        usuario.getId(),
                        Pageable
                        .ofSize(size)
                        .withPage(page));
    }

    public Medicao findUltimaMedicao(){

        //TO DO Usuário deve ser definido por contexto
        Usuario usuario = new Usuario();

        return this.repository
                .findFisrtByUsuario_IdOrderByMomentoMedicaoDesc(usuario.getId())
                .orElseThrow();
    }
}
