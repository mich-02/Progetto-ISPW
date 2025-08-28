package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;
import com.foodie.model.dao.db.DBDaoFactory;
import com.foodie.model.dao.memo.MemoDaoFactory;

public final class DaoFactoryProvider {
	private static DaoFactory istanza;

    // Costruttore privato per impedire istanziazione
    private DaoFactoryProvider() {

    }

    public static DaoFactory ottieniIstanza() {
        if (istanza == null) {
            switch (ConfiguratorePersistenza.getPersistenzaCorrente()) {
            	case MEMORIA :
            		istanza = new MemoDaoFactory();
            		break;
            	case DATABASE:
            		istanza = new DBDaoFactory();
            		break;
            }		   
        }
        return istanza;
    }
}
