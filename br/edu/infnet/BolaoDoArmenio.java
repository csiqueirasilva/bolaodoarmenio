package br.edu.infnet;

import br.edu.infnet.concursos.ConcursoDLO;
import br.edu.infnet.exceptions.DLOException;
import br.edu.infnet.usuarios.UsuarioDLO;

public class BolaoDoArmenio {	
	public static void main (String args[]) {
		
		try {
			System.out.println(UsuarioDLO.carregar(1).getEmail());
			System.out.println(UsuarioDLO.carregar(1).getSenha());
			ConcursoDLO.carregarBaseDeConcursos("C:\\Users\\csiqueira\\Downloads\\D_megase.zip");
		} catch (DLOException e) {
			e.printStackTrace();
		}
	}
}