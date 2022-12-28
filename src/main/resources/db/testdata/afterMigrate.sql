set foreign_key_checks = 0;
delete from usuario;
delete from tarefa;
set foreign_key_checks = 1;

alter table usuario auto_increment = 1;
alter table tarefa auto_increment = 1;

insert into usuario (id, nome, email, senha, tipo) values
(1, "Gabriel Guaitanele Niszczak", "gabriel.adm@mail.com", "$2a$12$M0LrIpSy0pa.pAu9oG8Q3eWXIiRuvEbNEmVICa1dnakHMNzNYq8qW", "ADMIN"),
(2, "Luna Brenda de Paula", "luna.brenda@mail.com", "$2a$12$M0LrIpSy0pa.pAu9oG8Q3eWXIiRuvEbNEmVICa1dnakHMNzNYq8qW", "USER"),
(3, "Henrique Bento Otávio Vieira", "henrique.bento@mail.com", "$2a$12$M0LrIpSy0pa.pAu9oG8Q3eWXIiRuvEbNEmVICa1dnakHMNzNYq8qW", "USER");

insert into tarefa (id, titulo, concluida, data_criacao, data_conclusao, usuario_id) values
(1, "Limpar o PC.", true, utc_timestamp, utc_timestamp, 1),
(2, "Fazer janta.", false, utc_timestamp, null, 1),
(3, "Estudar Programação.", false, utc_timestamp, null, 2);







