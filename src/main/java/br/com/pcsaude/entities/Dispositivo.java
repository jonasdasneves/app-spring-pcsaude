package br.com.pcsaude.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "DISPOSITIVO")
@NoArgsConstructor
@Getter
@Setter
public class Dispositivo {

    @Id
    private String uuid;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    public Dispositivo(String uuid) {
        this.uuid = uuid;
        this.dataCadastro = LocalDate.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDate.now();
        }
    }
}
