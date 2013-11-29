package br.edu.infnet.participantes;
import br.edu.infnet.exceptions.DLOException;
import br.edu.infnet.grupos.Grupo;
import br.edu.infnet.grupos.GrupoDLO;
import java.io.Serializable;

import br.edu.infnet.usuarios.Usuario;
import java.util.List;

public class Participante extends Usuario implements Serializable {
	private static final long serialVersionUID = 2L;
	protected String nome ;
        
	public Participante (Long id) {
		this.id = id;
	}
	
	public Participante (int id) {
		this(new Long(id)) ;
	}
	
	public Participante() {		
	}
	
        public List<Grupo> getGrupos() {
            try {
                return GrupoDLO.listarGruposUsuario(this.id);
            } catch (DLOException e) {
                return null;
            }
        }
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
}