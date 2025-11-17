package br.com.pcsaude.entities;

import br.com.pcsaude.enums.ModeloTrabalhoEnum;
import br.com.pcsaude.enums.Role;
import br.com.pcsaude.enums.SexoEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "USUARIO")
@NoArgsConstructor
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome do usuário não pode ser nulo")
    @Length(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(unique = true, nullable = false)
    private String nome;

    @Email(message = "Insira um e-mail válido")
    @Length(max = 40, message = "O e-mail deve ter no máximo 40 caracteres")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "A senha não pode ser nula")
    @Length(min = 8, message = "A senha deve possuir pelo menos 8 caracteres")
    @Column(name = "senha", nullable = false)
    private String senha;

    @Digits(integer = 3, fraction = 2,
            message = "A altura deve ter até 3 dígitos antes e 2 após a vírgula")
    @DecimalMin(value = "0.0", message = "A altura não deve ser negativa")
    private BigDecimal altura;

    @Digits(integer = 3, fraction = 2,
            message = "O peso deve ter até 3 dígitos antes e 2 após a vírgula")
    @DecimalMin(value = "0.0", message = "O peso não deve ser negativo")
    private BigDecimal peso;

    @NotNull(message = "O sexo deve ser informado")
    @Enumerated(EnumType.STRING)
    private SexoEnum sexo;

    @Column(name = "modelo_trabalho")
    @Enumerated(EnumType.STRING)
    private ModeloTrabalhoEnum modeloTrabalho;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, name = "data_cadastro")
    private LocalDate dataCadastro;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = Role.USER;
        }
        if (this.modeloTrabalho == null) {
            this.modeloTrabalho = ModeloTrabalhoEnum.PRESENCIAL;
        }
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDate.now();
        }
    }

    public Usuario(Role role,
                   String nome,
                   String email,
                   String senha,
                   BigDecimal altura,
                   BigDecimal peso,
                   SexoEnum sexo,
                   ModeloTrabalhoEnum modeloTrabalho) {

        this.role = (role == null ? Role.USER : role);
        this.modeloTrabalho = (modeloTrabalho == null ? ModeloTrabalhoEnum.PRESENCIAL : modeloTrabalho);

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.altura = altura;
        this.peso = peso;
        this.sexo = sexo;

        this.dataCadastro = LocalDate.now();
    }
}