package com.foodie.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import com.foodie.model.QuerySQLLogin;


public class UtenteDaoDB implements UtenteDao {
	
	private static final Logger logger = Logger.getLogger(UtenteDaoDB.class.getName());

	/*
	@Override
	public int validazioneLogin(String username, String password) throws SQLException, ClassNotFoundException {
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
        ResultSet risultati1 = QuerySQLLogin.effettuaLogin(connessione, username, password);
        while(risultati1.next()) {
        	if(risultati1.getInt(1) == 1) {
        		risultati1.close();
				ResultSet risultati2 = QuerySQLLogin.controllaTipo(connessione, username);
				while(risultati2.next()) {
					int tipo = risultati2.getInt(1);
					if(tipo == 0) {
						logger.info("utente base");
					}
					else if(tipo==1){
						logger.info("utente chef");
					}
					else {
						logger.info("utente Moderatore");
					}
					risultati2.close();
					return tipo;
					}
				}
            }
            //risultati1.close();
            return -1;
	}
	*/
	
	@Override
	public int validazioneLogin(String username, String password) throws SQLException {
	    Connection connessione = DBConnection.ottieniIstanza().getConnection();
	    try (ResultSet rs = QuerySQLLogin.effettuaLogin(connessione, username, password)) {
	        if (rs.next() && rs.getInt(1) == 1) {
	        	return ottieniRuolo(username);
	        }
	    }
	    return -1; // login fallito
	}
	
	private int ottieniRuolo(String username) throws SQLException{
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
	    int tipo = QuerySQLLogin.controllaTipo(connessione, username);
	    switch (tipo) {
	       	case 0 -> logger.info("utente base");
	        case 1 -> logger.info("utente chef");
	        default -> logger.info("utente moderatore");
	        }
	        return tipo;	   
	}


	@Override
	public int controllaUsername(String username) throws SQLException{
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
		try (ResultSet risultati = QuerySQLLogin.controllaUsername(connessione, username)){
	        if(risultati.next()) {
	            int count = risultati.getInt(1);
	            if(count > 0) {
	            	return 0;
	            }
	        }
		}
        return 1; 
	}

	@Override
	public void registraUtente(String nome, String cognome, String username, int ruolo, String password) throws SQLException{
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
		if(QuerySQLLogin.registraUtente(connessione, nome, cognome, username, ruolo, password) == 1) {
			logger.info("Utente registrato");
		}
		else {
			logger.info("Utente non registrato");
		}
	}

}
