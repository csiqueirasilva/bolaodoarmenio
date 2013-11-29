/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.jogos;

import br.edu.infnet.DBConfig;
import br.edu.infnet.exceptions.DAOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author csiqueira
 */
class ApostaDAO {
    
    TreeSet<Aposta> listar(Long id_bolao) throws DAOException {
        TreeSet<Aposta> apostas = null;
        if(id_bolao != null) {
            try {
                ArrayList<HashMap<String, Object>> results = DBConfig.runPreparedSql("SELECT DISTINCT id FROM aposta WHERE id_bolao = ?;", id_bolao);
                ComparadorAposta comparador = new ComparadorAposta();
                apostas = new TreeSet<Aposta>(comparador);
                
                for(int i = 0; i < results.size(); i++) {
                    apostas.add(this.obter((Long) results.get(i).get("id")));
                }
                
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
        return apostas;
    }
    
    Aposta obter (Long id_aposta) throws DAOException {
        Aposta aposta = null;
        if(id_aposta != null) {
            try {
                ArrayList<HashMap<String, Object>> results = DBConfig.runPreparedSql("SELECT * FROM aposta WHERE id = ?;", id_aposta);
                
                aposta = new Aposta();
                aposta.setNumeros(new TreeSet<Short>());
                aposta.setId(id_aposta);
                for(int i = 0; i < results.size(); i++) {
                    aposta.getNumeros().add((Short) results.get(i).get("numero"));
                }
                
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
        return aposta;
    }
    
    void inserirListaApostas(Aposta[] apostas) throws DAOException {
        try {
            for(int i = 0; i < apostas.length; i++) {
                inserir(apostas[i]);
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }
    
    private void inserir (Aposta aposta) throws DAOException {
        try {
            
            if(aposta == null || aposta.getBolao() == null || aposta.getNumeros() == null ||
                    aposta.getNumeros().size() < ApostaDLO.MIN_QTD_NUMERO_APOSTA ||
                    aposta.getNumeros().size() > ApostaDLO.MAX_QTD_NUMERO_APOSTA) {
                return ;
            }
            
            Long id_bolao = aposta.getBolao().getId();

            DBConfig.transactionQuery("CREATE TEMPORARY TABLE temp_id_aposta AS (SELECT nextval('seq_aposta') AS id);");
            
            Iterator<Short> it = aposta.getNumeros().iterator();
            while(it.hasNext()) {
                DBConfig.transactionQuery("INSERT INTO aposta (id, id_bolao, numero, creation_date) "
                        + "VALUES ((SELECT id FROM temp_id_aposta), ?, ?, now())", id_bolao, it.next());
            }
            
            DBConfig.transactionQuery("DROP TABLE temp_id_aposta;");
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }
}
