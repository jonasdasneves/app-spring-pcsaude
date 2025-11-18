package br.com.pcsaude.records;

import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.enums.SuporteStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SuporteOutDto(

        Long id,

        @NotNull(message = "O título é obrigatório")
        @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
        String titulo,

        @NotNull(message = "A descrição é obrigatória")
        String descricao,

        LocalDate dataReclamacao,

        LocalDate dataResolucao,

        @Enumerated(EnumType.STRING)
        SuporteStatusEnum status
){
}
