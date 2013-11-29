package br.edu.infnet.participantes;

import br.edu.infnet.exceptions.DAOException;
import br.edu.infnet.exceptions.DLOException;

public class ParticipanteDLO {
	public static Participante obterParticipante(Long id) throws DLOException {
		try {
			return new ParticipanteDAO().obter(id);
		} catch (DAOException e) {
			throw new DLOException(e);
		}
	}
	
	public static void inserirParticipante(Participante part) throws DLOException {
		try {
			new ParticipanteDAO().inserir(part);
		} catch (DAOException e) {
			throw new DLOException(e);
		}
	}	
}