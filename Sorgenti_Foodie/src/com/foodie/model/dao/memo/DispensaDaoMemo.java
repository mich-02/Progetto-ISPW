package com.foodie.model.dao.memo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.foodie.exception.DaoException;
import com.foodie.model.Alimento;
import com.foodie.model.Dispensa;
import com.foodie.model.dao.DispensaDao;

public class DispensaDaoMemo implements DispensaDao{
	
	// Mappa username -> copia della lista di alimenti dell'utente nella sessione
    private static final Map<String, List<Alimento>> memoriaUtenti = new HashMap<>();

    @Override
    public void salvaDispensa(String username) throws DaoException {
        // Copio la lista corrente della Dispensa per l'utente
        List<Alimento> alimentiCorrenti = Dispensa.ottieniIstanza().getAlimenti();
        memoriaUtenti.put(username, new ArrayList<>(alimentiCorrenti));
    }

    @Override
    public List<Alimento> caricaDispensa(String username) throws DaoException {
        // Recupeo la lista salvata per l'utente oppure creiamo una nuova
        List<Alimento> listaUtente = memoriaUtenti.computeIfAbsent(username, k -> new ArrayList<>()); 

        // Aggiorno la Dispensa con la copia dell'utente
        Dispensa dispensa = Dispensa.ottieniIstanza();
        dispensa.svuotaDispensa();
        for (Alimento a : listaUtente) {
            dispensa.aggiungiAlimento(a);
        }

        // Restituisco una copia per sicurezza
        return new ArrayList<>(listaUtente);
    }
}