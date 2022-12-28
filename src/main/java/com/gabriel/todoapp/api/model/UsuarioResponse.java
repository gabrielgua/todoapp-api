package com.gabriel.todoapp.api.model;

import com.gabriel.todoapp.domain.model.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
}
