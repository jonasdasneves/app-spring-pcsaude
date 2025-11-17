package br.com.pcsaude.records;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MedicaoOutDto(
        Long id,
        String uuidDispositivo,
        String postura,
        Integer tempoSentadoMin,
        BigDecimal iluminacaoLux,
        BigDecimal temperaturaC,
        BigDecimal alturaTelaCm,
        LocalDateTime momentoMedicao
) {}
