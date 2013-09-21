package br.edu.infnet.usuarios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import br.edu.infnet.DBConfig;
import br.edu.infnet.exceptions.DAOException;

class UsuarioDAO {
	Usuario obter(int id) throws DAOException {

		try {
			ArrayList<HashMap<String, Object>> uDb = null;
			uDb = DBConfig
					.runSql("SELECT id, email, senha, date_format(creation_date, '%Y-%m-%d %H:%i:%s') as creation_date, date_format(edit_date, '%Y-%m-%d %H:%i:%s') as edit_date FROM usuario WHERE id = "
							+ id + ";");

			if (uDb.toArray().length == 0) {
				return null;
			}

			HashMap<String, Object> linha = uDb.get(0);
			Usuario usuario = new Usuario();

			usuario.id = (Long) linha.get("id");

			SimpleDateFormat format_date = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");

			usuario.id = (Long) linha.get("id");
			usuario.creation_date = linha.get("creation_date") != null ? format_date
					.parse((String) linha.get("creation_date")) : null;
			usuario.edit_date = linha.get("e_date") != null ? format_date
					.parse((String) linha.get("edit_date")) : null;
			usuario.email = (String) linha.get("email");
			usuario.senha = (String) linha.get("senha");
			return usuario;
			
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	void inserir(Usuario usr) throws DAOException {
		if (usr == null) {
			return;
		}
		try {
			DBConfig.runPreparedSql("INSERT INTO usuarios (email, senha, creation_date) VALUES (?,?,creation_date=now());", usr.email, usr.senha);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	void remover(Usuario usr) throws DAOException {
		if (usr == null) {
			return;
		}
		try {
			DBConfig.runSql("DELETE FROM usuarios WHERE id = " + usr.id + ";");
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	void alterar(Usuario usr) throws DAOException {
		if (usr == null) {
			return;
		}
		try {
			DBConfig.runPreparedSql("UPDATE usuarios SET email = ?, senha = ?, edit_date = now() WHERE id = ?;", usr.email, usr.senha, usr.id );
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}