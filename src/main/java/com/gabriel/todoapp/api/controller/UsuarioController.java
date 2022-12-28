package com.gabriel.todoapp.api.controller;

import com.gabriel.todoapp.api.model.TarefaResponse;
import com.gabriel.todoapp.api.model.UsuarioRequest;
import com.gabriel.todoapp.api.model.UsuarioResponse;
import com.gabriel.todoapp.api.model.assembler.TarefaAssembler;
import com.gabriel.todoapp.api.model.assembler.UsuarioAssembler;
import com.gabriel.todoapp.domain.model.Tarefa;
import com.gabriel.todoapp.domain.model.TipoUsuario;
import com.gabriel.todoapp.domain.model.Usuario;
import com.gabriel.todoapp.domain.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("usuarios")
public class UsuarioController {

    private PasswordEncoder passwordEncoder;
    private UsuarioAssembler assembler;
    private TarefaAssembler tarefaAssembler;

    private UsuarioService service;

    @GetMapping
    public List<UsuarioResponse> listar() {
        return assembler.toCollectionList(service.listar());
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @GetMapping("/{id}/tarefas")
    public List<TarefaResponse> buscarTarefasDoUsuario(@PathVariable Long id) {
        return tarefaAssembler.toCollectionList(service.buscarTarefas(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse salvar(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = assembler.toEntity(request);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return assembler.toModel(service.salvar(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioResponse editar(@Valid @RequestBody UsuarioRequest request, @PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        assembler.copyToEntity(request, usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return assembler.toModel(service.salvar(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        service.remover(usuario.getId());
        return ResponseEntity.noContent().build();
    }
}