package br.edu.infnet.concursos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Concurso implements Serializable  {
	private static final long serialVersionUID = 1L;

	Integer codigoIdentificador;
	Date dataSorteio;
	List<Integer> numerosSorteio;
	Float valorSorteado;
	Long id;
	Date editDate, creationDate;

        public Long getId() {
            return this.id;
        }        
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public void setValorSorteado(Float valor) {
		this.valorSorteado = valor;
	}
	
	public void setCodigoIdentificador(Integer cod) {
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

	public Integer getCodigoIdentificador() {
		return this.codigoIdentificador;
	}

	public Date getDataSorteio() {
		return this.dataSorteio;
	}
	
	public Float getValorSorteado() {
		return this.valorSorteado;
	}	
}