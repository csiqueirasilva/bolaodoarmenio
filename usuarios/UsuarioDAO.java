package BolaoDoArmenio.usuarios;

import BolaoDoArmenio.DBConfig;
import java.util.ArrayList;
import java.util.HashMap;

public class UsuarioDAO {
	public Usuario obter(int id) {
			DBConfig.init();
			ArrayList<HashMap<String,Object>> uDb = DBConfig.runSql("SELECT * FROM usuarios WHERE id = "+id+";") ;
			DBConfig.end();
			return uDb.toArray().length == 0 ? null : new Usuario((String) uDb.get(0).get("email"),(String) uDb.get(0).get("senha"));
	}
	
	public void inserir(Usuario usr) {
		DBConfig.init();
		DBConfig.runSql("INSERT INTO usuarios (id, email, senha) VALUES (nextval('seq_usuarios'),$$"+usr.email+"$$,$$"+usr.senha+"$$;");
		DBConfig.end();
	}	
	
	public void remover(Usuario usr) {
		DBConfig.init();
		DBConfig.runSql("DELETE FROM usuarios WHERE id = "+usr.id+";");
		DBConfig.end();
	}
	
	public void alterar(Usuario usr) {
		DBConfig.init();
		DBConfig.runSql("UPDATE usuarios SET email = $$"+usr.email+"$$, senha = $$"+usr.senha+"$$ WHERE id = "+usr.id+";");
		DBConfig.end();
	}
}