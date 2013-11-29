--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: aposta; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE aposta (
    id_bolao bigint,
    id bigint NOT NULL,
    numero smallint NOT NULL,
    creation_date timestamp with time zone,
    edit_date timestamp with time zone
);


--
-- Name: bolao; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE bolao (
    id bigint NOT NULL,
    id_concurso bigint,
    id_grupo bigint,
    valor_depositado double precision,
    creation_date timestamp with time zone,
    edit_date timestamp with time zone
);


--
-- Name: concurso; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE concurso (
    valor_sorteado double precision,
    codigo_identificador integer,
    data_sorteio date,
    creation_date timestamp with time zone,
    edit_date timestamp with time zone,
    id bigint NOT NULL,
    n1 smallint,
    n2 smallint,
    n3 smallint,
    n4 smallint,
    n5 smallint,
    n6 smallint
);


--
-- Name: grupo; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE grupo (
    id bigint NOT NULL,
    id_admin bigint,
    nome text,
    saldo double precision,
    creation_date timestamp with time zone,
    edit_date timestamp with time zone
);


--
-- Name: grupo_participante; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE grupo_participante (
    id_participante bigint NOT NULL,
    id_grupo bigint NOT NULL,
    creation_date timestamp with time zone
);


--
-- Name: pagamento; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE pagamento (
    id_bolao bigint NOT NULL,
    id_participante bigint NOT NULL,
    valor double precision,
    creation_date timestamp without time zone NOT NULL
);


--
-- Name: participante; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE participante (
    id bigint NOT NULL,
    id_usuario bigint,
    nome text,
    creation_date timestamp with time zone,
    edit_date timestamp with time zone
);


--
-- Name: seq_aposta; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_aposta
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_bolao; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_bolao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_concurso; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_concurso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_grupo; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_grupo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_participante; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_participante
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE usuario (
    id bigint NOT NULL,
    email text,
    senha text,
    creation_date timestamp with time zone,
    edit_date timestamp with time zone
);


--
-- Name: vw_nums_concurso; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW vw_nums_concurso AS
 SELECT p.id, 
    p.numero
   FROM (        (        (        (        (         SELECT concurso.id, 
                                                    concurso.n1 AS numero
                                                   FROM concurso
                                        UNION 
                                                 SELECT concurso.id, 
                                                    concurso.n2
                                                   FROM concurso)
                                UNION 
                                         SELECT concurso.id, 
                                            concurso.n3
                                           FROM concurso)
                        UNION 
                                 SELECT concurso.id, 
                                    concurso.n4
                                   FROM concurso)
                UNION 
                         SELECT concurso.id, 
                            concurso.n5
                           FROM concurso)
        UNION 
                 SELECT concurso.id, 
                    concurso.n6
                   FROM concurso) p
  WHERE (p.numero IS NOT NULL);


--
-- Name: aposta_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY aposta
    ADD CONSTRAINT aposta_pkey PRIMARY KEY (id, numero);


--
-- Name: bolao_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY bolao
    ADD CONSTRAINT bolao_pkey PRIMARY KEY (id);


--
-- Name: concurso_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY concurso
    ADD CONSTRAINT concurso_pkey PRIMARY KEY (id);


--
-- Name: grupo_participante_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY grupo_participante
    ADD CONSTRAINT grupo_participante_pkey PRIMARY KEY (id_participante, id_grupo);


--
-- Name: grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY grupo
    ADD CONSTRAINT grupo_pkey PRIMARY KEY (id);


--
-- Name: participante_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY participante
    ADD CONSTRAINT participante_pkey PRIMARY KEY (id);


--
-- Name: pk_pagamento; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY pagamento
    ADD CONSTRAINT pk_pagamento PRIMARY KEY (id_bolao, id_participante, creation_date);


--
-- Name: unique_id_usuario; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY participante
    ADD CONSTRAINT unique_id_usuario UNIQUE (id_usuario);


--
-- Name: unique_nome; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY grupo
    ADD CONSTRAINT unique_nome UNIQUE (nome);


--
-- Name: usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- Name: aposta_id_bolao_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY aposta
    ADD CONSTRAINT aposta_id_bolao_fkey FOREIGN KEY (id_bolao) REFERENCES bolao(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: bolao_id_concurso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bolao
    ADD CONSTRAINT bolao_id_concurso_fkey FOREIGN KEY (id_concurso) REFERENCES concurso(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: bolao_id_grupo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bolao
    ADD CONSTRAINT bolao_id_grupo_fkey FOREIGN KEY (id_grupo) REFERENCES grupo(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_id_participante; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY grupo_participante
    ADD CONSTRAINT fk_id_participante FOREIGN KEY (id_participante) REFERENCES participante(id_usuario);


--
-- Name: fk_id_usuario_grupo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY grupo
    ADD CONSTRAINT fk_id_usuario_grupo FOREIGN KEY (id_admin) REFERENCES participante(id_usuario);


--
-- Name: grupo_participante_id_grupo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY grupo_participante
    ADD CONSTRAINT grupo_participante_id_grupo_fkey FOREIGN KEY (id_grupo) REFERENCES grupo(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: pagamento_id_bolao_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY pagamento
    ADD CONSTRAINT pagamento_id_bolao_fkey FOREIGN KEY (id_bolao) REFERENCES bolao(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: pagamento_id_participante_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY pagamento
    ADD CONSTRAINT pagamento_id_participante_fkey FOREIGN KEY (id_participante) REFERENCES participante(id_usuario);


--
-- Name: participante_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY participante
    ADD CONSTRAINT participante_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

