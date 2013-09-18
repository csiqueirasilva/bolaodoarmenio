package BolaoDoArmenio.jogos;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ConcursoDLO {
	
	static void listarNumerosMaisSorteados() {
	}

	public static List<Concurso> carregarBaseDeConcursos(File arq) {
		List<Concurso> listaConcursos = null;
		if (arq != null) {
			Document doc = null ;
			try {
				doc = Jsoup.parse(arq, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			Elements linhasSorteio = doc.select("table tbody tr");
			listaConcursos = new ArrayList<Concurso>();		
			SimpleDateFormat dtFormat = new SimpleDateFormat("DD/MM/YYYY");

			for(int i = 1; i < linhasSorteio.size(); i++ ) {
				Elements tds = linhasSorteio.get(i).select("td");
				Concurso conc = new Concurso();
				conc.setCodigoIdentificador(Integer.parseInt(tds.get(0).text()));
				try {
					conc.setDataSorteio(dtFormat.parse(tds.get(1).text()));
				} catch (ParseException e) {
					e.printStackTrace();
					return null ;
				}
				List<Integer> listaNumeros = new ArrayList<Integer>();
				for(int j = 2; j < 8; j++) {
					listaNumeros.add(new Integer(tds.get(j).text()));					
				}
				conc.setNumerosSorteio(listaNumeros);
				conc.setValorSorteado(10000000.0);
				listaConcursos.add(conc);
			}
			ConcursoDAO c = new ConcursoDAO();
			c.carregarRegistros(listaConcursos);
		}
		return listaConcursos;
	}
}