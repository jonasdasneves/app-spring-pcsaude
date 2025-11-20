package br.com.pcsaude.records;

import br.com.pcsaude.enums.PosturaEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MedicaoOutDto(
        Long id,
        String uuidDispositivo,
        PosturaEnum postura,
        Integer tempoSentadoMin,
        BigDecimal iluminacaoLux,
        BigDecimal temperaturaC,
        BigDecimal alturaTelaCm,
        LocalDateTime momentoMedicao
) {}
