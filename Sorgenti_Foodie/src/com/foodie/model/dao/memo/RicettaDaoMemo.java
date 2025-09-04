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

    /*
    public void aggiungiRicetta(Ricetta ricetta) throws DaoException, RicettaDuplicataException {
    	 //Rimuovo la ricetta dalla lista delle ricette da approvare
        ricetteDaApprovareDao.rifiutaRicetta(ricetta);

        //Recupero la lista di ricette dell'autore, oppure ne creo una nuova
        List<Ricetta> ricette = ricettePerAutore.get(ricetta.getAutore());
        if (ricette == null) {
            ricette = new ArrayList<>();
            ricettePerAutore.put(ricetta.getAutore(), ricette);
        }
        
        //Aggiungo la ricetta alla lista definitiva
        ricette.add(ricetta);
    }
    */
    

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
    

	/*
	private static final List<Ricetta> ricette = new ArrayList<>();

    @Override
    public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws SQLException{
        List<Alimento> alimentiUtente = Dispensa.ottieniIstanza().getAlimenti();
        List<Ricetta> trovate = new ArrayList<>();

        for (Ricetta r : ricette) {
            if (r.getDifficolta() != difficolta) continue;

            boolean tuttiPresenti = r.getIngredienti().stream()
                    .allMatch(alimento -> alimentiUtente.contains(alimento));

            if (tuttiPresenti) trovate.add(r);
        }

        return trovate;
    }

    @Override
    public List<Ricetta> trovaRicettePerAutore(String autore) throws SQLException {
        List<Ricetta> trovate = new ArrayList<>();
        for (Ricetta r : ricette) {
            if (r.getAutore().equals(autore)) trovate.add(r);
        }
        return trovate;
    }

    @Override
    public void aggiungiRicetta(Ricetta ricetta) throws SQLException,  RicettaDuplicataException {
        for (Ricetta r : ricette) {
            if (r.getNome().equals(ricetta.getNome()) && r.getAutore().equals(ricetta.getAutore())) {
                throw new RicettaDuplicataException("Ricetta già esistente!");
            }
        }
        ricette.add(ricetta);
    }

    @Override
    public void eliminaRicetta(String nome, String autore) throws SQLException{
        ricette.removeIf(r -> r.getNome().equals(nome) && r.getAutore().equals(autore));
    }

    @Override
    public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException{
        return ricette.stream()
                .filter(r -> r.getNome().equals(nome) && r.getAutore().equals(autore))
                .findFirst()
                .orElse(new Ricetta());
    }
    */
	/*
	private static final List<Ricetta> ricette = new ArrayList<>();
    private final DispensaDaoMemo dispensaDao = new DispensaDaoMemo(); // condividono dati statici

    @Override
    public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws SQLException {
        List<Ricetta> trovate = new ArrayList<>();
        List<Alimento> alimentiUtente = dispensaDao.caricaDispensa(username);

        for (Ricetta r : ricette) {
            if (r.getDifficolta() != difficolta) continue;

            boolean tuttiPresenti = true;
            for (Alimento ingrediente : r.getIngredienti()) {
                boolean presente = alimentiUtente.stream()
                        .anyMatch(a -> a.getNome().equals(ingrediente.getNome()));
                if (!presente) {
                    tuttiPresenti = false;
                    break;
                }
            }

            if (tuttiPresenti) trovate.add(r);
        }

        return trovate;
    }

    @Override
    public List<Ricetta> trovaRicettePerAutore(String autore) throws SQLException {
        List<Ricetta> trovate = new ArrayList<>();
        for (Ricetta r : ricette) {
            if (r.getAutore().equals(autore)) trovate.add(r);
        }
        return trovate;
    }

    @Override
    public void aggiungiRicetta(Ricetta ricetta) throws SQLException, RicettaDuplicataException {
        for (Ricetta r : ricette) {
            if (r.getNome().equals(ricetta.getNome()) && r.getAutore().equals(ricetta.getAutore())) {
                throw new RicettaDuplicataException("Ricetta già esistente in memoria!");
            }
        }
        ricette.add(ricetta);
    }

    @Override
    public void eliminaRicetta(String nome, String autore) throws SQLException {
        ricette.removeIf(r -> r.getNome().equals(nome) && r.getAutore().equals(autore));
    }

    @Override
    public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException {
        for (Ricetta r : ricette) {
            if (r.getNome().equals(nome) && r.getAutore().equals(autore)) return r;
        }
        return new Ricetta();
    }
    */
    
	/*
    private final List<Ricetta> ricette = new ArrayList<>();
    private final DispensaDaoMemo dispensaDao = new DispensaDaoMemo(); // riferimento alla classe che salva le dispense

    @Override
    public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws SQLException {
        List<Ricetta> trovate = new ArrayList<>();

        // Recuperiamo la dispensa dell'utente
        List<Alimento> alimentiUtente = dispensaDao.caricaDispensa(username);

        for (Ricetta r : ricette) {
            if (r.getDifficolta() == difficolta) {
                // controlliamo se tutti gli ingredienti della ricetta sono presenti nella dispensa
                boolean tuttiPresenti = true;

                for (Alimento ingrediente : r.getIngredienti()) {
                    boolean presente = alimentiUtente.stream()
                        .anyMatch(a -> a.getNome().equals(ingrediente.getNome()));
                    if (!presente) {
                        tuttiPresenti = false;
                        break;
                    }
                }

                if (tuttiPresenti) {
                    trovate.add(r);
                }
            }
        }

        return trovate;
    }

    @Override
    public List<Ricetta> trovaRicettePerAutore(String autore) throws SQLException {
        List<Ricetta> trovate = new ArrayList<>();
        for (Ricetta r : ricette) {
            if (r.getAutore().equals(autore)) {
                trovate.add(r);
            }
        }
        return trovate;
    }

    @Override
    public void aggiungiRicetta(Ricetta ricetta) throws SQLException, RicettaDuplicataException {
        for (Ricetta r : ricette) {
            if (r.getNome().equals(ricetta.getNome()) && r.getAutore().equals(ricetta.getAutore())) {
                throw new RicettaDuplicataException("Ricetta già esistente in memoria!");
            }
        }
        ricette.add(ricetta);
    }

    @Override
    public void eliminaRicetta(String nome, String autore) throws SQLException {
        ricette.removeIf(r -> r.getNome().equals(nome) && r.getAutore().equals(autore));
    }

    @Override
    public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException {
        for (Ricetta r : ricette) {
            if (r.getNome().equals(nome) && r.getAutore().equals(autore)) {
                return r;
            }
        }
        return new Ricetta();
    }
    */
}
