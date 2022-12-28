package com.gabriel.todoapp.api.controller;

import com.gabriel.todoapp.api.model.TarefaRequest;
import com.gabriel.todoapp.api.model.TarefaResponse;
import com.gabriel.todoapp.api.model.assembler.TarefaAssembler;
import com.gabriel.todoapp.domain.model.Tarefa;
import com.gabriel.todoapp.domain.model.Usuario;
import com.gabriel.todoapp.domain.service.TarefaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("tarefas")
public class TarefaController {

    private TarefaService service;
    private TarefaAssembler assembler;

    @GetMapping
    public List<TarefaResponse> listar() {
        return assembler.toCollectionList(service.listar());
    }

    @GetMapping("/{id}")
    public TarefaResponse buscar(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse adicionar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody TarefaRequest request) {

        var usuario_id = Long.valueOf(jwt.getClaimAsString("usuario_id"));
        var usuario = new Usuario();
        usuario.setId(usuario_id);

        var tarefa = assembler.toEntity(request);
        tarefa.setUsuario(usuario);

        return assembler.toModel(service.salvar(tarefa));
    }

    @PutMapping("/{id}")
    public TarefaResponse editar(@PathVariable Long id, @Valid @RequestBody TarefaRequest request) {
        var tarefa = service.buscarPorId(id);
        assembler.copyToEntity(request, tarefa);
        return assembler.toModel(service.salvar(tarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        var tarefa = service.buscarPorId(id);
        service.remover(tarefa.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/concluir/{id}")
    public ResponseEntity<Void> concluir(@PathVariable Long id) {

        var tarefa = service.buscarPorId(id);
        tarefa.limparDataConclusao();
        tarefa.concluir();
        service.salvar(tarefa);
        return ResponseEntity.noContent().build();
    }
}
