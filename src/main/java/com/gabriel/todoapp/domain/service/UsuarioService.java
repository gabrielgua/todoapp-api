package com.gabriel.todoapp.domain.service;

import com.gabriel.todoapp.domain.exception.EntidadeEmUsoException;
import com.gabriel.todoapp.domain.exception.EntidadeNaoEncontradaException;
import com.gabriel.todoapp.domain.exception.NegocioException;
import com.gabriel.todoapp.domain.exception.UsuarioNaoEncontradoException;
import com.gabriel.todoapp.domain.model.Tarefa;
import com.gabriel.todoapp.domain.model.Usuario;
import com.gabriel.todoapp.domain.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {

    private PasswordEncoder encoder;

    private UsuarioRepository repository;
    private TarefaService tarefaService;

    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public Usuario salvar(Usuario usuario) {

        return repository.save(usuario);
    }

    public void remover(Long id) {
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeEmUsoException(String.format("Usuário de id: '%s', está em uso e não pode ser removido.", id));
        }
    }

    public void alterarSenha(Long id, String senhaAtual, String senhaNova) {
        Usuario usuario = buscarPorId(id);
        if (!encoder.matches(senhaAtual, usuario.getSenha())) {
            throw new NegocioException("Senha informada não coincide");
        }
        usuario.setSenha(encoder.encode(senhaNova));
        repository.save(usuario);
    }

    public List<Tarefa> buscarTarefas(Long id) {
        return tarefaService.buscarPorUsuarioId(id);
    }
}
