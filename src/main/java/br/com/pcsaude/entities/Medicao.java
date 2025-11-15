package br.com.pcsaude.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEDICAO")
@NoArgsConstructor
@Getter
@Setter
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Usuário associado ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Length(max = 50, message = "A descrição da postura deve ter no máximo 50 caracteres")
    private String postura;

    @Min(value = 0, message = "O tempo sentado não pode ser negativo")
    @Max(value = 1440, message = "O tempo sentado deve ser menor que 1440 minutos (1 dia)")
    @Column(name = "tempo_sentado_min")
    private Integer tempoSentadoMin;

    @Digits(integer = 10, fraction = 2, message = "A iluminação deve ter até 10 dígitos e 2 casas decimais")
    @DecimalMin(value = "0.0", message = "O nível de iluminação não pode ser negativo")
    @Column(name = "iluminacao_lux")
    private BigDecimal iluminacaoLux;

    @Digits(integer = 5, fraction = 2, message = "A temperatura deve ter até 5 dígitos com 2 casas decimais")
    @Column(name = "temperatura_c")
    private BigDecimal temperaturaC;

    @Digits(integer = 5, fraction = 2, message = "A altura da tela deve ter até 5 dígitos com 2 casas decimais")
    @DecimalMin(value = "0.0", message = "A altura da tela não pode ser negativa")
    @Column(name = "altura_tela_cm")
    private BigDecimal alturaTelaCm;

    @Column(name = "momento_medicao", nullable = false)
    private LocalDateTime momentoMedicao;

    @PrePersist
    public void prePersist() {
        if (this.momentoMedicao == null) {
            this.momentoMedicao = LocalDateTime.now();
        }
    }

    public Medicao(Usuario usuario,
                   String postura,
                   Integer tempoSentadoMin,
                   BigDecimal iluminacaoLux,
                   BigDecimal temperaturaC,
                   BigDecimal alturaTelaCm) {

        this.usuario = usuario;
        this.postura = postura;
        this.tempoSentadoMin = tempoSentadoMin;
        this.iluminacaoLux = iluminacaoLux;
        this.temperaturaC = temperaturaC;
        this.alturaTelaCm = alturaTelaCm;

        this.momentoMedicao = LocalDateTime.now();
    }
}