package BolaoDoArmenio.concursos;

import java.util.Date;
import java.util.List;

public class Concurso {
	private int codigoIdentificador;
	private Date dataSorteio;
	private List<Integer> numerosSorteio;
	private double valorSorteado;
	
	public void setValorSorteado(double valor) {
		this.valorSorteado = valor;
	}
	
	public void setCodigoIdentificador(int cod) {
		this.codigoIdentificador = cod;
	}

	public void setDataSorteio(Date dt) {
		if (dt == null) {
			return;
		}
		this.dataSorteio = (Date) dt.clone();
	}

	public void setNumerosSorteio(List<Integer> numeros) {
		if (numeros == null) {
			return;
		}
		this.numerosSorteio = numeros;
	}

	public List<Integer> getNumerosSorteio() {
		return this.numerosSorteio;
	}

	public int getCodigoIdentificador() {
		return this.codigoIdentificador;
	}

	public Date getDataSorteio() {
		return this.dataSorteio;
	}
	
	public double getValorSorteado() {
		return this.valorSorteado;
	}	
}