package br.com.pcsaude.services;

import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.exceptions.RecursoNaoPertencenteException;
import br.com.pcsaude.exceptions.ResourceNotFoundException;
import br.com.pcsaude.exceptions.UniqueKeyDuplicadaException;
import br.com.pcsaude.repositories.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    private final DispositivoService dispositivoService;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, DispositivoService dispositivoService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.dispositivoService = dispositivoService;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario findById(Long id){

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            Usuario usuario = this.repository.findById(id).orElseThrow();

            if (!Objects.equals(usuarioLogado.getId(), id)){
                throw new RecursoNaoPertencenteException("Apenas o usuário pode consultar seu perfil");
            }

            return usuario;
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException("Não foi possível encontrar um usuário com o id " + id);
        }
    }

    public Usuario save(Usuario usuario){
        if (this.repository.findFirstByEmail(usuario.getEmail()) != null){
            throw new UniqueKeyDuplicadaException("Já existe um usuário cadastrado com o e-mail " + usuario.getEmail());
        }

        this.dispositivoService.save(usuario.getDispositivo());

        usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));

        return this.repository.save(usuario);
    }

    public Usuario update(Usuario usuario){

        Usuario usuarioOriginal = this.findById(usuario.getId());
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuarioLogado.getId() != usuarioOriginal.getId()){
            throw new RecursoNaoPertencenteException("Apenas o usuário pode editar seu perfil");
        }

        usuarioOriginal.setNome(usuario.getNome());
        usuarioOriginal.setEmail(usuario.getEmail());
        usuarioOriginal.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioOriginal.setModeloTrabalho(usuario.getModeloTrabalho());
        usuarioOriginal.setAltura(usuario.getAltura());
        usuarioOriginal.setPeso(usuario.getPeso());

        return this.repository.save(usuarioOriginal);
    }

    public void delete(Long id){

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(usuarioLogado.getId(), id)){
            throw new RecursoNaoPertencenteException("Apenas o usuário pode deletar seu perfil");
        }

        this.repository.deleteById(id);
    }
}
