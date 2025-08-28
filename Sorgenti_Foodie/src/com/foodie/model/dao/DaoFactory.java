package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;

public abstract class DaoFactory {
	
	private static DaoFactory istanza = null; //singleton
	
	protected DaoFactory() {
	}
	
	public static DaoFactory ottieniIstanza() {
		if (istanza == null){
			
			switch (ConfiguratorePersistenza.getPersistenzaCorrente()) {
			case MEMORIA :
					istanza = new MemoDaoFactory();
				break;
			case DATABASE:
				    istanza = new DBDaoFactory();
				break;
			}			
		}
		return istanza;
	}
	
	public abstract UtenteDao creaUtenteDao(); //login
	public abstract DispensaDao creaDispensaDao(); //dispensa che attualmente salvo solo su FS
	//public abstract ChefDao creaChefDao(); //area personale dello chef 
	public abstract RicettaDao creaRicettaDao(); //vecchio catalogo delle ricette 
	
}
