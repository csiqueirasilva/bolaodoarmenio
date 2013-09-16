package BolaoDoArmenio.usuarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import BolaoDoArmenio.DBConfig;

public class UsuarioDAO {
	public Usuario obter(int id) {
			DBConfig.init();
			ArrayList<HashMap<String,Object>> uDb = DBConfig.runSql("SELECT *, to_char(creation_date, 'YYYY-MM-DD HH24:MI:SS') as c_date, to_char(creation_date, 'YYYY-MM-DD HH24:MI:SS') as e_date FROM usuarios WHERE id = "+id+";") ;
			DBConfig.end();
			
			if(uDb.toArray().length == 0)
			{
				return null ;
			}
			
			HashMap<String,Object> linha = uDb.get(0);
			Usuario usuario = new Usuario();
			SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			usuario.setId((long)linha.get("id"));
			
			try {
				usuario.setCreationDate(format_date.parse((String) linha.get("c_date")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			try {
				usuario.setEditDate(format_date.parse((String) linha.get("e_date")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			usuario.setEmail((String)linha.get("email"));
			usuario.setSenha((String)linha.get("senha"));
			
			return usuario ;
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