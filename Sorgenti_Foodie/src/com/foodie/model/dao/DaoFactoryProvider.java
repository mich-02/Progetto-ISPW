package com.foodie.model.dao;

import com.foodie.applicazione.ConfiguratorePersistenza;
import com.foodie.applicazione.Persistenza;

public final class DaoFactoryProvider {
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
}
