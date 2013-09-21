package br.edu.infnet.concursos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import br.edu.infnet.exceptions.DLOException;

public class ConcursoDLO {

	private static File unzipListaResultados(File file, boolean deleteOnExit) throws DLOException {
		File megaSenaFile = null;
		try {
			String tDir = System.getProperty("java.io.tmpdir");
			ZipFile zip = new ZipFile(file);

			megaSenaFile = new File(tDir + "bolao.htm");
			if (deleteOnExit) {
				megaSenaFile.deleteOnExit();
			}
			InputStream zis = zip.getInputStream(zip.getEntry("D_MEGA.HTM"));

			byte[] buffer = new byte[2048];
			OutputStream os = null;
			os = new FileOutputStream(megaSenaFile);

			int len = 0;
			while ((len = zis.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}

			os.flush();
			os.close();
			zis.close();
			zip.close();
		} catch (Exception e) {
			throw new DLOException(e);
		}
		return megaSenaFile;
	}

	private static File downloadListaResultados() throws DLOException {
		File megaSenaFile = null;
		try {
			URL url = new URL("http://hawaii-ocean.dyndns.tv/D_megase.zip");
			String tDir = System.getProperty("java.io.tmpdir");
			String path = tDir + "base_bolao_do_armenio.zip";
			megaSenaFile = new File(path);
			megaSenaFile.deleteOnExit();

			FileUtils.copyURLToFile(url, megaSenaFile);

		} catch (Exception e) {
			throw new DLOException(e);
		}
		return megaSenaFile;
	}

	private static void parseHTMLMegaSena(File megaSenaFile) throws DLOException {
		if (megaSenaFile != null) {

			Document doc = null;
			try {
				doc = Jsoup.parse(megaSenaFile, "UTF-8");

				Elements linhasSorteio = doc.select("table tbody tr");
				List<Concurso> listaConcursos = new ArrayList<Concurso>();
				SimpleDateFormat dtFormat = new SimpleDateFormat("DD/MM/YYYY");

				ConcursoDAO c = new ConcursoDAO();
				long qtdRegistrosDb = c.contarRegistros();

				for (long i = qtdRegistrosDb + 1; i < linhasSorteio.size(); i++) {
					Elements tds = linhasSorteio.get((int) i).select("td");
					Concurso conc = new Concurso();
					conc.setCodigoIdentificador(Integer.parseInt(tds.get(0)
							.text()));
					conc.setDataSorteio(dtFormat.parse(tds.get(1).text()));
					List<Integer> listaNumeros = new ArrayList<Integer>();
					for (int j = 2; j < 8; j++) {
						listaNumeros.add(new Integer(tds.get(j).text()));
					}
					conc.setNumerosSorteio(listaNumeros);
					conc.setValorSorteado(10000000.0);
					listaConcursos.add(conc);
				}
				if (listaConcursos.size() > 0) {
					c.carregarRegistros(listaConcursos);
				}
			} catch (Exception e) {
				throw new DLOException(e);
			}
		}
	}

	static void listarNumerosMaisSorteados()  throws DLOException {
	}

	public static void carregarBaseDeConcursos(String nome_arquivo) throws DLOException {
		if (nome_arquivo != null) {
			carregarBaseDeConcursos(new File(nome_arquivo));
		}
	}

	public static void carregarBaseDeConcursos(File arquivoLocal) throws DLOException {
		if (arquivoLocal != null) {
			File megaSenaFile = unzipListaResultados(arquivoLocal, false);
			parseHTMLMegaSena(megaSenaFile);
		}
	}

	public static void carregarBaseDeConcursos() throws DLOException {
		File zipMegaSena = downloadListaResultados();
		File megaSenaFile = unzipListaResultados(zipMegaSena, true);
		parseHTMLMegaSena(megaSenaFile);
	}

}