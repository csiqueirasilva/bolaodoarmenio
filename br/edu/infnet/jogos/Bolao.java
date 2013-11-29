/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.jogos;

import br.edu.infnet.concursos.Concurso;
import br.edu.infnet.grupos.Grupo;
import java.util.List;

/**
 *
 * @author csiqueira
 */
public class Bolao {
    protected Float valorDepositado;
    protected Aposta[] listaApostas;
    protected Long id;
    protected Grupo grupo;
    protected Concurso concurso;
    
    public Grupo getGrupo () {
        return this.grupo;
    }
    
    public void setGrupo (Grupo grupo) {
        this.grupo = grupo;
    }
    
    public Concurso getConcurso () {
        return this.concurso;
    }
    
    public void setConcurso (Concurso concurso) {
        this.concurso = concurso;
    }
    
    public Aposta[] getListaApostas () {
        return this.listaApostas;
    }
    
    public void setListaApostas (Aposta[] listaApostas) {
        this.listaApostas = listaApostas;
    }    
    
    public Float getValorDepositado () {
        return this.valorDepositado;
    }
    
    public void setValorDepositado (Float valorDepositado) {
        this.valorDepositado = valorDepositado;
    }
    
    public Long getId () {
        return this.id;
    }
    
    public void setId (Long id) {
        this.id = id;
    }
}
