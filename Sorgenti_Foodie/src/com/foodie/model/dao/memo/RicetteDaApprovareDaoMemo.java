package com.foodie.model.dao.memo;

import java.util.ArrayList;
import java.util.List;
import com.foodie.exception.DaoException;
import com.foodie.exception.NessunaRicettaDaApprovareException;
import com.foodie.model.Ricetta;
import com.foodie.model.RicetteDaApprovare;
import com.foodie.model.dao.RicetteDaApprovareDao;

public class RicetteDaApprovareDaoMemo implements RicetteDaApprovareDao {
	
    private final RicetteDaApprovare ricetteDaApprovare1 = new RicetteDaApprovare();
	private static final List<Ricetta> ricetteDaApprovare = new ArrayList<>();
	
    @Override
    public void caricaRicetteDaApprovare() throws DaoException, NessunaRicettaDaApprovareException {
    	if (ricetteDaApprovare.isEmpty()) {
            throw new NessunaRicettaDaApprovareException();
        }
    	for(Ricetta r : ricetteDaApprovare) {
    		ricetteDaApprovare1.aggiungiRicettaDaVerificare(r);
    	}
    }

    @Override
    public void salvaRicettaDaApprovare(Ricetta ricetta) throws DaoException {
    	ricetteDaApprovare.add(ricetta);
    	
    }

    @Override
    public void rifiutaRicetta(Ricetta ricetta) throws DaoException {
    	ricetteDaApprovare.remove(ricetta);
    }
}