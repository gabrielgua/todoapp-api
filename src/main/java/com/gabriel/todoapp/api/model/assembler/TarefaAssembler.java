package com.gabriel.todoapp.api.model.assembler;

import com.gabriel.todoapp.api.model.TarefaRequest;
import com.gabriel.todoapp.api.model.TarefaResponse;
import com.gabriel.todoapp.domain.model.Tarefa;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TarefaAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public TarefaResponse toModel(Tarefa tarefa) {
        return modelMapper.map(tarefa, TarefaResponse.class);
    }

    public List<TarefaResponse> toCollectionList(List<Tarefa> tarefas) {
        return tarefas.stream()
                .map(tarefa -> toModel(tarefa))
                .collect(Collectors.toList());
    }

    public Tarefa toEntity(TarefaRequest request) {
        return modelMapper.map(request, Tarefa.class);
    }

    public void copyToEntity(TarefaRequest request, Tarefa tarefa) {
        modelMapper.map(request, tarefa);
    }
}
