package br.edu.infnet.usuarios;

import br.edu.infnet.exceptions.DLOException;

public class UsuarioDLO {

	public static Usuario carregar(long id) throws DLOException {
		return carregar((int) id);
	}

	public static Usuario carregar(int id) throws DLOException {
		try {
			return (new UsuarioDAO()).obter(id);
		} catch (Exception e) {
			throw new DLOException(e);
		}
	}

	public static void salvar(Usuario usr) throws DLOException {
		if (usr.email != null && usr.senha != null) {
			UsuarioDAO uDAO = new UsuarioDAO();
			try {
				if (usr.id != null) {
					uDAO.inserir(usr);
				} else {
					uDAO.alterar(usr);
				}
			} catch (Exception e) {
				throw new DLOException(e);
			}
		}
	}
}