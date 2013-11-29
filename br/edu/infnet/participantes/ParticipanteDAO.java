package br.edu.infnet.participantes;

import java.util.ArrayList;
import java.util.HashMap;

import br.edu.infnet.DBConfig;
import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.usuarios.UsuarioDLO;
import br.edu.infnet.usuarios.Usuario;

class ParticipanteDAO {
	public Participante obter (Long id) throws DAOException {
		try {
			Usuario usr = UsuarioDLO.carregar(id);
                        Participante part = new Participante();
			ArrayList<HashMap<String, Object>> arr = DBConfig.runPreparedSql("SELECT nome FROM participante WHERE id_usuario = ?", id);
			part.setId(usr.getId());
                        part.setEmail(usr.getEmail());
                        part.setSenha(usr.getSenha());
                        part.setNome((String) arr.get(0).get("nome"));
			return part ;			
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	public void inserir (Participante part) throws DAOException {
		if (part == null) {
			return ;
		}
		try {
			UsuarioDLO.salvar(part);
			part.setId(UsuarioDLO.obterId(part));
			DBConfig.runPreparedSql("INSERT INTO participante (nome, id_usuario, creation_date, id) VALUES ( ? , ?, now(), nextval('seq_participante') )", part.nome, part.getId());
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}