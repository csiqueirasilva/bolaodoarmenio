/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.jogos;

import java.util.Comparator;

/**
 *
 * @author csiqueira
 */
public class ComparadorAposta implements Comparator<Aposta> {

    @Override
    public int compare(Aposta aposta1, Aposta aposta2) {
        int resultado;

        Short[] numeros1 = (Short[]) aposta1.numeros.toArray(new Short[aposta1.numeros.size()]);
        Short[] numeros2 = (Short[]) aposta2.numeros.toArray(new Short[aposta2.numeros.size()]);

        int qtdIguais = 0;
        for (int j = 0; j < numeros1.length; j++) {
            int i = 0;
            for (; i < numeros2.length && numeros2[i].compareTo(numeros1[j]) != 0; i++) {
                ;
            }

            if (i != numeros2.length) {
                qtdIguais++;
            }
        }

        if (qtdIguais == numeros1.length || qtdIguais == numeros2.length) {
            resultado = 0;
        } else {
            resultado = aposta1.numeros.size() > aposta2.numeros.size() ? 1 : -1;
        }

        return resultado;
    }
}
