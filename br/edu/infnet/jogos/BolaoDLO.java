/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.infnet.jogos;

import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.exceptions.DLOException;
import java.util.List;

/**
 *
 * @author csiqueira
 */
public class BolaoDLO {
    
    public static void registrarPagamento (Long id_participante, Long id_bolao, Float valorDepositado) throws DLOException {
        try {
            new BolaoDAO().registrarPagamento(id_participante, id_bolao, valorDepositado);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
    
    public static List<Bolao> listar (Long id_participante) throws DLOException {
        try {
            return new BolaoDAO().listarBolaoAberto(id_participante);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }

    public static void transferirSaldoBolaoParaGrupo() throws DLOException {
        try {
            new BolaoDAO().transferirSaldoBolaoParaGrupo();
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
    
    public static Bolao obterBolaoAberto (Long id_grupo) throws DLOException {
        try {
            return new BolaoDAO().obterPorGrupo(id_grupo);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
    
    public static Bolao obter (Long id_bolao) throws DLOException {
        try {
            return new BolaoDAO().obter(id_bolao);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
    
    public static void inserir (Bolao bolao) throws DLOException {
        try {
            new BolaoDAO().inserir(bolao);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
}
