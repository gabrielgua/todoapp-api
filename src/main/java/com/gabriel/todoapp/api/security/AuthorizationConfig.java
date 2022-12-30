package com.gabriel.todoapp.api.security;

import com.gabriel.todoapp.domain.repository.TarefaRepository;
import com.gabriel.todoapp.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationConfig {

    @Autowired
    private TarefaRepository tarefaRepository;

    public Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();

    }

    public Long getUsuarioId() {
        var jwt = (Jwt) getAuthentication().getPrincipal();
        return Long.valueOf(jwt.getClaim("usuario_id"));
    }
    public boolean isAutenticado() {
        return getAuthentication().isAuthenticated();
    }

    public boolean temAutorizacao(String authority) {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }

    public boolean temEscopoLeitura() {
        return temAutorizacao("SCOPE_READ");
    }

    public boolean temEscopoEscrita() {
        return temAutorizacao("SCOPE_WRITE");
    }

    public boolean isAdmin() {
        return temAutorizacao("ADMIN");
    }

    public boolean isUser() {
        return temAutorizacao("USER");
    }

    public boolean isUsuarioAutenticado(Long usuarioId) {
        return
                getUsuarioId() != null
                && usuarioId != null
                && getUsuarioId().equals(usuarioId);
    }


    public boolean podeListarUsuarios() {
        return temEscopoLeitura() && isAdmin();
    }

    public boolean podeConsultarUsuarios(Long usuarioId) {
        return temEscopoLeitura() && (isAdmin() || isUsuarioAutenticado(usuarioId));
    }

    public boolean podeGerenciarUsuarios(Long usuarioId) {
        return temEscopoEscrita() && (isAdmin() || isUsuarioAutenticado(usuarioId));
    }

    public boolean podeListarTodasAsTarefas() {
        return temEscopoLeitura() && isAdmin();
    }

    public boolean podeConsultarTarefa(Long tarefaId) {
        return temEscopoLeitura() && (isAdmin() || isAutorDaTarefa(tarefaId, getUsuarioId()));
    }

    public boolean podeAdicionarTarefas() {
        return temEscopoEscrita() && isAutenticado();
    }

    public boolean podeGerenciarTarefas(Long tarefaId) {
        return temEscopoEscrita() && (isAdmin() || isAutorDaTarefa(tarefaId, getUsuarioId()));
    }

    public boolean isAutorDaTarefa(Long tarefaId, Long usuarioId) {
        return tarefaRepository.isUsuario(tarefaId, usuarioId);
    }
}
