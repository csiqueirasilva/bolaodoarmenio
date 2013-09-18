package BolaoDoArmenio.usuarios;

import java.util.Date;

public class Usuario {
	protected String email, senha;
	protected Integer id;
	protected Date creation_date, edit_date ;
	
	public Usuario(String email, String senha) {
		this.email = email;
		this.senha = senha;
	}

	public Usuario() {
		;
	}	

	public Usuario(int id) {
		this.id = id;
		carregar();
	}	

	public int getId () {
		return this.id ;
	}

	public void setId (int id) {
		this.id = id;
	}

	public void setId (long id) {
		this.id = (int) id;
	}
	
	public String getEmail () {
		return this.email ;
	}
	
	public String getSenha() {
		return this.senha ;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public void setSenha (String senha) {
		this.senha = senha;
	}

	public void setCreationDate (Date creation_date) {
		this.creation_date = (Date) creation_date.clone();
	}
	
	public void setEditDate (Date edit_date) {
		this.edit_date = (Date) edit_date.clone();
	}
	
	public Date getEditDate () {
		return this.edit_date ;
	}
	
	public Date getCreationDate () {
		return this.creation_date ;
	}
	
	public void carregar() {
		if (this.id != null) {
			UsuarioDAO uDAO = new UsuarioDAO();
			Usuario u = uDAO.obter(this.id);
						
			if (u != null) {
				this.id = u.id;
				this.email = u.email;
				this.senha = u.senha;
				this.creation_date = (Date) u.creation_date.clone();
				this.edit_date = (Date) u.edit_date.clone();
			}
		}
	}
	
	public void salvar () {
		if ( this.id != null && this.email != null && this.senha != null )
		{
			UsuarioDAO uDAO = new UsuarioDAO();
			uDAO.inserir(this);
		}
	}
	
}