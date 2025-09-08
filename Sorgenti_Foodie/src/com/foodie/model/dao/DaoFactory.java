package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;
import com.foodie.applicazione.Persistenza;

/*
public interface DaoFactory {
	public  UtenteDao creaUtenteDao(); 
	public  DispensaDao creaDispensaDao(); 
	public  RicetteDaApprovareDao creaRicetteDaApprovareDao();
	public  RicettaDao creaRicettaDao(); 
	
}
*/

public abstract class DaoFactory {
	
	public static DaoFactory getFactory() {
		Persistenza persistenza = ConfiguratorePersistenza.getPersistenzaCorrente();
        if (persistenza == Persistenza.MEMORIA) {
            //return new MemoDaoFactory();
        	return MemoDaoFactory.ottieniIstanza();
        } else if (persistenza == Persistenza.DATABASE) {
            //return new DBDaoFactory();
        	return DBDaoFactory.ottieniIstanza();
        } else {
            throw new IllegalStateException("Persistenza non configurata");
        }
    }
	
	public abstract UtenteDao creaUtenteDao(); 
	public abstract DispensaDao creaDispensaDao(); 
	public abstract RicetteDaApprovareDao creaRicetteDaApprovareDao();
	public abstract RicettaDao creaRicettaDao(); 
}


