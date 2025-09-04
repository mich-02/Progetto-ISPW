package com.foodie.model.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.foodie.exception.DaoException;
import com.foodie.model.dao.DBConnection;
import com.foodie.model.dao.UtenteDao;

public class UtenteDaoDB implements UtenteDao {

	@Override
	public int validazioneLogin(String username, String password) throws DaoException {
	    String query = "SELECT ruolo FROM user_account WHERE username = ? AND password = ?";
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = connessione.prepareStatement(query)) {
	        ps.setString(1, username);
	        ps.setString(2, password);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("ruolo"); // login corretto, ritorna ruolo
	            }
	        }
	    } catch (SQLException e) {
	        throw new DaoException("validazioneLogin " + e.getMessage());
	    }

	    return -1; // login fallito
	}
	
	@Override
	public int controllaUsername(String username) throws DaoException {
	    String query = "SELECT COUNT(*) FROM user_account WHERE username = ?";
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = connessione.prepareStatement(query)) {
	        ps.setString(1, username);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                int count = rs.getInt(1);
	                return (count > 0) ? 0 : 1; // 0 = già presente, 1 = disponibile
	            }
	        }
	    } catch (SQLException e) {
	        throw new DaoException("controllaUsername " + e.getMessage());
	    }

	    return 1; // username disponibile se non trovato
	}
	
	@Override
	public void registraUtente(String nome, String cognome, String username, int ruolo, String password) throws DaoException {
	    String query = "INSERT INTO user_account (nome, cognome, username, ruolo, password) VALUES (?, ?, ?, ?, ?)";
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = connessione.prepareStatement(query)) {
	        ps.setString(1, nome);
	        ps.setString(2, cognome);
	        ps.setString(3, username);
	        ps.setInt(4, ruolo);
	        ps.setString(5, password);

	        ps.executeUpdate();
	    } catch (SQLException e) {
	        throw new DaoException("registraUtente " + e.getMessage());
	    }
	}
}
