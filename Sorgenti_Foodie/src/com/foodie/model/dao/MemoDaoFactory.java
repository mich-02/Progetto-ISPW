package com.foodie.model.dao;

import com.foodie.model.dao.memo.DispensaDaoMemo;
import com.foodie.model.dao.memo.RicettaDaoMemo;
import com.foodie.model.dao.memo.RicetteDaApprovareDaoMemo;
import com.foodie.model.dao.memo.UtenteDaoMemo;

public class MemoDaoFactory extends DaoFactory { 
	private static MemoDaoFactory istanza = null;
	
	private MemoDaoFactory() {
	}
	
	public static MemoDaoFactory ottieniIstanza() {
		if(istanza == null) {
			istanza = new MemoDaoFactory();
		}
		return istanza;
	}
	
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
		return new RicettaDaoMemo(this.creaDispensaDao(), this.creaRicetteDaApprovareDao());
	}

	@Override
	public RicetteDaApprovareDao creaRicetteDaApprovareDao() {
		return new RicetteDaApprovareDaoMemo();
	}

}
