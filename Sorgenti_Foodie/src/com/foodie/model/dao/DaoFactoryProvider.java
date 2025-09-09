package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;
import com.foodie.applicazione.Persistenza;
import com.foodie.model.dao.db.DBDaoFactory;
import com.foodie.model.dao.memo.MemoDaoFactory;

public final class DaoFactoryProvider {
	private static DaoFactoryProvider istanza; 
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
	/*
	private static DaoFactory istanza;

    // Costruttore privato per impedire istanziazione
    private DaoFactoryProvider() {
    	
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
    */
}
