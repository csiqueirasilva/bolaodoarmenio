package BolaoDoArmenio.concursos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ConcursoDLO {
	
	static void listarNumerosMaisSorteados() {
	}

	public static void carregarBaseDeConcursos() {
			
			URL url = null;
			
			try {
				url = new URL("http://www1.caixa.gov.br/loterias/_arquivos/loterias/D_mgsasc.zip");
			} catch (MalformedURLException e2) {
				e2.printStackTrace();
			}
			
			String tDir = System.getProperty("java.io.tmpdir");
			String path = tDir + "base_bolao_do_armenio" + ".zip";
			File file = new File(path);
			file.deleteOnExit();
			
			ReadableByteChannel rbc;
			try {
				rbc = Channels.newChannel(url.openStream());
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
				return ;
			}
			
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
			} catch (FileNotFoundException e2) {
			}
			
			try {
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return ;
			}
			
			ZipFile zip;
			try {
				zip = new ZipFile(file);
			} catch (IOException e) {
				e.printStackTrace();
				return ;
			}
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while(entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				System.out.println(entry.getName());
			}
			
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
				return ;
			}
			
			
/*			
			Document doc = null ;
			try {
				doc = Jsoup.parse(, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				return ;
			}
			
			Elements linhasSorteio = doc.select("table tbody tr");
			List<Concurso> listaConcursos = new ArrayList<Concurso>();		
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
		}*/
	}
}