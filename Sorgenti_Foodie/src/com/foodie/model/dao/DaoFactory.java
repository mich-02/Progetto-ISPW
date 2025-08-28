package com.foodie.model.dao;


public interface DaoFactory {
	
	public  UtenteDao creaUtenteDao(); //login
	public  DispensaDao creaDispensaDao(); //dispensa che attualmente salvo solo su FS
	//public  ChefDao creaChefDao(); //area personale dello chef 
	public  RicettaDao creaRicettaDao(); //vecchio catalogo delle ricette 
	
}
