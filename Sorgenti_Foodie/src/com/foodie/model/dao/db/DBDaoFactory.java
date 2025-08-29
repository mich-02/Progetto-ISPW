package com.foodie.model.dao.db;

import com.foodie.model.dao.DaoFactory;
import com.foodie.model.dao.DispensaDao;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.UtenteDao;

public class DBDaoFactory implements DaoFactory {

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
		return new RicettaDaoFS();
	}

}
