package br.edu.infnet.usuarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import br.edu.infnet.DBConfig;

class UsuarioDAO {
	Usuario obter(int id) {
		ArrayList<HashMap<String, Object>> uDb = DBConfig
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

		try {
			usuario.creation_date = linha.get("creation_date") != null ? format_date.parse((String) linha.get("creation_date")) : null;
		} catch (ParseException e) {
			e.printStackTrace();
			usuario.creation_date = null;
		}

		try {
			usuario.edit_date = linha.get("e_date") != null ? format_date.parse((String) linha.get("edit_date")) : null;
		} catch (ParseException e) {
			e.printStackTrace();
			usuario.edit_date = null;
		}

		usuario.email = (String) linha.get("email");
		usuario.senha = (String) linha.get("senha");

		return usuario;
	}

	void inserir(Usuario usr) {
		if (usr == null) {
			return;
		}
		DBConfig.runSql("INSERT INTO usuarios (id, email, senha) VALUES (nextval('seq_usuarios'),'"
				+ usr.email + "','" + usr.senha + "',creation_date=now());");
	}

	void remover(Usuario usr) {
		if (usr == null) {
			return;
		}
		DBConfig.runSql("DELETE FROM usuarios WHERE id = " + usr.id + ";");
	}

	void alterar(Usuario usr) {
		if (usr == null) {
			return;
		}
		DBConfig.runSql("UPDATE usuarios SET email = '" + usr.email
				+ "', senha = '" + usr.senha + "' WHERE id = " + usr.id
				+ ";");
	}
}