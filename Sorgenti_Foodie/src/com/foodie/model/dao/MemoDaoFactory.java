package com.foodie.model.dao;

public class MemoDaoFactory extends DaoFactory { //finché non ho aggiungo la demo creo sempre i Dao DB

	@Override
	public UtenteDao creaUtenteDao() {
		return new UtenteDaoDB();
	}

	@Override
	public DispensaDao creaDispensaDao() {
		return new DispensaDaoDB();
	}

	/*
	@Override
	public ChefDao creaChefDao() {
		return new ChefDaoDB();
	}
	*/

	@Override
	public RicettaDao creaRicettaDao() {
		return new RicettaDaoDB();
	}

}
