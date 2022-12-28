package com.gabriel.todoapp.domain.exception;

public class TarefaNaoEncontradaException extends EntidadeNaoEncontradaException{
    public TarefaNaoEncontradaException(String message) {
        super(message);
    }

    public TarefaNaoEncontradaException(Long id) {
        super(String.format("Tarefa não encontrada, id: #%s", id));
    }
}
