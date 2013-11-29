/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.jogos;

import br.edu.infnet.concursos.ConcursoDLO;
import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.exceptions.DLOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author csiqueira
 */
public class ApostaDLO {

    public static float PRECO_APOSTA = 2f;
    public static short MIN_QTD_NUMERO_APOSTA = 6;
    public static short MAX_QTD_NUMERO_APOSTA = 15;

    public static double precoAposta(Short qtdNums) {
        double res = PRECO_APOSTA;
        if (qtdNums >= MIN_QTD_NUMERO_APOSTA && qtdNums <= MAX_QTD_NUMERO_APOSTA) {
            BigDecimal fatS = new BigDecimal(1);
            BigDecimal fatN = new BigDecimal(1);
            short num = qtdNums;

            for (; num > 1; num--) {
                fatS = fatS.multiply(new BigDecimal(num));
            }

            num = (short) (qtdNums - ConcursoDLO.QTD_NUMEROS_POR_CONCURSO);

            for (; num > 1; num--) {
                fatN = fatN.multiply(new BigDecimal(num));
            }

            res = fatS.multiply(new BigDecimal(PRECO_APOSTA)).divide(fatN.multiply(new BigDecimal(720))).doubleValue();
        }
        return res;
    }

    public static TreeSet<Aposta> listar(Long id_bolao) throws DLOException {
        try {
            return new ApostaDAO().listar(id_bolao);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }

    /* Cobre os tipos randômico e manual */
    private static Aposta adicionarAposta(TreeSet<Aposta> apostas, int tipo, List<Short> numeros, int qtdNumeros) {
        Aposta aposta = new Aposta();
        boolean inseriu = true;
        do {
            TreeSet<Short> numerosDaAposta = new TreeSet<Short>();
            int i = 0;

            if (tipo == Aposta.TIPO_MANUAL || tipo == Aposta.TIPO_ESTATISTICA) {
                for (; i < qtdNumeros && i < numeros.size(); i++) {
                    Short numero = new Short(numeros.get(i));
                    numerosDaAposta.add(numero);
                }
            }

            boolean inseriuNaListaDeNumeros = false;
            Random rand = new Random(System.currentTimeMillis());
            for (; i < qtdNumeros; i++) {
                while (!numerosDaAposta.add((short) (rand.nextInt(60) + 1)));
            }

            aposta.numeros = numerosDaAposta;

            inseriu = apostas.add(aposta);
            if (!inseriu && numeros.size() > 0) {
                numeros.remove(numeros.size() - 1);
            }
        } while (!inseriu);

        return aposta;
    }

    private static List<Short> numerosEstatisticos() {
        List<Short> numerosEstatisticos = null;
        try {
            Map<Short, Long> maisSorteados = ConcursoDLO.listarNumerosMaisSorteados();
            int[] pares = ConcursoDLO.listarQuantidadeNumerosPares();
            numerosEstatisticos = new ArrayList<Short>();
            
            int maxIdx = 0;
            for(int i = 1; i < pares.length; i++) {
                if(pares[maxIdx] < pares[i]) {
                    maxIdx = i;
                }
            }

            int qtdPar = maxIdx;
            int qtdImpar = ConcursoDLO.QTD_NUMEROS_POR_CONCURSO - qtdPar;

            List<Short> sortedMaisSorteados = new ArrayList<Short>();

            int qtdSorteados = maisSorteados.values().size();
            while (qtdSorteados != sortedMaisSorteados.size()) {
                Long max = Collections.max(maisSorteados.values());
                Set<Short> keys = maisSorteados.keySet();
                Iterator<Short> it = keys.iterator();
                Short num = it.next();
                for (; it.hasNext() && maisSorteados.get(num) != max; num = it.next()) {
                    ;
                }
                sortedMaisSorteados.add(num);
                maisSorteados.remove(num);
            }

            while (numerosEstatisticos.size() != qtdSorteados) {
                int i = 0;
                List<Short> itList = new ArrayList<Short>(sortedMaisSorteados);
                Iterator<Short> it = itList.iterator();
                Short num;
                for (; it.hasNext() && i < qtdPar;) {
                    num = it.next();
                    if (num % 2 == 0) {
                        numerosEstatisticos.add(num);
                        sortedMaisSorteados.remove(num);
                        i++;
                    }
                }

                i = 0;
                itList = new ArrayList<Short>(sortedMaisSorteados);
                it = itList.iterator();
                for (; it.hasNext() && i < qtdImpar;) {
                    num = it.next();
                    if (num % 2 != 0) {
                        numerosEstatisticos.add(num);
                        sortedMaisSorteados.remove(num);
                        i++;
                    }
                }
            }

        } catch (DLOException ex) {
            Logger.getLogger(ApostaDLO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return numerosEstatisticos;
    }

    private static TreeSet<Aposta> adicionarApostas(TreeSet<Aposta> apostas, List<Short> numerosPreferenciais, int tipo, int qtd, int qtdNumeros) {
        TreeSet<Aposta> apostasNovas = new TreeSet<Aposta>(new ComparadorAposta());
        List<Short> numerosEstatisticos = numerosEstatisticos();
        for (int i = 0; i < qtd; i++) {
            Aposta apostaNova ;
            if (Aposta.TIPO_ESTATISTICA == tipo) {
                apostaNova = adicionarAposta(apostas, tipo, numerosEstatisticos, qtdNumeros);
            } else /* Aposta.TIPO_MANUAL == tipo || Aposta.TIPO_RANDOMICA == tipo */ {
                apostaNova = adicionarAposta(apostas, tipo, numerosPreferenciais, qtdNumeros);
            }
            apostasNovas.add(apostaNova);
        }
        return apostasNovas;
    }

    private static TreeSet<Aposta> distribuirApostas(Bolao bolao, Float valorDepositado, Float qtdEstatisticaSRC, Float qtdRandomicaSRC, Float qtdManualSRC, List<Short> numeros) throws DLOException {
        TreeSet<Aposta> apostasNovas = null;
        TreeSet<Aposta> apostas;
        Float qtdEstatistica = qtdEstatisticaSRC;
        Float qtdManual = qtdManualSRC;
        Float qtdRandomica = qtdRandomicaSRC;

        if (bolao == null) {
            throw new DLOException("Não há bolão para distribuição de apostas!");
        } else if (qtdEstatistica >= 0 && qtdManual >= 0 && qtdRandomica >= 0 && (qtdEstatistica + qtdManual + qtdRandomica) <= 100f) {

            try {
                apostas = new ApostaDAO().listar(bolao.getId());
            } catch (DAOException e) {
                throw new DLOException(e);
            }

            int[] listaQuantidade = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            int totalDeApostas = 0;

            for (int i = MAX_QTD_NUMERO_APOSTA; i >= MIN_QTD_NUMERO_APOSTA && valorDepositado >= PRECO_APOSTA; i--) {
                double preco = precoAposta((short) i);
                int qtdApostas = (int) (valorDepositado / preco);
                listaQuantidade[i - MIN_QTD_NUMERO_APOSTA] = qtdApostas;
                valorDepositado = new BigDecimal(valorDepositado).subtract(new BigDecimal(preco).multiply(new BigDecimal(qtdApostas))).floatValue();
                totalDeApostas += qtdApostas;
            }

            bolao.setValorDepositado(valorDepositado);

            qtdEstatistica = new Float(new BigDecimal(qtdEstatistica).divide(new BigDecimal(100f)).multiply(new BigDecimal(totalDeApostas)).floatValue());
            qtdManual = new Float(new BigDecimal(qtdManual).divide(new BigDecimal(100f)).multiply(new BigDecimal(totalDeApostas)).floatValue());
            qtdRandomica = new Float(new BigDecimal(qtdRandomica).divide(new BigDecimal(100f)).multiply(new BigDecimal(totalDeApostas)).floatValue());

            qtdEstatistica = (float) Math.round(qtdEstatistica);
            qtdManual = (float) Math.round(qtdManual);
            qtdRandomica = (float) Math.round(qtdRandomica);

            int diff = totalDeApostas - (qtdEstatistica.intValue() + qtdManual.intValue() + qtdRandomica.intValue());

            if (diff != 0) {
                TreeSet<Float> decideErrosAposta = new TreeSet<Float>();
                decideErrosAposta.add(qtdManualSRC);
                decideErrosAposta.add(qtdEstatisticaSRC);
                decideErrosAposta.add(qtdRandomicaSRC);

                Float qtdAlterada;
                if (diff > 0) {
                    qtdAlterada = decideErrosAposta.pollLast();
                } else {
                    while ((qtdAlterada = decideErrosAposta.pollFirst()).intValue() == 0);
                }

                if (qtdAlterada == qtdManualSRC) {
                    qtdManual = new Float(qtdManual.floatValue() + diff);
                } else if (qtdAlterada == qtdEstatisticaSRC) {
                    qtdEstatistica = new Float(qtdEstatistica.floatValue() + diff);
                } else /* qtdAlterada == qtdRandomicaSRC */ {
                    qtdRandomica = new Float(qtdRandomica.floatValue() + diff);
                }
            }

            TreeSet<QuantidadeAposta> qtds = new TreeSet<QuantidadeAposta>(new Comparator<QuantidadeAposta>() {
                public int compare(QuantidadeAposta o1, QuantidadeAposta o2) {
                    if (o1.qtd == o2.qtd) {
                        return 0;
                    }
                    return o1.qtd > o2.qtd ? 1 : -1;
                }
            });

            if (qtdRandomica != 0) {
                qtds.add(new QuantidadeAposta(qtdRandomica.intValue(), Aposta.TIPO_RANDOMICA));
            }

            if (qtdEstatistica != 0) {
                qtds.add(new QuantidadeAposta(qtdEstatistica.intValue(), Aposta.TIPO_ESTATISTICA));
            }

            if (qtdManual != 0) {
                qtds.add(new QuantidadeAposta(qtdManual.intValue(), Aposta.TIPO_MANUAL));
            }

            apostasNovas = new TreeSet<Aposta>(new ComparadorAposta());
            for (int i = listaQuantidade.length - 1; i >= 0 && qtds.size() > 0; i--) {
                if (listaQuantidade[i] != 0) {
                    int qtdApostaEspecifica;
                    QuantidadeAposta maiorQtd = qtds.pollLast();
                    int qtdNumeros = i + MIN_QTD_NUMERO_APOSTA;

                    if (listaQuantidade[i] > maiorQtd.qtd) {
                        qtdApostaEspecifica = (int) maiorQtd.qtd;
                        listaQuantidade[i] -= maiorQtd.qtd;
                        maiorQtd.qtd = 0;
                        i++;
                    } else {
                        qtdApostaEspecifica = (int) listaQuantidade[i];
                        maiorQtd.qtd -= listaQuantidade[i];
                        listaQuantidade[i] = 0;
                    }

                    TreeSet<Aposta> apostasAdicionadas = adicionarApostas(apostas, numeros, maiorQtd.tipo, qtdApostaEspecifica, qtdNumeros);
                    for(Iterator<Aposta> it = apostasAdicionadas.iterator(); it.hasNext(); ) {
                        Aposta ap = it.next();
                        apostasNovas.add(ap);
                    }
                    
                    if (maiorQtd.qtd != 0) {
                        qtds.add(maiorQtd);
                    }
                }
            }

        } else {
            throw new DLOException("Estatística de distribuição de apostas inválida.");
        }
        return apostasNovas;
    }

    public static TreeSet<Aposta> inserir(Long id_bolao, Float qtdEstatistica, Float qtdRandomica, Float qtdManual, List<Short> numeros) throws DLOException {
        Bolao bolao = BolaoDLO.obter(id_bolao);
        Float valorDisponivel = bolao.getValorDepositado();
        TreeSet<Aposta> apostas = distribuirApostas(bolao, valorDisponivel, qtdEstatistica, qtdRandomica, qtdManual, numeros);
        Aposta[] arrayApostas = (Aposta[]) apostas.toArray(new Aposta[apostas.size()]);
        for (int i = 0; i < arrayApostas.length; i++) {
            arrayApostas[i].setBolao(bolao);
        }

        try {
            new BolaoDAO().atualizar(bolao, arrayApostas);
        } catch (DAOException e) {
            throw new DLOException(e);
        }

        return apostas;
    }
}
