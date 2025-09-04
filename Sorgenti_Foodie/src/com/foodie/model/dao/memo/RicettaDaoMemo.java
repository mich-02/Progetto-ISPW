package com.foodie.model.dao.memo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.foodie.exception.DaoException;
import com.foodie.exception.RicettaDuplicataException;
import com.foodie.model.Alimento;
import com.foodie.model.Ricetta;
import com.foodie.model.dao.DispensaDao;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.RicetteDaApprovareDao;

public class RicettaDaoMemo implements RicettaDao {

	private final DispensaDao dispensaDao;
	private final RicetteDaApprovareDao ricetteDaApprovareDao;
	
	// Map autore -> lista di ricette
    private static final Map<String, List<Ricetta>> ricettePerAutore = new HashMap<>();

    
    public RicettaDaoMemo(DispensaDao dispensaDao, RicetteDaApprovareDao ricetteDaApprovareDao) {
        this.dispensaDao = dispensaDao;
        this.ricetteDaApprovareDao = ricetteDaApprovareDao;
    }
    

    @Override
    public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws DaoException {
        List<Ricetta> risultato = new ArrayList<>();
        List<Alimento> alimentiUtente = dispensaDao.caricaDispensa(username);

        for (List<Ricetta> ricette : ricettePerAutore.values()) {
            for (Ricetta r : ricette) {
                if (r.getDifficolta() != difficolta) continue;

                boolean tuttiIngredientiPresenti = r.getIngredienti().stream()
                        .allMatch(ingrediente -> alimentiUtente.stream()
                                .anyMatch(a -> a.getNome().equals(ingrediente.getNome())));

                if (tuttiIngredientiPresenti) {
                    risultato.add(r);
                }
            }
        }
       
        return risultato;
    }

    @Override
    public List<Ricetta> trovaRicettePerAutore(String autore) throws DaoException {
        if (ricettePerAutore.containsKey(autore)) {
            return new ArrayList<>(ricettePerAutore.get(autore));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void aggiungiRicetta(Ricetta ricetta) throws DaoException, RicettaDuplicataException {
        // Rimuovo la ricetta dalla lista delle ricette da approvare
        ricetteDaApprovareDao.rifiutaRicetta(ricetta);

        // Recupero la lista di ricette dell'autore, oppure ne creo una nuova
        List<Ricetta> ricette = ricettePerAutore.get(ricetta.getAutore());
        if (ricette == null) {
            ricette = new ArrayList<>();
            ricettePerAutore.put(ricetta.getAutore(), ricette);
        }

        // Controllo duplicati 
        boolean esiste = ricette.stream()
                                 .anyMatch(r -> r.getNome().equals(ricetta.getNome()));
        if (esiste) {
            throw new RicettaDuplicataException();
        }

        // Aggiungo la ricetta alla lista definitiva
        ricette.add(ricetta);
    }

    @Override
    public void eliminaRicetta(String nome, String autore) throws DaoException {
        if (ricettePerAutore.containsKey(autore)) {
            List<Ricetta> ricette = ricettePerAutore.get(autore);
            ricette.removeIf(r -> r.getNome().equals(nome));
        }
    }

    @Override
    public Ricetta ottieniDatiRicetta(String nome, String autore) throws DaoException {
        if (ricettePerAutore.containsKey(autore)) {
            List<Ricetta> ricette = ricettePerAutore.get(autore);
            for (Ricetta r : ricette) {
                if (r.getNome().equals(nome)) {
                    return r;
                }
            }
        }
        return null; 
    }
}
