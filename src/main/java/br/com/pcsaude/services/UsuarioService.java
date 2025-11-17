package br.com.pcsaude.services;

import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.exceptions.ResourceNotFoundException;
import br.com.pcsaude.exceptions.UniqueKeyDuplicadaException;
import br.com.pcsaude.repositories.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario findById(Long id){
        return this.repository
                        .findById(id)
                        .orElseThrow();
    }

    public Page<Usuario> findAll(int page, int size){
        return this.repository
                        .findAll( Pageable
                                .ofSize(size)
                                .withPage(page));
    }

    public Usuario save(Usuario usuario){

        if (this.repository.findFirstByNome(usuario.getNome()) != null){
            throw new UniqueKeyDuplicadaException("Já existe um usuário cadastrado com o nome " + usuario.getNome());
        }
        if (this.repository.findFirstByEmail(usuario.getEmail()) != null){
            throw new UniqueKeyDuplicadaException("Já existe um usuário cadastrado com o e-mail " + usuario.getEmail());
        }

        return this.repository.save(usuario);
    }

    public Usuario update(Usuario usuario){
        Usuario usuarioOriginal = this.findById(usuario.getId());

        usuarioOriginal.setNome(usuario.getNome());
        usuarioOriginal.setEmail(usuario.getEmail());
        usuarioOriginal.setSenha(usuario.getSenha());
        usuarioOriginal.setModeloTrabalho(usuario.getModeloTrabalho());
        usuarioOriginal.setAltura(usuario.getAltura());
        usuarioOriginal.setPeso(usuario.getPeso());

        return this.repository.save(usuarioOriginal);
    }

    public void delete(Long id){
        this.repository.deleteById(id);
    }
}
