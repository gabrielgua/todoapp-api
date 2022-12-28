package com.gabriel.todoapp.domain.service;

import com.gabriel.todoapp.domain.exception.TarefaNaoEncontradaException;
import com.gabriel.todoapp.domain.exception.UsuarioNaoEncontradoException;
import com.gabriel.todoapp.domain.model.Tarefa;
import com.gabriel.todoapp.domain.repository.TarefaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TarefaService {

    private TarefaRepository repository;


    public List<Tarefa> buscarPorUsuarioId(Long id) {
        return repository.findByUsuarioId(id)
                .orElseThrow((() -> new UsuarioNaoEncontradoException(id)));
    }


    @Transactional(readOnly = true)
    public List<Tarefa> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    public Tarefa salvar(Tarefa tarefa) {
        return repository.save(tarefa);
    }

    public void remover(Long id) {
        repository.deleteById(id);
    }


}
