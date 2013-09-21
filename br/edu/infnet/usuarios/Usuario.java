package br.edu.infnet.usuarios;

import java.util.Date;

public class Usuario {
	protected String email, senha;
	protected Long id;
	protected Date creation_date, edit_date ;
	
	public Usuario(String email, String senha) {
		this.email = email;
		this.senha = senha;
	}

	public Usuario() {
		;
	}	

	public long getId () {
		return this.id ;
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

	public Date getEditDate () {
		return this.edit_date ;
	}
	
	public Date getCreationDate () {
		return this.creation_date ;
	}
	
}