package br.edu.infnet.concursos;

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
				+ " VALUES(?,?,?,now(),?,?,?,?,?,?);" ;
		DBConfig.initTransaction();		
		for (int i = 0; i < concursos.size(); i++) {
			Concurso conc = concursos.get(i) ;
			List<Integer> numeros = conc.getNumerosSorteio() ;
			DBConfig.transactionQuery(insert_cmd,
					conc.getValorSorteado(), 
					conc.getCodigoIdentificador(),
					conc.getDataSorteio(),
					numeros.get(0),
					numeros.get(1),
					numeros.get(2),
					numeros.get(3),
					numeros.get(4),
					numeros.get(5));
		}
		DBConfig.endTransaction();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	long contarRegistros() throws DAOException {
		try {
			return (Long) DBConfig.runSql("SELECT count(1) AS count FROM concurso;").get(0).get("count");
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}