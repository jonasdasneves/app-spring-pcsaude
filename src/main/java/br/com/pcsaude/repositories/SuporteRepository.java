package br.com.pcsaude.repositories;

import br.com.pcsaude.entities.Medicao;
import br.com.pcsaude.entities.Suporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuporteRepository extends JpaRepository<Suporte, Long> {
    Page<Suporte> findAllByUsuario_Id(Long id, Pageable pageable);
}
