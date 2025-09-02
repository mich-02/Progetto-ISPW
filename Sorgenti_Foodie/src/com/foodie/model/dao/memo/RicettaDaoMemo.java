package com.foodie.model.dao.memo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.foodie.model.Alimento;
import com.foodie.model.Dispensa;
import com.foodie.model.Ricetta;
import com.foodie.model.RicettaDuplicataException;
import com.foodie.model.dao.RicettaDao;

public class RicettaDaoMemo implements RicettaDao {

	@Override
	public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ricetta> trovaRicettePerAutore(String autore) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void aggiungiRicetta(Ricetta ricetta) throws SQLException, RicettaDuplicataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminaRicetta(String nome, String autore) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException {
		// TODO Auto-generated method stub
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
