package com.foodie.model.dao;


public interface DaoFactory {
	public  UtenteDao creaUtenteDao(); 
	public  DispensaDao creaDispensaDao(); 
	public  RicetteDaApprovareDao creaRicetteDaApprovareDao();
	public  RicettaDao creaRicettaDao(); 
	
}