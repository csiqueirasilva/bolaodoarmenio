/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.jogos;

import br.edu.infnet.DBConfig;
import br.edu.infnet.concursos.ConcursoDLO;
import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.exceptions.DLOException;
import br.edu.infnet.grupos.GrupoDLO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author csiqueira
 */
class BolaoDAO {

    private Bolao dbResultToClass(HashMap<String, Object> result) throws DLOException {
        Bolao bolao = new Bolao();
        bolao.setId((Long) result.get("id"));
        bolao.setGrupo(GrupoDLO.obter((Long) result.get("id_grupo")));
        bolao.setConcurso(ConcursoDLO.obterConcurso((Long) result.get("id_concurso")));
        bolao.setValorDepositado(new Float((Double) result.get("valor_depositado")));
        // NYI
        //bolao.setListaApostas(ApostaDLO.listarApostas((Long) result.get("id")));
        return bolao;
    }

    void atualizar(Bolao bolao, Aposta[] apostas) throws DAOException {
        if (bolao == null) {
            throw new DAOException("Não há bolão para atualização.");
        } else if (apostas == null) {
            throw new DAOException("Não há lista de apostas para atualização.");
        }

        try {
            DBConfig.initTransaction();

            new ApostaDAO().inserirListaApostas(apostas);

            DBConfig.transactionQuery("UPDATE bolao SET "
                    + "valor_depositado = ?, edit_date = now()"
                    + "WHERE id = ?;",
                    bolao.valorDepositado,
                    bolao.id);
            DBConfig.endTransaction();
        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

    void registrarPagamento(Long id_participante, Long id_bolao, Float valorDepositado) throws DAOException {
        if (id_participante == null) {
            throw new DAOException("ID participante nulo");
        } else if (id_bolao == null) {
            throw new DAOException("ID bolao nulo");
        } else if (valorDepositado == null) {
            throw new DAOException("valor depositado nulo");
        }

        try {
            DBConfig.initTransaction();
            DBConfig.transactionQuery("UPDATE bolao SET "
                    + "valor_depositado = valor_depositado + ?, edit_date = now() "
                    + "WHERE id = ?;",
                    valorDepositado,
                    id_bolao);
            DBConfig.transactionQuery("INSERT INTO pagamento (id_bolao, id_participante, creation_date, valor) "
                    + "VALUES (?, ?, now(), ?);",
                    id_bolao,
                    id_participante,
                    valorDepositado);
            DBConfig.endTransaction();
        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

    void inserir(Bolao bolao) throws DAOException {
        if (bolao == null) {
            throw new DAOException("Variavel bolao esta nula");
        } else if (bolao.getGrupo() == null
                || bolao.getConcurso() == null
                || bolao.getValorDepositado() == null) {
            throw new DAOException("Variavel bolao nao possui grupo ou concurso");
        }

        try {
            DBConfig.initTransaction();
            DBConfig.transactionQuery("INSERT INTO bolao (id, id_concurso, id_grupo, "
                    + "valor_depositado, creation_date) "
                    + "VALUES (nextval('seq_bolao'), ?, ?, ? + (SELECT saldo FROM grupo WHERE id = ?), now());",
                    bolao.getConcurso().getId(),
                    bolao.getGrupo().getId(),
                    bolao.getValorDepositado(),
                    bolao.getGrupo().getId());
            DBConfig.transactionQuery("UPDATE grupo SET saldo = 0 WHERE id = ?", bolao.getGrupo().getId());
            DBConfig.endTransaction();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    void transferirSaldoBolaoParaGrupo() throws DAOException {
        try {
            ArrayList<HashMap<String, Object>> results = DBConfig
                    .runPreparedSql("SELECT bolao.id, bolao.id_concurso, bolao.id_grupo, bolao.valor_depositado "
                    + "FROM bolao, concurso "
                    + "AND bolao.id_concurso = concurso.id "
                    + "AND concurso.codigo_identificador IS NOT NULL "
                    + "AND valor_depositado <> 0;");

            DBConfig.initTransaction();

            for (int i = 0; i < results.size(); i++) {
                HashMap<String, Object> line = results.get(i);
                DBConfig.transactionQuery("UPDATE grupo SET saldo = saldo + ?, edit_date = now() WHERE id = ?", line.get("valor_depositado"), line.get("id_grupo"));
                DBConfig.transactionQuery("UPDATE bolao SET valor_depositado = 0, edit_date = now() WHERE id = ?", line.get("id"));
            }

            DBConfig.endTransaction();

        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    Bolao obterPorGrupo(Long id_grupo) throws DAOException {
        Bolao bolao = null;
        try {
            ArrayList<HashMap<String, Object>> results = DBConfig
                    .runPreparedSql("SELECT bolao.id, bolao.id_concurso, bolao.id_grupo, bolao.valor_depositado "
                    + "FROM bolao, concurso "
                    + "WHERE bolao.id_grupo = ? "
                    + "AND bolao.id_concurso = concurso.id "
                    + "AND concurso.codigo_identificador IS NULL;", id_grupo);
            if (results.size() == 1) {
                bolao = dbResultToClass(results.get(0));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return bolao;
    }

    Bolao obter(Long id_bolao) throws DAOException {
        Bolao bolao = null;
        try {
            ArrayList<HashMap<String, Object>> results = DBConfig
                    .runPreparedSql("SELECT bolao.id, bolao.id_concurso, bolao.id_grupo, bolao.valor_depositado "
                    + "FROM bolao "
                    + "WHERE bolao.id = ? ", id_bolao);
            if (results.size() == 1) {
                bolao = dbResultToClass(results.get(0));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return bolao;
    }

    List<Bolao> listarBolaoAberto(Long id_participante) throws DAOException {
        List<Bolao> boloes = null;
        try {
            ArrayList<HashMap<String, Object>> results = DBConfig
                    .runPreparedSql("SELECT grupo.nome, bolao.id, bolao.id_concurso, bolao.id_grupo, bolao.valor_depositado "
                    + "FROM bolao, concurso, grupo, grupo_participante "
                    + "WHERE grupo_participante.id_participante = ? "
                    + "AND grupo_participante.id_grupo = grupo.id "
                    + "AND grupo_participante.id_grupo = bolao.id_grupo "
                    + "AND bolao.id_concurso = concurso.id "
                    + "AND concurso.codigo_identificador IS NULL "
                    + "ORDER BY grupo.nome ASC ;", id_participante);
            boloes = new ArrayList<Bolao>();
            for (int i = 0; i < results.size(); i++) {
                Bolao bolao = dbResultToClass(results.get(i));
                TreeSet<Aposta> apostas = ApostaDLO.listar(bolao.id);
                bolao.listaApostas = apostas.toArray(new Aposta[apostas.size()]);
                boloes.add(bolao);
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return boloes;
    }

    List<Bolao> listar(Long id_grupo) throws DAOException {
        List<Bolao> boloes = null;
        try {
            ArrayList<HashMap<String, Object>> results = DBConfig
                    .runPreparedSql("SELECT id, id_concurso, id_grupo, valor_depositado "
                    + "FROM bolao "
                    + "WHERE id_grupo = ?;", id_grupo);
            boloes = new ArrayList<Bolao>();
            for (int i = 0; i < results.size(); i++) {
                boloes.add(dbResultToClass(results.get(i)));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return boloes;
    }
}
