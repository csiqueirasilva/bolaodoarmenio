package br.edu.infnet.grupos;

import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.exceptions.DLOException;
import br.edu.infnet.participantes.Participante;
import java.util.List;

public class GrupoDLO {

    public static List<Participante> listarNaoMembros (Grupo grupo) throws DLOException {
        try {
            return new GrupoDAO().listarNaoMembros(grupo);
        } catch (Exception e) {
            throw new DLOException(e);
        }        
    }
    
    public static void adicionarMembro (Grupo grupo, Participante participante) throws DLOException {
        try {
            new GrupoDAO().adicionarMembro(grupo, participante);
        } catch (Exception e) {
            throw new DLOException(e);
        }
    }
            
    public static Grupo obter (Long idGrupo) throws DLOException {
        try {
            return new GrupoDAO().obterGrupo(idGrupo);
        } catch (Exception e) {
            throw new DLOException(e);
        }
    }
    
    public static List<Participante> membros (Long idGrupo, Long inicio, Long qtd) throws DLOException {
        try {
            return new GrupoDAO().listarMembros(idGrupo, inicio, qtd);
        } catch (Exception e) {
            throw new DLOException(e);
        }
    }
    
    public static boolean existe (String nome) throws DLOException {
        try {
            return new GrupoDAO().verificarNome(nome);
        } catch (Exception e) {
            throw new DLOException(e);
        }
    }
    
    public static void inserirNovoGrupo (Grupo grupo) throws DLOException {
        try {
            new GrupoDAO().inserir(grupo);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
    
    public static List<Grupo> listarGrupoBolaoDisponivelCriacao (Long id_usuario) throws DLOException {
        try {
            return new GrupoDAO().listarGrupoBolaoDisponivelCriacao(id_usuario);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }        
    
    public static List<Grupo> listarGruposUsuario (Long id_usuario) throws DLOException {
        try {
            return new GrupoDAO().listar(id_usuario);
        } catch (DAOException e) {
            throw new DLOException(e);
        }
    }
}