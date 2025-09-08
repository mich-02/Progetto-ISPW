package com.foodie.bean;

public class AlimentoBean {  
	
	private String nome;
    private String quantita;  
    
    public AlimentoBean(String nome) {
    	this.nome = nome;
    }
    
    public AlimentoBean() {
    }
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

    public void setQuantita(String quantita) { 
        this.quantita = quantita;
    }   

    public String getQuantita() {
        return quantita;
    }   
	
}