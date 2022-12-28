package com.gabriel.todoapp.api.model;

import com.gabriel.todoapp.domain.model.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UsuarioRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private TipoUsuario tipo;
}
