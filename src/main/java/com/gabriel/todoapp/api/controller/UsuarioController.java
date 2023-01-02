package com.gabriel.todoapp.api.controller;

import com.gabriel.todoapp.api.model.*;
import com.gabriel.todoapp.api.model.assembler.TarefaAssembler;
import com.gabriel.todoapp.api.model.assembler.UsuarioAssembler;
import com.gabriel.todoapp.api.security.CheckSecurity;
import com.gabriel.todoapp.domain.model.TipoUsuario;
import com.gabriel.todoapp.domain.model.Usuario;
import com.gabriel.todoapp.domain.service.UsuarioService;
import lombok.AllArgsConstructor;
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

    @CheckSecurity.Usuarios.CanListUsuarios
    @GetMapping
    public List<UsuarioResponse> listar() {
        return assembler.toCollectionList(service.listar());
    }

    @CheckSecurity.Usuarios.CanSearchUsuario
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }


    @CheckSecurity.Usuarios.CanSearchUsuario
    @GetMapping("/{id}/tarefas")
    public List<TarefaResponse> buscarTarefasDoUsuario(@PathVariable Long id) {
        return tarefaAssembler.toCollectionList(service.buscarTarefas(id));
    }


    @CheckSecurity.Usuarios.CanWriteUsuarios
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse salvar(@Valid @RequestBody UsuarioComSenhaRequest request) {
        Usuario usuario = assembler.toEntity(request);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setTipo(TipoUsuario.USER);
        return assembler.toModel(service.salvar(usuario));
    }

    @CheckSecurity.Usuarios.CanManageUsuarios
    @PutMapping("/{id}")
    public UsuarioResponse editar(@Valid @RequestBody UsuarioRequest request, @PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        assembler.copyToEntity(request, usuario);
        return assembler.toModel(service.salvar(usuario));
    }

    @CheckSecurity.Usuarios.CanManageUsuarios
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        service.remover(usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Usuarios.CanManageUsuarios
    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @Valid @RequestBody NovaSenha senhaRequest) {
        service.alterarSenha(id, senhaRequest.getSenhaAtual(), senhaRequest.getSenhaNova());
        return ResponseEntity.noContent().build();
    }
}
