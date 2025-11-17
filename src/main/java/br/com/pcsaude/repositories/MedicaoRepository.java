package br.com.pcsaude.repositories;

import br.com.pcsaude.entities.Medicao;
import br.com.pcsaude.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

    Page<Medicao> findAllByUsuario_Id(Long id, Pageable pageable);

    Optional<Medicao> findFisrtByUsuario_IdOrderByMomentoMedicaoDesc(Long usuarioId);
}
