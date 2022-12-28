package com.gabriel.todoapp.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tarefa {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Boolean concluida = false;

    @CreationTimestamp
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataConclusao;

    @ManyToOne
    private Usuario usuario;

    public void concluir() {
        setConcluida(!concluida);
    }

    public void limparDataConclusao() {
        if (!getConcluida()) {
            setDataConclusao(OffsetDateTime.now());
        } else {
            setDataConclusao(null);
        }
    }


}
