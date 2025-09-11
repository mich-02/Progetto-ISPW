package com.foodie.model.dao.db;

import com.foodie.model.dao.DaoFactory;
import com.foodie.model.dao.DispensaDao;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.RicetteDaApprovareDao;
import com.foodie.model.dao.UtenteDao;

public class DBDaoFactory implements DaoFactory { 
	private static DBDaoFactory istanza = null;
	
	private DBDaoFactory() {
	}
	
	public static DBDaoFactory ottieniIstanza() {
		if(istanza == null) {
			istanza = new DBDaoFactory();
		}
		return istanza;
	}

	@Override
	public UtenteDao creaUtenteDao() {
		return new UtenteDaoDB();
	}

	@Override
	public DispensaDao creaDispensaDao() {
		return new DispensaDaoDB();
	}

	
	@Override
	public RicetteDaApprovareDao creaRicetteDaApprovareDao() {
		return new RicetteDaApprovareDaoDB();
	}
	

	@Override
	public RicettaDao creaRicettaDao() {
		return new RicettaDaoDB();
	}

}
