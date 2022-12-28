package com.gabriel.todoapp.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException{
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }

    public UsuarioNaoEncontradoException(Long id) {
        super(String.format("Usuário não encontrado, id: #%s", id));
    }
}
