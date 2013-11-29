package br.edu.infnet.grupos;

import br.edu.infnet.participantes.Participante;

public class Grupo {
    protected String nome;
    protected Participante admin;
    protected Double saldo;
    protected Long id;
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    
    public void setAdmin(Participante admin) {
        this.admin = admin;
    }
    
    public Double getSaldo () {
        return this.saldo;
    }
    
    public String getNome () {
        return this.nome;
    }
    
    public Participante getAdmin () {
        return this.admin;
    }

    public Long getId () {
        return this.id;
    }
}
