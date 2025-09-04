package com.foodie.model.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.foodie.exception.DaoException;
import com.foodie.model.Alimento;
import com.foodie.model.Dispensa;
import com.foodie.model.dao.DBConnection;
import com.foodie.model.dao.DispensaDao;

public class DispensaDaoDB implements DispensaDao {

	@Override
	public void salvaDispensa(String username) throws DaoException {
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();
	    Dispensa dispensa = Dispensa.ottieniIstanza();
	    List<Alimento> alimentiInMemoria = dispensa.getAlimenti();

	    if (alimentiInMemoria == null) {
	        alimentiInMemoria = new ArrayList<>();
	    }

	    try {
	        connessione.setAutoCommit(false); // inizio transazione

	        // Recupera gli alimenti già presenti nel DB
	        Set<String> alimentiNelDB = new HashSet<>();
	        String selectQuery = "SELECT alimento FROM dispense WHERE username = ?";
	        try (PreparedStatement selectStmt = connessione.prepareStatement(selectQuery)) {
	            selectStmt.setString(1, username);
	            try (ResultSet rs = selectStmt.executeQuery()) {
	                while (rs.next()) {
	                    alimentiNelDB.add(rs.getString("alimento"));
	                }
	            }
	        }

	        // Inserisci solo i nuovi alimenti
	        String insertQuery = "INSERT INTO dispense (username, alimento) VALUES (?, ?)";
	        try (PreparedStatement insertStmt = connessione.prepareStatement(insertQuery)) {
	        	insertStmt.setString(1, username);
	            for (Alimento a : alimentiInMemoria) {
	                String nome = a.getNome();
	                if (nome == null || nome.isEmpty()) {
	                    continue; // skip nomi nulli o vuoti
	                }
	                if (!alimentiNelDB.contains(nome)) {
	                    insertStmt.setString(2, nome);
	                    insertStmt.executeUpdate();
	                }
	            }
	        }

	        // Elimina gli alimenti che non sono più presenti in memoria
	        String deleteQuery = "DELETE FROM dispense WHERE username = ? AND alimento = ?";
	        try (PreparedStatement deleteStmt = connessione.prepareStatement(deleteQuery)) {
	        	deleteStmt.setString(1, username);
	            for (String nomeDB : alimentiNelDB) {
	                boolean ancoraPresente = alimentiInMemoria.stream()
	                        .anyMatch(a -> nomeDB.equals(a.getNome()));
	                if (!ancoraPresente) {
	                    deleteStmt.setString(2, nomeDB);
	                    deleteStmt.executeUpdate();
	                }
	            }
	        }

	        connessione.commit();

	    }  catch (SQLException ex) {
	        try {
	            connessione.rollback();
	        } catch (SQLException rollbackEx) {
	            throw new DaoException("salvaDispensa - rollback fallito: " + rollbackEx.getMessage());
	        }
	        throw new DaoException("salvaDispensa - errore SQL: " + ex.getMessage());
	    } finally {
	        try {
	            connessione.setAutoCommit(true); // ripristino autoCommit, ma NON chiudo la connessione
	        } catch (SQLException autoCommitEx) {
	            throw new DaoException("salvaDispensa - ripristino autoCommit fallito: " + autoCommitEx.getMessage());
	        }
	    }
	}
	
	@Override
	public List<Alimento> caricaDispensa(String username) throws DaoException {
	    List<Alimento> alimenti = new ArrayList<>();
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    String query = "SELECT alimento FROM dispense WHERE username = ?";

	    try (PreparedStatement stmt = connessione.prepareStatement(query)) {
	        stmt.setString(1, username);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                String nomeAlimento = rs.getString("alimento");
	                if (nomeAlimento != null && !nomeAlimento.isEmpty()) {
	                    alimenti.add(new Alimento(nomeAlimento));
	                }
	            }
	        }
	    } catch (SQLException e) {
	        throw new DaoException("caricaDispensa: " + e.getMessage());
	    }
	    
	    return alimenti;
	}
}
