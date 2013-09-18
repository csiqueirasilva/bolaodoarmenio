package BolaoDoArmenio;

import java.io.File;
import java.util.List;

import BolaoDoArmenio.jogos.Concurso;
import BolaoDoArmenio.jogos.ConcursoDLO;

public class BolaoDoArmenio {	
	public static void main (String args[]) {
		/*Usuario usuario = new Usuario(1);
		System.out.println(usuario.getEmail());
		System.out.println(usuario.getSenha());*/
		List<Concurso> concs = ConcursoDLO.carregarBaseDeConcursos(new File("C:/Users/csiqueira/AppData/Local/Temp/Temp1_D_megase.zip/D_MEGA.HTM"));
		System.out.println(concs.size());
	}
}