package com.foodie.model;

public class AlimentoBean {  //BEAN DELLA CLASSE ALIMENTO
	
	private String nome;
    private String quantita;  
	
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