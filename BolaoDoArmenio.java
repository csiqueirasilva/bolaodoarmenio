package BolaoDoArmenio;

import BolaoDoArmenio.usuarios.*;

public class BolaoDoArmenio {	
	public static void main (String args[]) {
		Usuario usuario = null;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuario = usuarioDAO.obter(1);
		System.out.println(usuario.getEmail());
		System.out.println(usuario.getSenha());
	}
}