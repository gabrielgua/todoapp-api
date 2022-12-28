create table usuario (
    id bigint not null auto_increment,
    nome varchar(150) not null,
    email varchar(255) not null,
    senha varchar(255) not null,
    tipo varchar(10) not null,

    primary key(id)
) engine=InnoDB default charset=UTF8MB4;

create table tarefa (
    id bigint not null auto_increment,
    titulo varchar(150) not null,
    concluida tinyint(1) not null,
    data_criacao datetime not null,
    data_conclusao datetime,
    usuario_id bigint not null,

    primary key(id)
) engine=InnoDB default charset=UTF8MB4;
