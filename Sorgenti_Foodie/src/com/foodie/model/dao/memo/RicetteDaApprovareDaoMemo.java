package com.foodie.model.dao.memo;

import java.sql.SQLException;

import com.foodie.model.Ricetta;
import com.foodie.model.RicetteDaApprovare;
import com.foodie.model.dao.RicetteDaApprovareDao;

public class RicetteDaApprovareDaoMemo implements RicetteDaApprovareDao {
	
	private static final RicetteDaApprovare ricetteDaApprovare = new RicetteDaApprovare();

    @Override
    public void caricaRicetteDaApprovare() throws SQLException {
        // non serve fare nulla in memoria
    }

    @Override
    public void salvaRicettaDaApprovare(Ricetta ricetta) throws SQLException{
        if (ricetta != null) {
            ricetteDaApprovare.aggiungiRicettaDaVerificare(ricetta);
        }
    }

    @Override
    public void rifiutaRicetta(Ricetta ricetta) {
        if (ricetta != null) {
            ricetteDaApprovare.ricettaVerificata(ricetta);
        }
    }

    public static RicetteDaApprovare getRicetteDaApprovare() {
        return ricetteDaApprovare;
    }
	/*
	private final RicetteDaApprovare ricetteDaApprovare = new RicetteDaApprovare();

    @Override
    public void caricaRicetteDaApprovare() throws SQLException {
        // In-memory: non carichiamo nulla da DB
    }

    @Override
    public void salvaRicettaDaApprovare(Ricetta ricetta) throws SQLException {
        if (ricetta != null && ricetta.getNome() != null && ricetta.getAutore() != null) {
            ricetteDaApprovare.aggiungiRicettaDaVerificare(ricetta);
        }
    }

    @Override
    public void rifiutaRicetta(Ricetta ricetta) throws SQLException {
        if (ricetta != null) {
            ricetteDaApprovare.ricettaVerificata(ricetta);
        }
    }
    */
}