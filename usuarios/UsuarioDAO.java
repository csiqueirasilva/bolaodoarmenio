package BolaoDoArmenio.usuarios;

import java.util.ArrayList;
import java.util.HashMap;

import BolaoDoArmenio.DBConfig;

public class UsuarioDAO extends DBConfig {
	public Usuario obter(Usuario usr) {
			ArrayList<HashMap<String,Object>> uDb = this.runSql("SELECT * FROM usuarios WHERE id = "+usr.id+";") ;
			return uDb.toArray().length == 0 ? null : new Usuario((String) uDb.get(0).get("email"),(String) uDb.get(0).get("senha"));
	}
	
	public void inserir(Usuario usr) {
		this.runSql("INSERT INTO usuarios (id, email, senha) VALUES (nextval('seq_usuarios'),$$"+usr.email+"$$,$$"+usr.senha+"$$;");
	}
	
	
	public void remover(Usuario usr) {
		this.runSql("DELETE FROM usuarios WHERE id = "+usr.id+";");
	}
	
	public void alterar(Usuario usr) {
		this.runSql("UPDATE usuarios SET email = $$"+usr.email+"$$, senha = $$"+usr.senha+"$$ WHERE id = "+usr.id+";");
	}
}