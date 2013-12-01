insert into concurso (id, creation_date()) values (nextval('seq_concurso'), now());
insert into usuario (id, email, senha) values (nextval('seq_usuario'), 'root@bolaodoarmenio.com.br', 'senha');