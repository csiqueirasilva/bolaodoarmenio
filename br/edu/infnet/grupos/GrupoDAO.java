/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.grupos;

import br.edu.infnet.DBConfig;
import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.participantes.Participante;
import br.edu.infnet.participantes.ParticipanteDLO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class GrupoDAO {
        
        List<Participante> listarNaoMembros(Grupo grupo) throws DAOException {
            List<Participante> naoParticipantes = null;
            
            if(grupo != null) {
                try {
                    ArrayList<HashMap<String,Object>> results = DBConfig.runPreparedSql("SELECT participante.nome, participante.id_usuario as id "
                            + "FROM participante "
                            + "WHERE participante.id_usuario NOT IN "
                            + "(SELECT id_participante "
                            + "FROM grupo_participante "
                            + "WHERE id_grupo = ?) "
                            + "ORDER BY nome ASC;", grupo.getId());

                    naoParticipantes = new ArrayList<Participante>();
                    
                    for(int i = 0; i < results.size(); i++) {
                        Participante part = new Participante();
                        part.setId((Long) results.get(i).get("id"));
                        part.setNome((String) results.get(i).get("nome"));
                        naoParticipantes.add(part);
                    }
                    
                } catch (Exception e) {
                    throw new DAOException(e);
                }
            }
            
            return naoParticipantes;
        }
    
        private List<Grupo> montaListaGrupos (ArrayList<HashMap<String, Object>> results) throws Exception {
                List<Grupo> grupos = new ArrayList<Grupo>();

                for(int i = 0; i < results.toArray().length; i++) {
                    HashMap<String, Object> linha = results.get(i);
                    Grupo grupo = new Grupo();
                    grupo.id = (Long) linha.get("id");
                    grupo.admin = ParticipanteDLO.obterParticipante((Long) linha.get("id_admin"));
                    grupo.nome = (String) linha.get("nome");
                    grupo.saldo = (Double) linha.get("saldo");
                    grupos.add(grupo);
                }

                return grupos;
        }
        
    	List<Grupo> listar(Long id_usuario) throws DAOException {
		try {
                        ArrayList<HashMap<String, Object>> grupoDb = null;
			grupoDb = DBConfig
					.runSql("select grupo.nome, grupo.id, grupo.id_admin, grupo.saldo from grupo_participante left join grupo on grupo_participante.id_grupo = grupo.id WHERE id_participante = "
							+ id_usuario + ";");
                        return montaListaGrupos(grupoDb);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
        
        List<Grupo> listarGrupoBolaoDisponivelCriacao (Long id_usuario) throws DAOException {
            try {
                    ArrayList<HashMap<String, Object>> grupoDb = null;
                    grupoDb = DBConfig
                                    .runSql("select grupo.nome, grupo.id, grupo.id_admin, grupo.saldo "
                            + "from grupo_participante "
                            + "left join grupo "
                            + "on grupo_participante.id_grupo = grupo.id "
                            + "WHERE id_participante = "
                                                    + id_usuario + " "
                            + "AND grupo.id NOT IN ("
                            + "SELECT id_grupo "
                            + "FROM bolao "
                            + "WHERE id_concurso IN ("
                            + "SELECT id "
                            + "FROM concurso "
                            + "WHERE codigo_identificador IS NULL)) "
                            + "ORDER BY grupo.nome ASC;");

                    return montaListaGrupos(grupoDb);
            } catch (Exception e) {
                    throw new DAOException(e);
            }
	}

	void inserir(Grupo grupo) throws DAOException {
		try {
                        if (grupo == null || grupo.admin == null ) {
                            return ;
                        }
                        
			DBConfig.initTransaction();
                        DBConfig.transactionQuery("INSERT INTO grupo (id_admin, nome, saldo, creation_date, id) VALUES ( ?, ?, ?, now(), nextval('seq_grupo') );",
                                    grupo.admin.getId(), grupo.nome, grupo.saldo == null ? 0 : grupo.saldo);
                        DBConfig.transactionQuery("INSERT INTO grupo_participante (id_grupo, id_participante, creation_date) VALUES ( (SELECT max(id) FROM grupo), ?, now());",
                                    grupo.admin.getId() );
                        DBConfig.endTransaction();
                } catch (Exception e) {
                    throw new DAOException(e);
                }
	}
        
        void adicionarMembro(Grupo grupo, Participante participante) throws DAOException {
            try {
                if (grupo == null || participante == null) {
                    return;
                }
                
                DBConfig.runPreparedSql("INSERT INTO grupo_participante (id_participante, id_grupo, creation_date) VALUES (?, ?, now());", participante.getId(), grupo.getId());
                
            } catch (Exception e) {
                
            }
        }        
        
        boolean verificarNome (String nome) throws DAOException {
            boolean ret = false;
            if (nome != null) {
                try {
                    ArrayList<HashMap<String, Object>> arr = DBConfig.runPreparedSql("SELECT count(1) FROM grupo WHERE LOWER(nome) = LOWER(?)", nome);
                    if((Long) arr.get(0).get("count") == 1) {
                        ret = true ;
                    }
                } catch (Exception e) {
                    throw new DAOException(e);
                }
            }
            return ret;
        }
        
        Grupo obterGrupo (Long id) throws DAOException {
            Grupo grupo = null;
            try {
                ArrayList<HashMap<String, Object>> results =
                        DBConfig.runPreparedSql("SELECT grupo.id, grupo.id_admin, usuario.email as admin_email, grupo.saldo, grupo.nome, participante.nome as admin "
                        + "FROM grupo, participante, usuario "
                        + "WHERE grupo.id = ? "
                        + "AND participante.id_usuario = grupo.id_admin "
                        + "AND usuario.id = participante.id_usuario;", id);
                
                Participante usuario = new Participante();
                grupo = new Grupo();
                
                usuario.setEmail((String)results.get(0).get("admin_email"));
                usuario.setId((Long)results.get(0).get("id_admin"));
                usuario.setNome((String)results.get(0).get("admin"));
                
                grupo.setAdmin(usuario);
                grupo.setId((Long) results.get(0).get("id"));
                grupo.setNome((String) results.get(0).get("nome"));
                grupo.setSaldo((Double) results.get(0).get("saldo"));
                
            } catch (Exception e) {
                
            }
            
            return grupo;
        }
        
        List<Participante> listarMembros (Long idGrupo, Long inicio, Long qtd) throws DAOException {
            List<Participante> participantes = new ArrayList<Participante>();
            if(idGrupo != null) {
                try {
                    
                    ArrayList<HashMap<String,Object>> results = DBConfig.runPreparedSql("SELECT participante.nome, usuario.email, usuario.id "
                            + "FROM grupo_participante, participante, usuario "
                            + "WHERE participante.id_usuario = grupo_participante.id_participante "
                            + "AND grupo_participante.id_grupo = ? "
                            + "AND usuario.id = participante.id_usuario "
                            + "LIMIT ? "
                            + "OFFSET ?;", idGrupo, qtd, inicio);
                    
                    for(int i = 0; i < results.size(); i++) {
                        HashMap<String,Object> linha = results.get(i);
                        Participante part = new Participante();
                        part.setNome((String) linha.get("nome"));
                        part.setEmail((String) linha.get("email"));
                        part.setId((Long) linha.get("id"));
                        participantes.add(part);
                    }
                    
                } catch (Exception e) {
                    throw new DAOException(e);
                }
            }
            return participantes;
        }
        
}
