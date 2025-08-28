package com.foodie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuerySQLChef {
	
	private QuerySQLChef() {
	}
	
	public static ResultSet selezionaRicetteDalNomeAutore(Statement dichiarazione) throws SQLException{ //old
		String sqlQuery = "SELECT nome,autore FROM ricette";  //OTTIENI LE DESCRIZIONI DELLE RICETTE
		return dichiarazione.executeQuery(sqlQuery);
	}
	public static ResultSet selezionaRicetteDalNomeAutore(Connection conn) throws SQLException{
		String sqlQuery = "SELECT nome, autore FROM ricette";
	    try (Statement stmt = conn.createStatement()){
	    	return stmt.executeQuery(sqlQuery);
	    }
	}
	
	
	public static int inserisciRicetta(Statement dichiarazione, Ricetta ricetta) throws SQLException  { //old
		int codiceDiRitorno=0;  //INSERISCI NELLE 2 TABELLE LINKATE LA RICETTA
		String sqlInsert=String.format("INSERT INTO ricette (nome,descrizione,difficolta,autore) "
									 + "VALUES ( '%s', '%s', %d, '%s')", ricetta.getNome(), ricetta.getDescrizione(),ricetta.getDifficolta(), ricetta.getAutore());
        if(dichiarazione.executeUpdate(sqlInsert)==0) {
        	return 0;  //SE 0 SIGNIFICA FALLIMENTO SULLA PRIMA TABELLA DI CONSEGUENZA NON AGGIORNO LA SECONDA TABELLA
        }
        for(int i=0; i< ricetta.getIngredienti().size(); i++ ) {
        	sqlInsert=String.format("INSERT INTO ingredienti (nome_ricetta, autore_ricetta, alimento, quantita) "
        						  + "VALUES ('%s', '%s', '%s', '%s')", ricetta.getNome(),ricetta.getAutore(), ricetta.getIngredienti().get(i).getNome(), ricetta.getQuantita().get(i));
        	codiceDiRitorno=codiceDiRitorno+dichiarazione.executeUpdate(sqlInsert); //OGNI ALIMENTO CHE INSERISCO NELLA SECONDA TABELLA MI INCREMENTA DI 1 IL CONTATORE
        }
        if(codiceDiRitorno<ricetta.getIngredienti().size()) {
        	return -1; //SE IL CONTATORE è MINORE DEGLI ALIMENTI DA INSERIRE OVVIAMENTE HA FALLITO L'INSERIMENTO DEGLI ALIMENTI NELLA SECONDA TABELLA
        }
        return 1;
    }
	public static int inserisciRicetta(Connection conn, Ricetta ricetta) throws SQLException {
	    int codiceDiRitorno = 0;

	    // Inserimento nella tabella ricette
	    String sqlRicetta = "INSERT INTO ricette (nome, descrizione, difficolta, autore) VALUES (?, ?, ?, ?)";
	    try (PreparedStatement psRicetta = conn.prepareStatement(sqlRicetta)) {
	        psRicetta.setString(1, ricetta.getNome());
	        psRicetta.setString(2, ricetta.getDescrizione());
	        psRicetta.setInt(3, ricetta.getDifficolta());
	        psRicetta.setString(4, ricetta.getAutore());

	        if (psRicetta.executeUpdate() == 0) {
	            return 0; // fallimento inserimento ricetta
	        }
	    }

	    // Inserimento ingredienti
	    String sqlIngrediente = "INSERT INTO ingredienti (nome_ricetta, autore_ricetta, alimento, quantita) VALUES (?, ?, ?, ?)";
	    try (PreparedStatement psIngrediente = conn.prepareStatement(sqlIngrediente)) {
	        for (int i = 0; i < ricetta.getIngredienti().size(); i++) {
	            psIngrediente.setString(1, ricetta.getNome());
	            psIngrediente.setString(2, ricetta.getAutore());
	            psIngrediente.setString(3, ricetta.getIngredienti().get(i).getNome());
	            psIngrediente.setString(4, ricetta.getQuantita().get(i));

	            codiceDiRitorno += psIngrediente.executeUpdate();
	        }
	    }

	    if (codiceDiRitorno < ricetta.getIngredienti().size()) {
	        return -1; // fallimento inserimento ingredienti
	    }

	    return 1; // successo
	}
	
	//old
	public static int rimuoviRicetta(Statement dichiarazione, String nome, String autore) throws SQLException{ //ELIMINA LA RICETTA DAL DB (DBMS GESTISCE ELIMINAZIONE A CASCATA)
		String sqlDelete= String.format("DELETE FROM  ricette  "
									 + "WHERE nome = '%s' AND autore = '%s'", nome, autore);
		return dichiarazione.executeUpdate(sqlDelete);
	}
	public static int rimuoviRicetta(Connection conn, String nome, String autore) throws SQLException {
	    String sqlDelete = "DELETE FROM ricette WHERE nome = ? AND autore = ?";
	    try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
	        ps.setString(1, nome);
	        ps.setString(2, autore);
	        return ps.executeUpdate();
	    }
	}
	
	//old
	public static ResultSet ottieniRicettePersonali(Statement dichiarazione, String username) throws SQLException {  //OTTIENI TUTTE LE RICETTE DI UNO CHEF IN BASE AL SUO USERNAME
		String sqlQuery=String.format("SELECT a.nome, a.autore, a.descrizione, a.difficolta, b.alimento, b.quantita "
									+ "FROM ricette a LEFT JOIN ingredienti b ON a.nome = b.nome_ricetta AND a.autore = b.autore_ricetta "
									+ "WHERE a.autore = '%s'",username);
		return dichiarazione.executeQuery(sqlQuery);
	}
	
	public static ResultSet ottieniRicettePersonali(Connection conn, String username) throws SQLException {
	    String sqlQuery = "SELECT a.nome, a.autore, a.descrizione, a.difficolta, b.alimento, b.quantita "
	                    + "FROM ricette a "
	                    + "LEFT JOIN ingredienti b ON a.nome = b.nome_ricetta AND a.autore = b.autore_ricetta "
	                    + "WHERE a.autore = ?";
	    try (PreparedStatement ps = conn.prepareStatement(sqlQuery)){
	    	ps.setString(1, username);
	    	return ps.executeQuery(); 
	    }
	}
		
}