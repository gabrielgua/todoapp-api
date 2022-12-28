package com.gabriel.todoapp.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class TarefaResponse {

    private Long id;
    private String titulo;
    private Boolean concluida;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataConclusao;
    private Long usuarioId;
}
