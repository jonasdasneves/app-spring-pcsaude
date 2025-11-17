package br.com.pcsaude.entities;

import br.com.pcsaude.enums.SuporteStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "SUPORTE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Suporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário que abriu a reclamação
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Administrador que prestou o suporte (pode ser nulo inicialmente)
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Usuario admin;

    @NotNull(message = "O título é obrigatório")
    @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
    private String titulo;

    @NotNull(message = "A descrição é obrigatória")
    @Lob
    @Column(columnDefinition = "CLOB", nullable = false)
    private String descricao;

    @Column(name = "data_reclamacao", nullable = false)
    private LocalDate dataReclamacao;

    private LocalDate dataResolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SuporteStatusEnum status;

    @PrePersist
    protected void prePersist() {
        if (dataReclamacao == null) {
            dataReclamacao = LocalDate.now();
        }
        if (status == null) {
            status = SuporteStatusEnum.AGUARDANDO;
        }
    }

    public Suporte(Usuario usuario, String titulo, String descricao) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.descricao = descricao;

        this.status = SuporteStatusEnum.AGUARDANDO;
        this.dataReclamacao = LocalDate.now();
    }
}

