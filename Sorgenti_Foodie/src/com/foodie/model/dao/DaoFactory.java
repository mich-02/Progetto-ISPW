package com.foodie.model.dao;


public interface DaoFactory {
	
	public  UtenteDao creaUtenteDao(); //login
	public  DispensaDao creaDispensaDao(); //dispensa che attualmente salvo solo su FS
	public  RicetteDaApprovareDao creaRicetteDaApprovareDao();
	public  RicettaDao creaRicettaDao(); //vecchio catalogo delle ricette 
	
}
