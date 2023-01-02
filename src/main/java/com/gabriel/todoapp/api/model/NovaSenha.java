package com.gabriel.todoapp.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NovaSenha {

    @NotBlank
    private String senhaAtual;
    @NotBlank
    private String senhaNova;
}
