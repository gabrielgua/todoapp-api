package com.gabriel.todoapp.domain.repository;

import com.gabriel.todoapp.domain.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    Optional<List<Tarefa>> findByUsuarioId(Long id);
    boolean isUsuario(Long tarefaId, Long usuarioId);
}
