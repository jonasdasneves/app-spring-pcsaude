package br.com.pcsaude.repositories;

import br.com.pcsaude.entities.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}
