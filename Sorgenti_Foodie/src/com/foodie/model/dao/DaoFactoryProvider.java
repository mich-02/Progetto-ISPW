package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;
import com.foodie.applicazione.Persistenza;
import com.foodie.model.dao.db.DBDaoFactory;
import com.foodie.model.dao.memo.MemoDaoFactory;

public final class DaoFactoryProvider {
	private static DaoFactoryProvider istanza = null; 
    private DaoFactory daoFactory; 

    // Costruttore privato per impedire istanziazione esterna
    private DaoFactoryProvider() {
        Persistenza persistenza = ConfiguratorePersistenza.getPersistenzaCorrente();
        if (persistenza == Persistenza.MEMORIA) {
            daoFactory = new MemoDaoFactory();
        } else if (persistenza == Persistenza.DATABASE) {
            daoFactory = new DBDaoFactory();
        } else {
            throw new IllegalStateException("Persistenza non configurata");
        }
    }

    public static DaoFactoryProvider ottieniIstanza() {
        if (istanza == null) {
            istanza = new DaoFactoryProvider();
        }
        return istanza;
    }

    // Metodo per ottenere la DaoFactory
    public DaoFactory getDaoFactory() {
        return daoFactory;
    }
}
