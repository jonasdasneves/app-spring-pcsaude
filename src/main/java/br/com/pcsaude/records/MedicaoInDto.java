package br.com.pcsaude.records;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MedicaoInDto(

        @Length(max = 50, message = "O uuid do dispositivo deve ter no máximo 50 caracteres")
        @NotNull(message = "O uuid do dispositivo é obrigatório")
        String uuidDispositivo,

        @Length(max = 50, message = "A descrição da postura deve ter no máximo 50 caracteres")
        String postura,

        @Min(value = 0, message = "O tempo sentado não pode ser negativo")
        @Max(value = 1440, message = "O tempo sentado deve ser menor que 1440 minutos (1 dia)")
        Integer tempoSentadoMin,

        @Digits(integer = 10, fraction = 2, message = "A iluminação deve ter até 10 dígitos e 2 casas decimais")
        @DecimalMin(value = "0.0", message = "O nível de iluminação não pode ser negativo")
        BigDecimal iluminacaoLux,

        @Digits(integer = 5, fraction = 2, message = "A temperatura deve ter até 5 dígitos com 2 casas decimais")
        BigDecimal temperaturaC,

        @Digits(integer = 5, fraction = 2, message = "A altura da tela deve ter até 5 dígitos com 2 casas decimais")
        @DecimalMin(value = "0.0", message = "A altura da tela não pode ser negativa")
        BigDecimal alturaTelaCm
) {}

