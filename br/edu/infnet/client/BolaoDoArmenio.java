package br.edu.infnet.client;


import br.edu.infnet.concursos.ConcursoDLO;
import br.edu.infnet.exceptions.DLOException;

public class BolaoDoArmenio {	
	
	public static void main (String args[]) {
		
		try {
                    ConcursoDLO.carregarBaseDeConcursos();
		} catch (DLOException e) {
			e.printStackTrace();
		}
	} 
}