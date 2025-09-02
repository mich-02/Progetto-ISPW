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
	
	private final DispensaDaoMemo dispensaDaoMemo = new DispensaDaoMemo();
	private final RicettaDaoMemo ricettaDaoMemo = new RicettaDaoMemo();
	private final RicetteDaApprovareDaoMemo ricetteDaApprovareDaoMemo = new RicetteDaApprovareDaoMemo();

	@Override
	public UtenteDao creaUtenteDao() {
		return new UtenteDaoDB();
	}

	@Override
	public DispensaDao creaDispensaDao() {
		return dispensaDaoMemo;
	}

	@Override
	public RicettaDao creaRicettaDao() {
		return ricettaDaoMemo;
	}

	@Override
	public RicetteDaApprovareDao creaRicetteDaApprovareDao() {
		return ricetteDaApprovareDaoMemo;
	}

}
