package BolaoDoArmenio.concursos;

import java.util.List;
import java.util.Locale;

import BolaoDoArmenio.DBConfig;

class ConcursoDAO {
	void carregarRegistros(List<Concurso> concursos) {
		if (concursos == null) {
			return;
		}
		String insert_cmd = "INSERT INTO concurso (id, valor_sorteado, codigo_identificador, data_sorteio, creation_date, n1, n2, n3, n4, n5, n6 )"
				+ "VALUES( nextval('seq_concurso'), %f, %d, $$%s$$, now(), %d, %d, %d, %d, %d, %d ) ;" ,
				query = "TRUNCATE concurso CASCADE;"; /* Linha temporária para manter o banco um pouco menos cheio, deve ser uma String vazia */
		for (int i = 0; i < concursos.size(); i++) {
			Concurso conc = concursos.get(i) ;
			List<Integer> numeros = conc.getNumerosSorteio() ;
			query += String.format(Locale.US,
					insert_cmd,
					conc.getValorSorteado(), 
					conc.getCodigoIdentificador(),
					conc.getDataSorteio().toString(),
					numeros.get(0),
					numeros.get(1),
					numeros.get(2),
					numeros.get(3),
					numeros.get(4),
					numeros.get(5));
		}

		DBConfig.init();
		DBConfig.runSql(query);
		DBConfig.end();
	}
}