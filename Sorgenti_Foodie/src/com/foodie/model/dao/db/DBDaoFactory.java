package com.foodie.model.dao.db;

import com.foodie.model.dao.DaoFactory;
import com.foodie.model.dao.DispensaDao;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.RicetteDaApprovareDao;
import com.foodie.model.dao.UtenteDao;

public class DBDaoFactory implements DaoFactory {

	@Override
	public UtenteDao creaUtenteDao() {
		return new UtenteDaoDB();
	}

	@Override
	public DispensaDao creaDispensaDao() {
		//return new DispensaDaoFS();
		return new DispensaDaoDB();
	}

	
	@Override
	public RicetteDaApprovareDao creaRicetteDaApprovareDao() {
		return new RicetteDaApprovareDaoDB();
	}
	

	@Override
	public RicettaDao creaRicettaDao() {
		return new RicettaDaoDB();
		//return new RicettaDaoFS();
	}

}
