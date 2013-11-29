package br.edu.infnet.usuarios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import br.edu.infnet.DBConfig;
import br.edu.infnet.exceptions.DAOException;
import java.text.DateFormat;

class UsuarioDAO {
        private Usuario parseUsuario (HashMap<String, Object> linha) throws Exception {
            Usuario usuario = new Usuario();

            usuario.id = (Long) linha.get("id");

            SimpleDateFormat format_date = new SimpleDateFormat(DBConfig.DATE_PARSE_STRING);

            usuario.id = (Long) linha.get("id");
            usuario.creation_date = linha.get("creation_date") != null ? format_date.parse(linha.get("creation_date").toString()) : null;
            usuario.edit_date = linha.get("e_date") != null ? format_date.parse(linha.get("creation_date").toString()) : null;
            usuario.email = (String) linha.get("email");
            usuario.senha = (String) linha.get("senha");
            
            return usuario;
        } 
    
	Usuario obter(int id) throws DAOException {
		try {
			ArrayList<HashMap<String, Object>> uDb = null;
			Usuario usr = null;
                        uDb = DBConfig
					.runSql("SELECT id, email, senha, creation_date, edit_date FROM usuario WHERE id = "
							+ id + ";");

			if (uDb.toArray().length != 0) {
				usr = parseUsuario(uDb.get(0));
			}

                        return usr;

		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	Usuario obter(Usuario ext) throws DAOException {
		try {
			ArrayList<HashMap<String, Object>> uDb = null;
                        Usuario usr = null;
			uDb = DBConfig
					.runPreparedSql("SELECT id, email, senha, creation_date, edit_date FROM usuario WHERE email = ? AND senha = ?;", ext.email, ext.senha );

			if (uDb.toArray().length != 0) {
				usr = parseUsuario(uDb.get(0));
			}

                        return usr;
			
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}	
	
	void inserir(Usuario usr) throws DAOException {
		if (usr == null) {
			return;
		}
		try {
			DBConfig.runPreparedSql("INSERT INTO usuario (id, email, senha, creation_date) VALUES (nextval('seq_usuario'),?,?,now())", usr.email, usr.senha);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	void remover(Usuario usr) throws DAOException {
		if (usr == null) {
			return;
		}
		try {
			DBConfig.runSql("DELETE FROM usuario WHERE id = " + usr.id + ";");
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	void alterar(Usuario usr) throws DAOException {
		if (usr == null) {
			return;
		}
		try {
			DBConfig.runPreparedSql("UPDATE usuario SET email = ?, senha = ?, edit_date = now() WHERE id = ?;", usr.email, usr.senha, usr.id );
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
        
        boolean verificarEmail (String email) throws DAOException {
            boolean ret = false ;
            if (email != null) {
                try {
                    ArrayList<HashMap<String, Object>> arr = DBConfig.runPreparedSql("SELECT count(1) FROM usuario WHERE LOWER(email) = LOWER(?)", email);
                    if((Long) arr.get(0).get("count") == 1) {
                        ret = true ;
                    }
                } catch (Exception e) {
                    throw new DAOException(e);
                }
            }
            return ret ;
        }
}