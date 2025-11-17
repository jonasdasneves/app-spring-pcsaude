package br.com.pcsaude.repositories;

import br.com.pcsaude.entities.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {
}
