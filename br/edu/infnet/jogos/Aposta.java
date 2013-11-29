/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.jogos;

import br.edu.infnet.jogos.Bolao;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author csiqueira
 */
public class Aposta {
    protected TreeSet<Short> numeros;
    protected Bolao bolao;
    protected Long id;
    
    public static int TIPO_RANDOMICA = 1;
    public static int TIPO_MANUAL = 2;
    public static int TIPO_ESTATISTICA = 3;
    
    public Long getId () {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public TreeSet<Short> getNumeros () {
        return this.numeros;
    }
    
    public void setNumeros(TreeSet<Short> numeros) {
        this.numeros = numeros;
    }
    
    public Bolao getBolao() {
        return this.bolao;
    }
    
    public void setBolao (Bolao bolao) {
        this.bolao = bolao;
    }
}
