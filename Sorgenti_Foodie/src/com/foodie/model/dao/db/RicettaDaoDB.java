package com.foodie.model.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.foodie.exception.DaoException;
import com.foodie.exception.RicettaDuplicataException;
import com.foodie.model.Alimento;
import com.foodie.model.Ricetta;
import com.foodie.model.dao.DBConnection;
import com.foodie.model.dao.RicettaDao;

public class RicettaDaoDB implements RicettaDao {
	
	@Override
	public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws DaoException {
	    List<Ricetta> ricetteTrovate = new ArrayList<>();

	    String query =
	        "SELECT r.nome, r.descrizione, r.difficolta, r.autore, i.alimento, i.quantita " +
	        "FROM ricette r " +
	        "JOIN ingredienti i ON r.nome = i.nome_ricetta AND r.autore = i.autore_ricetta " +
	        "WHERE r.difficolta = ? " +
	        "AND NOT EXISTS ( " +
	        "    SELECT 1 " +
	        "    FROM ingredienti ing " +
	        "    WHERE ing.nome_ricetta = r.nome " +
	        "      AND ing.autore_ricetta = r.autore " +
	        "      AND ing.alimento NOT IN ( " +
	        "          SELECT d.alimento FROM dispense d WHERE d.username = ? " +
	        "      ) " +
	        ")";

	    Connection conn = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, difficolta);
	        ps.setString(2, username);

	        try (ResultSet rs = ps.executeQuery()) {
	            popolaListaRicette(rs, ricetteTrovate);
	        }
	    } catch (SQLException e) {
	        throw new DaoException("trovaRicettePerDispensa: " + e.getMessage());
	    }
	    
	    return ricetteTrovate;
	}

	@Override
	public List<Ricetta> trovaRicettePerAutore(String autore) throws DaoException {
	    List<Ricetta> ricetteTrovate = new ArrayList<>();

	    String query =
	        "SELECT r.nome, r.descrizione, r.difficolta, r.autore, i.alimento, i.quantita " +
	        "FROM ricette r " +
	        "LEFT JOIN ingredienti i ON r.nome = i.nome_ricetta AND r.autore = i.autore_ricetta " +
	        "WHERE r.autore = ?";

	    Connection conn = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, autore);

	        try (ResultSet rs = ps.executeQuery()) {
	            popolaListaRicette(rs, ricetteTrovate);
	        }
	    } catch (SQLException e) {
	        throw new DaoException("trovaRicettePerAutore: " + e.getMessage());
	    }

	    return ricetteTrovate;
	}

	// Metodo per popolare la lista di ricette dal ResultSet
	private void popolaListaRicette(ResultSet rs, List<Ricetta> lista) throws SQLException {
	    String nomeCorrente = null;
	    Ricetta ricettaCorrente = null;

	    while (rs.next()) {
	        String nome = rs.getString("nome");

	        if (!nome.equals(nomeCorrente)) {
	            String descrizione = rs.getString("descrizione");
	            int difficolta = rs.getInt("difficolta");
	            String autore = rs.getString("autore");

	            ricettaCorrente = new Ricetta(nome, descrizione, difficolta, new ArrayList<>(), autore, new ArrayList<>());
	            lista.add(ricettaCorrente);

	            nomeCorrente = nome;
	        }

	        String alimento = rs.getString("alimento");
	        String quantita = rs.getString("quantita");
	        if (alimento != null && ricettaCorrente != null) {
	            ricettaCorrente.aggiungiIngrediente(new Alimento(alimento), quantita);
	        }
	    }
	}

	@Override
	public void aggiungiRicetta(Ricetta ricetta) throws DaoException, RicettaDuplicataException {
	    String sqlEliminaDaApprovare = "DELETE FROM ricette_da_approvare WHERE nome = ? AND autore = ?";
	    String sqlInserisci = "INSERT INTO ricette (nome, descrizione, difficolta, autore) VALUES (?, ?, ?, ?)";
	    String sqlInserisciIngrediente = "INSERT INTO ingredienti (nome_ricetta, autore_ricetta, alimento, quantita) VALUES (?, ?, ?, ?)";

	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try {
	        // Inizio transazione
	        connessione.setAutoCommit(false);

	        // 1) Elimino dalla tabella ricette_da_approvare
	        try (PreparedStatement psDel = connessione.prepareStatement(sqlEliminaDaApprovare)) {
	            psDel.setString(1, ricetta.getNome());
	            psDel.setString(2, ricetta.getAutore());
	            psDel.executeUpdate();
	        }

	        // 2) Controllo se esiste già la ricetta definitiva
	        try (PreparedStatement ps = connessione.prepareStatement(
	                "SELECT 1 FROM ricette WHERE nome = ? AND autore = ?")) {
	            ps.setString(1, ricetta.getNome());
	            ps.setString(2, ricetta.getAutore());
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    throw new RicettaDuplicataException();
	                }
	            }
	        }

	        // 3) Inserimento della ricetta
	        try (PreparedStatement ps = connessione.prepareStatement(sqlInserisci)) {
	            ps.setString(1, ricetta.getNome());
	            ps.setString(2, ricetta.getDescrizione());
	            ps.setInt(3, ricetta.getDifficolta());
	            ps.setString(4, ricetta.getAutore());
	            ps.executeUpdate();
	        }

	        // 4) Inserimento degli ingredienti
	        try (PreparedStatement psIng = connessione.prepareStatement(sqlInserisciIngrediente)) {
	            for (int i = 0; i < ricetta.getIngredienti().size(); i++) {
	                psIng.setString(1, ricetta.getNome());
	                psIng.setString(2, ricetta.getAutore());
	                psIng.setString(3, ricetta.getIngredienti().get(i).getNome());
	                psIng.setString(4, ricetta.getQuantita().get(i));
	                psIng.addBatch();
	            }
	            psIng.executeBatch();
	        }

	        // Commit della transazione
	        connessione.commit();

	    } catch (SQLException e) {
	        // Rollback in caso di errore SQL
	        if (connessione != null) {
	            try {
	                connessione.rollback();
	            } catch (SQLException rollbackEx) {
	                throw new DaoException("aggiungiRicetta - Errore durante il rollback: " + rollbackEx.getMessage());
	            }
	        }
	        throw new DaoException("aggiungiRicetta - Errore durante l'aggiunta della ricetta: " + e.getMessage());

	    } finally {
	        // Riattiva l'auto-commit
	        if (connessione != null) {
	            try {
	                connessione.setAutoCommit(true);
	            } catch (SQLException e) {
	            	
	            }
	        }
	    }
	}
	
	@Override
	public void eliminaRicetta(String nome, String autore) throws DaoException { 
	    String sqlDelete = "DELETE FROM ricette WHERE nome = ? AND autore = ?";
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = connessione.prepareStatement(sqlDelete)) {
	        ps.setString(1, nome);
	        ps.setString(2, autore);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        throw new DaoException("eliminaRicetta: " + e.getMessage());
	    }
	}
	
	@Override
	public Ricetta ottieniDatiRicetta(String nome, String autore) throws DaoException {
	    String sqlQuery = 
	        "SELECT a.nome, a.autore, a.descrizione, a.difficolta, b.alimento, b.quantita " +
	        "FROM ricette a " +
	        "LEFT JOIN ingredienti b ON a.nome = b.nome_ricetta AND a.autore = b.autore_ricetta " +
	        "WHERE a.nome = ? AND a.autore = ?";

	    Ricetta ricetta = null;
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = connessione.prepareStatement(sqlQuery)) {
	        ps.setString(1, nome);
	        ps.setString(2, autore);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                if (ricetta == null) {
	                    // Creo la ricetta alla prima riga trovata
	                    ricetta = new Ricetta();
	                    ricetta.setNome(rs.getString("nome"));
	                    ricetta.setDescrizione(rs.getString("descrizione"));
	                    ricetta.setDifficolta(rs.getInt("difficolta"));
	                    ricetta.setAutore(rs.getString("autore"));
	                }

	                // Aggiungo gli ingredienti se presenti
	                String alimento = rs.getString("alimento");
	                String quantita = rs.getString("quantita");
	                if (alimento != null && quantita != null) {
	                    ricetta.aggiungiIngrediente(new Alimento(alimento), quantita);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        throw new DaoException("ottieniDatiRicetta: " + e.getMessage());
	    }

	    // Se la ricetta non è stata trovata, ritorna null
	    return ricetta;
	}
}
