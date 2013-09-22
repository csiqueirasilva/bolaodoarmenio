package br.edu.infnet.concursos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.edu.infnet.DBConfig;
import br.edu.infnet.exceptions.DAOException;

class ConcursoDAO {
	void carregarRegistros(List<Concurso> concursos) throws DAOException {
		try {
			if (concursos == null) {
				return;
			}
			String insert_cmd = "INSERT INTO concurso (valor_sorteado,codigo_identificador,data_sorteio,creation_date,n1,n2,n3,n4,n5,n6)"
					+ " VALUES(?,?,?,now(),?,?,?,?,?,?);";
			DBConfig.initTransaction();
			for (int i = 0; i < concursos.size(); i++) {
				Concurso conc = concursos.get(i);
				List<Integer> numeros = conc.getNumerosSorteio();
				DBConfig.transactionQuery(insert_cmd, conc.getValorSorteado(),
						conc.getCodigoIdentificador(), conc.getDataSorteio(),
						numeros.get(0), numeros.get(1), numeros.get(2),
						numeros.get(3), numeros.get(4), numeros.get(5));
			}
			DBConfig.endTransaction();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	private Concurso dbConcursoToClass(HashMap<String, Object> result)
			throws DAOException {
		try {
			DateFormat formatDateDB = new SimpleDateFormat(
					"YYYY-mm-dd HH:mm:ss");
			Concurso conc = new Concurso();
			conc.id = (long) result.get("id");
			conc.codigoIdentificador = (int) result.get("codigo_identificador");
			conc.valorSorteado = (float) result.get("valor_sorteado");
			conc.editDate = result.get("edit_date") != null ? formatDateDB
					.parse((String) result.get("edit_date")) : null;
			conc.creationDate = (Date) formatDateDB.parse((String) result
					.get("creation_date"));
			conc.numerosSorteio = new ArrayList<Integer>();
			conc.dataSorteio = (Date) result.get("data_sorteio");
			conc.numerosSorteio.add(new Integer((short) result.get("n1")));
			conc.numerosSorteio.add(new Integer((short) result.get("n2")));
			conc.numerosSorteio.add(new Integer((short) result.get("n3")));
			conc.numerosSorteio.add(new Integer((short) result.get("n4")));
			conc.numerosSorteio.add(new Integer((short) result.get("n5")));
			conc.numerosSorteio.add(new Integer((short) result.get("n6")));
			return conc;
		} catch (Exception e) {
			throw new DAOException(e);
		}

	}

	int[] listarQuantidadeNumerosPares () throws DAOException {
		try {
			
			int[] pares = new int[7];

			List<HashMap<String,Object>> results = DBConfig.runSql("select count(1) as count, coalesce(par,0) as par, coalesce(impar,0) as impar from concurso left join (select id, count(1) as par from vw_nums_concurso AS V where numero%2 = 0 group by id) as P on P.id = concurso.id left join (select id, count(1) as impar from vw_nums_concurso AS V where numero%2 = 1 group by id) as I on I.id = concurso.id group by par, impar order by count desc;");
			
			for(int i = 0; i < results.size(); i++) {
				HashMap<String, Object> result = results.get(i);
				pares[(new Long((Long)result.get("par")).intValue())] = (new Long((Long) result.get("count"))).intValue();
			}			
			
			return pares;
			
		} catch (Exception e) {
			throw new DAOException(e);
		}
		
	}
	
	HashMap<Short, Long> listarNumerosMaisSorteados () throws DAOException {
		try {
			HashMap<Short, Long> list = new HashMap<Short,Long>();
			
			List<HashMap<String, Object>> results = DBConfig.runSql("select count(1) as quantidade, numero " 
			+ "from vw_nums_concurso AS numeros " +
			"group by numero " +
			"order by quantidade desc;") ;
			
			for(int i=0; i<results.size();i++) {
				HashMap<String, Object> result = results.get(i);
				list.put((Short) result.get("numero"), (Long) result.get("quantidade"));
			}
			
			return list;			
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	List<Concurso> listar() throws DAOException {
		try {
			List<Concurso> list = new ArrayList<Concurso>();
			ArrayList<HashMap<String, Object>> results = DBConfig
					.runSql("SELECT id, data_sorteio, creation_date, nullif(edit_date,'0000-00-00 00:00:00') as edit_date, valor_sorteado, codigo_identificador, n1, n2, n3, n4, n5, n6 FROM concurso");

			for(int i = 0; i < results.size(); i++) {
				list.add(dbConcursoToClass(results.get(i)));
			}
			
			return list;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	Concurso obter(Long id) throws DAOException {
		try {
			Concurso conc = null;
			ArrayList<HashMap<String, Object>> results = DBConfig
					.runSql("SELECT id, data_sorteio, creation_date, nullif(edit_date,'0000-00-00 00:00:00') as edit_date, valor_sorteado, codigo_identificador, n1, n2, n3, n4, n5, n6 FROM concurso WHERE id = "
							+ id);

			if (results.size() != 0) {
				conc = dbConcursoToClass(results.get(0));
			}

			return conc;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	long contarRegistros() throws DAOException {
		try {
			return (Long) DBConfig
					.runSql("SELECT count(1) AS count FROM concurso;").get(0)
					.get("count");
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}