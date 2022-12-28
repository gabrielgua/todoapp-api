alter table tarefa add constraint fk_tarefa_usuario_id
foreign key (usuario_id) references usuario (id);
