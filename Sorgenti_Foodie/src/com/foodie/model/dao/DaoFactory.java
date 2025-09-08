package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;
import com.foodie.applicazione.Persistenza;
import com.foodie.model.dao.db.DBDaoFactory;
import com.foodie.model.dao.memo.MemoDaoFactory;

/*
public interface DaoFactory {
	public  UtenteDao creaUtenteDao(); 
	public  DispensaDao creaDispensaDao(); 
	public  RicetteDaApprovareDao creaRicetteDaApprovareDao();
	public  RicettaDao creaRicettaDao(); 
	
}
*/

public abstract class DaoFactory {
	
	private static DaoFactory istanza = null;
	
	protected DaoFactory() {

    }

    public static DaoFactory ottieniIstanza() {
        if (istanza == null) {
            Persistenza persistenza = ConfiguratorePersistenza.getPersistenzaCorrente();
            if (persistenza == Persistenza.MEMORIA) {
                istanza = new MemoDaoFactory();
            } else if (persistenza == Persistenza.DATABASE) {
                istanza = new DBDaoFactory();
            } else {
                throw new IllegalStateException("Persistenza non configurata");
            }
        }
        return istanza;
    }
    
    public abstract UtenteDao creaUtenteDao(); 
	public abstract DispensaDao creaDispensaDao(); 
	public abstract RicetteDaApprovareDao creaRicetteDaApprovareDao();
	public abstract RicettaDao creaRicettaDao(); 
}
