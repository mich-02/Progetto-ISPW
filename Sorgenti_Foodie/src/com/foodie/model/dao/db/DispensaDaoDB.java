package com.foodie.model.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.foodie.model.Alimento;
import com.foodie.model.Dispensa;
import com.foodie.model.dao.DBConnection;
import com.foodie.model.dao.DispensaDao;

public class DispensaDaoDB implements DispensaDao {
	private static final Logger logger = Logger.getLogger(DispensaDaoDB.class.getName());

	/*
	@Override
	public void salvaDispensa(String username) throws SQLException{
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
	    Dispensa dispensa = Dispensa.ottieniIstanza();
	    List<Alimento> alimenti = dispensa.getAlimenti();

	    if (alimenti == null || alimenti.isEmpty()) {
	        logger.info("Nessun alimento da salvare per " + username);
	        return;
	    }

	    connessione.setAutoCommit(false); // inizio transazione

	    try {
	        // Elimina i vecchi alimenti dell'utente
	        try (PreparedStatement deleteStmt = connessione.prepareStatement(
	                "DELETE FROM dispense WHERE username = ?")) {
	            deleteStmt.setString(1, username);
	            deleteStmt.executeUpdate();
	        }

	        // Inserisci i nuovi alimenti
	        try (PreparedStatement insertStmt = connessione.prepareStatement(
	                "INSERT INTO dispense (username, alimento) VALUES (?, ?)")) {
	            for (Alimento a : alimenti) {
	                insertStmt.setString(1, username);
	                insertStmt.setString(2, a.getNome());
	                insertStmt.addBatch();
	            }
	            insertStmt.executeBatch();
	        }

	        connessione.commit(); // commit della transazione
	        logger.info("Dispensa salvata nel database per utente: " + username);

	    } catch (SQLException e) {
	        connessione.rollback(); // rollback in caso di errore
	        logger.severe("Errore nel salvataggio della dispensa su DB: " + e.getMessage());

	    } finally {
	        connessione.setAutoCommit(true); // ripristino comportamento predefinito
	    }
	}
	*/
	@Override
	public void salvaDispensa(String username) throws SQLException{
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
	    Dispensa dispensa = Dispensa.ottieniIstanza();
	    List<Alimento> alimentiInMemoria = dispensa.getAlimenti();

	    if (alimentiInMemoria == null) {
	        alimentiInMemoria = new ArrayList<>();
	    }

	    connessione.setAutoCommit(false); // inizio transazione

	    try {
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
	            for (Alimento a : alimentiInMemoria) {
	                String nome = a.getNome();
	                if (nome == null || nome.isEmpty()) {
	                    logger.warning("Nome alimento nullo o vuoto, skip: " + a);
	                    continue;
	                }
	                if (!alimentiNelDB.contains(nome)) {
	                    insertStmt.setString(1, username);
	                    insertStmt.setString(2, nome);
	                    insertStmt.executeUpdate();
	                    logger.info("Inserito nuovo alimento: " + nome);
	                }
	            }
	        }

	        // Elimina gli alimenti che non sono più presenti in memoria
	        String deleteQuery = "DELETE FROM dispense WHERE username = ? AND alimento = ?";
	        try (PreparedStatement deleteStmt = connessione.prepareStatement(deleteQuery)) {
	            for (String nomeDB : alimentiNelDB) {
	                boolean ancoraPresente = alimentiInMemoria.stream()
	                        .anyMatch(a -> nomeDB.equals(a.getNome()));
	                if (!ancoraPresente) {
	                    deleteStmt.setString(1, username);
	                    deleteStmt.setString(2, nomeDB);
	                    deleteStmt.executeUpdate();
	                    logger.info("Eliminato alimento non più presente: " + nomeDB);
	                }
	            }
	        }

	        connessione.commit();
	        logger.info("Dispensa sincronizzata correttamente per utente: " + username);

	    } catch (SQLException e) {
	        connessione.rollback();
	        logger.severe("Errore nella sincronizzazione della dispensa per utente " + username + ": " + e.getMessage());
	        throw e;

	    } finally {
	        connessione.setAutoCommit(true);
	    }
	}

	@Override
	public List<Alimento> caricaDispensa(String username) throws SQLException{
		List<Alimento> alimenti = new ArrayList<>();
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    String query = "SELECT alimento FROM dispense WHERE username = ?";
	    try (PreparedStatement stmt = connessione.prepareStatement(query)) {
	        stmt.setString(1, username);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Alimento a = new Alimento(rs.getString("alimento"));
	                alimenti.add(a);
	            }
	        }
	    }

	    if (alimenti.isEmpty()) {
	        logger.warning("Nessuna dispensa salvata per l'utente: " + username);
	    }

	    return alimenti;
	}

}
