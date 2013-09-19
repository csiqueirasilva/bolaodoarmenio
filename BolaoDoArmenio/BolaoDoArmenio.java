package BolaoDoArmenio;

import java.io.File;
import java.util.List;

import BolaoDoArmenio.concursos.Concurso;
import BolaoDoArmenio.concursos.ConcursoDLO;

public class BolaoDoArmenio {	
	public static void main (String args[]) {
		/*Usuario usuario = new Usuario(1);
		System.out.println(usuario.getEmail());
		System.out.println(usuario.getSenha());*/
		ConcursoDLO.carregarBaseDeConcursos();
	}
}