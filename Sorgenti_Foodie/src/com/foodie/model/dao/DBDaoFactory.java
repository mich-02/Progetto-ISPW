package com.foodie.model.dao;

import com.foodie.model.dao.db.DispensaDaoDB;
import com.foodie.model.dao.db.RicettaDaoDB;
import com.foodie.model.dao.db.RicetteDaApprovareDaoDB;
import com.foodie.model.dao.db.UtenteDaoDB;

public class DBDaoFactory extends DaoFactory {
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
