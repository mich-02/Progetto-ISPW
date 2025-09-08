package com.foodie.model.dao.memo;

import com.foodie.model.dao.DaoFactory;
import com.foodie.model.dao.DispensaDao;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.RicetteDaApprovareDao;
import com.foodie.model.dao.UtenteDao;

public class MemoDaoFactory extends DaoFactory { 

	@Override
	public UtenteDao creaUtenteDao() {
		return new UtenteDaoMemo();
	}

	@Override
	public DispensaDao creaDispensaDao() {
		return new DispensaDaoMemo();
	}

	@Override
	public RicettaDao creaRicettaDao() {
		return new RicettaDaoMemo(creaDispensaDao(), creaRicetteDaApprovareDao());
	}

	@Override
	public RicetteDaApprovareDao creaRicetteDaApprovareDao() {
		return new RicetteDaApprovareDaoMemo();
	}

}
