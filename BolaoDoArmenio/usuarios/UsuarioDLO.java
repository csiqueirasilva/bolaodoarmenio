package BolaoDoArmenio.usuarios;

class UsuarioDLO {

	public Usuario carregar(long id) {
		return carregar((int) id);
	}

	public Usuario carregar(int id) {
		return (new UsuarioDAO()).obter(id);
	}

	public void salvar(Usuario usr) {
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