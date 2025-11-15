package br.com.pcsaude.entities;

import br.com.pcsaude.Enum.GrauAlertaEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ALERTA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário associado ao alerta
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Medição relacionada ao alerta (opcional)
    @ManyToOne
    @JoinColumn(name = "medicao_id")
    private Medicao medicao;

    @NotNull(message = "O título do alerta é obrigatório")
    @Size(min = 3, max = 50, message = "O título deve ter entre 3 e 50 caracteres")
    private String titulo;

    @NotNull(message = "A descrição é obrigatória")
    @Size(min = 5, max = 150, message = "A descrição deve ter entre 5 e 150 caracteres")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GrauAlertaEnum grau;

    @Column(name = "momento_alerta", nullable = false)
    private LocalDate momentoAlerta;

    @PrePersist
    protected void prePersist() {

        if (grau == null) {
            grau = GrauAlertaEnum.BAIXO;
        }

        if (momentoAlerta == null) {
            momentoAlerta = LocalDate.now();
        }
    }

    public Alerta(Usuario usuario, Medicao medicao, String titulo, String descricao, GrauAlertaEnum grau) {
        this.usuario = usuario;
        this.medicao = medicao;
        this.titulo = titulo;
        this.descricao = descricao;
        this.grau = grau;
        this.momentoAlerta = LocalDate.now();
    }
}

