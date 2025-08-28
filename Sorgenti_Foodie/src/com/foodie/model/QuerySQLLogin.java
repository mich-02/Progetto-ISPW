package com.foodie.model;

import java.sql.*;

public class QuerySQLLogin {  


//	private static final String FROM = "FROM user_account ";
//	private static final String WHERE = "WHERE username = '";
	
	private QuerySQLLogin() {
	}
	
	/*
	public static ResultSet effettuaLogin(Statement dichiarazione, String username, String password) throws SQLException { //old
		String query="SELECT count(1) "        
				+ FROM
				+ WHERE + username + "' AND password ='" + password + "'";
		
		String sqlQuery=String.format(query);
		return dichiarazione.executeQuery(sqlQuery);  //QUERY PER EFFETTUARE IL LOGIN SE REGISTRATO
	}
	
	public static ResultSet effettuaLogin(Connection conn, String username, String password) throws SQLException {
		String query = "SELECT COUNT(1) FROM user_account WHERE username = ? AND password = ?";
	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	    	ps.setString(1, username);
	    	ps.setString(2, password);
	    	return ps.executeQuery(); 
	    }
	}
	
	/*
	public static ResultSet controllaUsername(Statement dichiarazione,String username) throws SQLException { //old
		String query="SELECT COUNT(*) "
				 + FROM
				 + WHERE + username + "'";
		String sqlQuery= String.format(query);
		return dichiarazione.executeQuery(sqlQuery);  //QUERY PER CONTROLLARE L'USERNAME SE REGISTRATO
	}
	
	
	public static ResultSet controllaUsername(Connection conn, String username) throws SQLException {
		String query = "SELECT COUNT(*) FROM user_account WHERE username = ?";
	    PreparedStatement ps = conn.prepareStatement(query);
	    ps.setString(1, username);
	    return ps.executeQuery();
	}
	
	
	/*
	public static int registraUtente(Statement dichiarazione, String nome,String cognome,String username,int ruolo,String password) throws SQLException {
		String insertFields = "INSERT INTO user_account(nome,cognome,username,ruolo,password) VALUES ('";
    	String insertValues = nome + "','" + cognome + "','" + username + "'," + ruolo + ",'" + password + "');";
    	String query= insertFields+insertValues;
    	String sqlInsert = String.format(query);
    	return dichiarazione.executeUpdate(sqlInsert);  //QUERY PER REGISTRARE L'UTENTE
	}
	
	public static int registraUtente(Connection conn, String nome,String cognome,String username,int ruolo,String password) throws SQLException {
		String query = "INSERT INTO user_account (nome, cognome, username, ruolo, password) VALUES (?, ?, ?, ?, ?)";

	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, nome);
	        ps.setString(2, cognome);
	        ps.setString(3, username);
	        ps.setInt(4, ruolo);
	        ps.setString(5, password);
	        return ps.executeUpdate(); // restituisce il numero di righe inserite (1 se ok)
	    }
	}
	/*
	public static ResultSet controllaTipo(Statement dichiarazione, String username) throws SQLException { //old
		String query="SELECT ruolo "
				 + FROM
				 + WHERE+username+"' ";
		String sqlQuery= String.format(query);
		return dichiarazione.executeQuery(sqlQuery);  //QUERY PER OTTENERE IL TIPO D'UTENTE CHE SI è LOGGATO
	}
	
	
	
	public static int controllaTipo(Connection conn, String username) throws SQLException {
		String query = "SELECT ruolo FROM user_account WHERE username = ?";
		PreparedStatement ps = conn.prepareStatement(query)){
		ps.setString(1, username);
		return ps.executeQuery();
		}	
	}
	
	public static int controllaTipo(Connection conn, String username) throws SQLException {
	    String query = "SELECT ruolo FROM user_account WHERE username = ?";
	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, username);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    }
	    return -1; // se non trovato
	}
	*/
	
}