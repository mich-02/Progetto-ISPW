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

public class MemoDaoFactory implements DaoFactory { 
	
	//private final DispensaDaoMemo dispensaDaoMemo = new DispensaDaoMemo();
	//private final RicetteDaApprovareDaoMemo ricetteDaApprovareDaoMemo = new RicetteDaApprovareDaoMemo();
	//private final RicettaDaoMemo ricettaDaoMemo = new RicettaDaoMemo(dispensaDaoMemo,ricetteDaApprovareDaoMemo );

	@Override
	public UtenteDao creaUtenteDao() {
		return new UtenteDaoMemo();
	}

	@Override
	public DispensaDao creaDispensaDao() {
		//return dispensaDaoMemo;
		return new DispensaDaoMemo();
	}

	@Override
	public RicettaDao creaRicettaDao() {
		//return ricettaDaoMemo;
		return new RicettaDaoMemo(creaDispensaDao(), creaRicetteDaApprovareDao());
	}

	@Override
	public RicetteDaApprovareDao creaRicetteDaApprovareDao() {
		//return ricetteDaApprovareDaoMemo;
		return new RicetteDaApprovareDaoMemo();
	}

}
