package br.edu.infnet.usuarios;

import br.edu.infnet.exceptions.DLOException;

public class UsuarioDLO {

        public static String ROOT_EMAIL = "root@bolaodoarmenio.com.br";
    
        public static boolean existe (String email) throws DLOException {
            try {
                return new UsuarioDAO().verificarEmail(email);
            } catch (Exception e) {
                throw new DLOException(e);
            }
        }
    
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

	public static Long obterId (Usuario usr) throws DLOException {
		try {
			return new UsuarioDAO().obter(usr).id;
		} catch (NullPointerException e) {
			return new Long(-1);
		} catch (Exception e) {
			throw new DLOException(e);
		}
	}
	
	public static void salvar(Usuario usr) throws DLOException {
		if (usr.email != null && usr.senha != null) {
			UsuarioDAO uDAO = new UsuarioDAO();
			try {
				if (usr.id == null || usr.id == 0) {
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