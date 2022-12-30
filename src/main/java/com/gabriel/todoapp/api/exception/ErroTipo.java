package com.gabriel.todoapp.api.exception;

import lombok.Getter;

@Getter
public enum ErroTipo {
    NEGOCIO_ERRO("/negocio-erro", "Erro de negócio"),

    CORPO_NAO_LEGIVEL("/corpo-nao-legivel", "Corpo não legível"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro interno do servidor"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    DADOS_INVALIDOS("/dados-invalidos", "Dados Inválidos"),
    ACESSO_NEGADO("/acesso-negado", "Acesso Negado"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso");

    private String title;
    private String uri;

    ErroTipo(String path, String title) {
        this.uri = "https://todoapp.com" + path;
        this.title = title;
    }

}
