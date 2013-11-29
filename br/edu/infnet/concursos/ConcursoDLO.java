package br.edu.infnet.concursos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.exceptions.DLOException;
import br.edu.infnet.jogos.BolaoDLO;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class ConcursoDLO {

    public static short QTD_NUMEROS_POR_CONCURSO = 6;
    static String WEB_SRC_LISTA_CONCURSOS = "http://localhost:8080/D_megase.zip";

    /* Dúvida se este parse deve ser feito no DAO ou no DLO 
     * Resp: No DAO. Pois está buscando uma fonte de dados dos concursos. */
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
            URL url = new URL(WEB_SRC_LISTA_CONCURSOS);
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

    private static Concurso parseLineHTML(Elements tds) throws ParseException {

        Concurso conc = new Concurso();
        SimpleDateFormat dtFormat = new SimpleDateFormat("d/M/y");

        conc.setCodigoIdentificador(Integer.parseInt(tds.get(0)
                .text()));
        conc.setDataSorteio(dtFormat.parse(tds.get(1).text()));
        List<Integer> listaNumeros = new ArrayList<Integer>();
        for (int j = 2; j < 8; j++) {
            listaNumeros.add(new Integer(tds.get(j).text()));
        }
        conc.setNumerosSorteio(listaNumeros);
        conc.setValorSorteado((float) 10000000.0);

        return conc;
    }

    private static void parseHTMLMegaSena(File megaSenaFile) throws DLOException {
        if (megaSenaFile != null) {

            Document doc = null;
            try {
                doc = Jsoup.parse(megaSenaFile, "UTF-8");

                Elements linhasSorteio = doc.select("table tbody tr");
                List<Concurso> listaConcursos = new ArrayList<Concurso>();

                ConcursoDAO c = new ConcursoDAO();
                long qtdRegistrosDb = c.contarRegistros();

                // O primeiro índice é o header da tabela
                if (qtdRegistrosDb < linhasSorteio.size()) {
                    Elements tds = linhasSorteio.get((int) qtdRegistrosDb).select("td");
                    c.atualizar(parseLineHTML(tds));
                    for (long i = qtdRegistrosDb + 1; i < linhasSorteio.size(); i++) {
                        tds = linhasSorteio.get((int) i).select("td");
                        listaConcursos.add(parseLineHTML(tds));
                    }

                    Concurso conc = new Concurso();
                    List<Integer> listaNumeros = new ArrayList<Integer>();
                    for (int j = 2; j < 8; j++) {
                        listaNumeros.add(null);
                    }

                    conc.setCodigoIdentificador(null);
                    conc.setValorSorteado(null);
                    conc.setDataSorteio(dataProximoSorteio());
                    conc.setNumerosSorteio(listaNumeros);
                    listaConcursos.add(conc);

                    c.carregarRegistros(listaConcursos);
                }

            } catch (Exception e) {
                throw new DLOException(e);
            }
        }
    }

    public static HashMap<Long, Long> contarAcertos() throws DLOException {
        try {
            return (new ConcursoDAO()).contarAcertos();
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
    
    public static Concurso obterProximoConcurso() throws DLOException {
        try {
            return (new ConcursoDAO()).obter();
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }

    public static Concurso obterConcurso(int id) throws DLOException {
        return obterConcurso((long) id);
    }

    public static Concurso obterConcurso(Long id) throws DLOException {
        try {
            return (new ConcursoDAO()).obter(id);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }

    public static List<Concurso> listarTodos() throws DLOException {
        try {
            return (new ConcursoDAO()).listar();
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }

    public static int[] listarQuantidadeNumerosPares() throws DLOException {
        try {
            return (new ConcursoDAO()).listarQuantidadeNumerosPares();
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }

    public static TreeMap<Short, Long> listarNumerosMaisSorteados() throws DLOException {
        try {
            return (new ConcursoDAO()).listarNumerosMaisSorteados();
        } catch (DAOException e) {
            throw new DLOException(e);
        }
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
            BolaoDLO.transferirSaldoBolaoParaGrupo();
        }
    }

    public static void carregarBaseDeConcursos() throws DLOException {
        File zipMegaSena = downloadListaResultados();
        File megaSenaFile = unzipListaResultados(zipMegaSena, true);
        parseHTMLMegaSena(megaSenaFile);
        BolaoDLO.transferirSaldoBolaoParaGrupo();
    }

    public static Date dataProximoSorteio() {
        Calendar now = Calendar.getInstance();
        int weekday = now.get(Calendar.DAY_OF_WEEK);

        int days;
        if (weekday > Calendar.WEDNESDAY && weekday < Calendar.SATURDAY) {
            days = Calendar.SATURDAY - weekday;
        } else {
            days = Math.abs(Calendar.WEDNESDAY - weekday);
        }

        now.add(Calendar.DAY_OF_YEAR, days);

        return now.getTime();
    }

    public static boolean verificarJanelaCriacaoBolao(Concurso concurso) {
            Calendar now = Calendar.getInstance();
            Calendar dataConcurso = new GregorianCalendar();
            
            dataConcurso.setTime(concurso.getDataSorteio());
            
            return !dataConcurso.equals(now) && !dataConcurso.before(now);
    }
}