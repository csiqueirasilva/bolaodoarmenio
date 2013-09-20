package br.edu.infnet.usuarios;

public class UsuarioDLO {

	public static Usuario carregar(long id) {
		return carregar((int) id);
	}

	public static Usuario carregar(int id) {
		return (new UsuarioDAO()).obter(id);
	}

	public static void salvar(Usuario usr) {
		if (usr.email != null && usr.senha != null) {
			UsuarioDAO uDAO = new UsuarioDAO();
			if (usr.id != null) {
				uDAO.inserir(usr);
			} else {
				uDAO.alterar(usr);
			}
		}
	}
}