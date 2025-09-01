package com.foodie.model.dao.memo;

import com.foodie.model.dao.DaoFactory;
import com.foodie.model.dao.DispensaDao;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.RicetteDaApprovareDao;
import com.foodie.model.dao.UtenteDao;
import com.foodie.model.dao.db.DispensaDaoDB;
import com.foodie.model.dao.db.RicettaDaoDB;
import com.foodie.model.dao.db.RicetteDaApprovareDaoDB;
import com.foodie.model.dao.db.UtenteDaoDB;

public class MemoDaoFactory implements DaoFactory { //finché non ho aggiungo la demo creo sempre i Dao DB

	@Override
	public UtenteDao creaUtenteDao() {
		return new UtenteDaoMemo();
	}

	@Override
	public DispensaDao creaDispensaDao() {
		return new DispensaDaoDB();
	}

	@Override
	public RicettaDao creaRicettaDao() {
		return new RicettaDaoDB();
	}

	@Override
	public RicetteDaApprovareDao creaRicetteDaApprovareDao() {
		// TODO Auto-generated method stub
		return new RicetteDaApprovareDaoDB();
	}

}
