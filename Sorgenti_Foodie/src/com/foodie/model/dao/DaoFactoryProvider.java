package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;
import com.foodie.applicazione.Persistenza;
import com.foodie.model.dao.db.DBDaoFactory;
import com.foodie.model.dao.memo.MemoDaoFactory;

public final class DaoFactoryProvider {
	private static DaoFactoryProvider istanza = null;  

    private DaoFactoryProvider() {
    }
    
    public static DaoFactoryProvider ottieniIstanza() {
        if (istanza == null) {
            istanza = new DaoFactoryProvider();
        }
        return istanza;
    }
    
    public DaoFactory getDaoFactory() {
    	Persistenza persistenza = ConfiguratorePersistenza.getPersistenzaCorrente();
    	DaoFactory daoFactory;
        if (persistenza == Persistenza.MEMORIA) {
            daoFactory = MemoDaoFactory.ottieniIstanza();
        } else if (persistenza == Persistenza.DATABASE) {
            daoFactory = DBDaoFactory.ottieniIstanza();
        } else {
            throw new IllegalStateException("Persistenza non configurata");
        }
        return daoFactory;
    }
    
    
}
