package br.edu.infnet.concursos;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import br.edu.infnet.DBConfig;

class ConcursoDAO {
	void carregarRegistros(List<Concurso> concursos) {
		if (concursos == null) {
			return;
		}
		String insert_cmd = "INSERT INTO concurso (valor_sorteado,codigo_identificador,data_sorteio,creation_date,n1,n2,n3,n4,n5,n6)"
				+ " VALUES(%f,%d,nullif('%s',''),now(),%d,%d,%d,%d,%d,%d);\n" ,
				query = "";
		DBConfig.initTransaction();		
		for (int i = 0; i < concursos.size(); i++) {
			Concurso conc = concursos.get(i) ;
			List<Integer> numeros = conc.getNumerosSorteio() ;
			query = String.format(Locale.US,
					insert_cmd,
					conc.getValorSorteado(), 
					conc.getCodigoIdentificador(),
					(new SimpleDateFormat("yyyy-MM-dd")).format(conc.getDataSorteio()),
					numeros.get(0),
					numeros.get(1),
					numeros.get(2),
					numeros.get(3),
					numeros.get(4),
					numeros.get(5));
			DBConfig.transactionQuery(query);
		}
		DBConfig.endTransaction();
	}
	
	long contarRegistros() {
		return (Long) DBConfig.runSql("SELECT count(1) AS count FROM concurso;").get(0).get("count");
	}
}