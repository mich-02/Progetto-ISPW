package com.foodie.model.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.foodie.exception.DaoException;
import com.foodie.exception.NessunaRicettaDaApprovareException;
import com.foodie.model.Alimento;
import com.foodie.model.Ricetta;
import com.foodie.model.RicetteDaApprovare;
import com.foodie.model.dao.DBConnection;
import com.foodie.model.dao.RicetteDaApprovareDao;

public class RicetteDaApprovareDaoDB implements RicetteDaApprovareDao{
	
	private final RicetteDaApprovare ricetteDaApprovare = new RicetteDaApprovare();
	
	@Override
	public void caricaRicetteDaApprovare() throws DaoException, NessunaRicettaDaApprovareException {
		String sqlRicette = "SELECT nome, autore, difficolta, descrizione FROM ricette_da_approvare";
	    String sqlIngredienti = "SELECT alimento, quantita FROM ingredienti_da_approvare WHERE nome_ricetta = ? AND autore_ricetta = ?";

	    Connection conn = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement psRicette = conn.prepareStatement(sqlRicette);
	         ResultSet rsRicette = psRicette.executeQuery()) {

	        boolean trovataAlmenoUna = false;

	        while (rsRicette.next()) {
	            trovataAlmenoUna = true;

	            Ricetta ricetta = new Ricetta();
	            String nome = rsRicette.getString("nome");
	            String autore = rsRicette.getString("autore");
	            ricetta.setNome(nome);
	            ricetta.setAutore(autore);
	            ricetta.setDifficolta(rsRicette.getInt("difficolta"));
	            ricetta.setDescrizione(rsRicette.getString("descrizione"));

	            // Recupero ingredienti e quantità
	            List<Alimento> ingredienti = new ArrayList<>();
	            List<String> quantita = new ArrayList<>();

	            try (PreparedStatement psIng = conn.prepareStatement(sqlIngredienti)) {
	                psIng.setString(1, nome);
	                psIng.setString(2, autore);
	                try (ResultSet rsIng = psIng.executeQuery()) {
	                    while (rsIng.next()) {
	                        Alimento alimento = new Alimento(rsIng.getString("alimento"));
	                        ingredienti.add(alimento);
	                        quantita.add(rsIng.getString("quantita"));
	                    }
	                }
	            }

	            ricetta.setIngredienti(ingredienti);
	            ricetta.setQuantita(quantita);

	            ricetteDaApprovare.aggiungiRicettaDaVerificare(ricetta);
	        }

	        if (!trovataAlmenoUna) {
	            throw new NessunaRicettaDaApprovareException();
	        }

	    } catch (SQLException e) {
	        throw new DaoException("caricaRicetteDaApprovare: " + e.getMessage());
	    }
	}

	@Override
	public void salvaRicettaDaApprovare(Ricetta ricetta) throws DaoException {
	    String sqlRicetta = "INSERT INTO ricette_da_approvare (nome, autore, descrizione, difficolta) VALUES (?, ?, ?, ?)";
	    String sqlIngrediente = "INSERT INTO ingredienti_da_approvare (nome_ricetta, autore_ricetta, alimento, quantita) VALUES (?, ?, ?, ?)";

	    Connection conn = DBConnection.ottieniIstanza().getConnection();

	    try {
	        // Inserisco la ricetta
	        try (PreparedStatement ps = conn.prepareStatement(sqlRicetta)) {
	            ps.setString(1, ricetta.getNome());
	            ps.setString(2, ricetta.getAutore());
	            ps.setString(3, ricetta.getDescrizione());
	            ps.setInt(4, ricetta.getDifficolta());
	            ps.executeUpdate();
	        }

	        // Inserisco gli ingredienti collegati
	        try (PreparedStatement psIng = conn.prepareStatement(sqlIngrediente)) {
	            List<Alimento> ingredienti = ricetta.getIngredienti();
	            List<String> quantitaList = ricetta.getQuantita();

	            for (int i = 0; i < ingredienti.size(); i++) {
	                Alimento alimento = ingredienti.get(i);
	                String quantita = quantitaList.get(i);

	                psIng.setString(1, ricetta.getNome());
	                psIng.setString(2, ricetta.getAutore());
	                psIng.setString(3, alimento.getNome());
	                psIng.setString(4, quantita);
	                
	                psIng.addBatch(); // aggiunge l'istruzione al batch
	            }
	            psIng.executeBatch(); // esegue tutte le insert in un colpo solo     
	        }
	    } catch (SQLException e) {
	        throw new DaoException("salvaRicettaDaApprovare: " + e.getMessage());
	    }
	}

	@Override
	public void rifiutaRicetta(Ricetta ricetta) throws DaoException {
	    String sqlEliminaDaApprovare = "DELETE FROM ricette_da_approvare WHERE nome = ? AND autore = ?";
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = connessione.prepareStatement(sqlEliminaDaApprovare)) {
	        ps.setString(1, ricetta.getNome());
	        ps.setString(2, ricetta.getAutore());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        throw new DaoException("rifiutaRicetta: " + e.getMessage());
	    }
	}
}
