package com.foodie.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.foodie.model.Alimento;
import com.foodie.model.Dispensa;
import com.foodie.model.QuerySQLChef;
import com.foodie.model.QuerySQLUtente;
import com.foodie.model.Ricetta;
import com.foodie.model.RicettaDuplicataException;

public class RicettaDaoDB implements RicettaDao {
	private static final Logger logger = Logger.getLogger(UtenteDaoDB.class.getName());

	@Override
	public List<Ricetta> trovaRicette(Dispensa dispensa, int difficolta, String autore) throws SQLException { //TROVA LE RICETTE NEL DB O PER ALIMENTI-DIFFICOLTA' O PER AUTORE
		List<Ricetta> ricetteTrovate = new ArrayList<>();

	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    // Controllo se la dispensa è vuota
	    if (dispensa != null && dispensa.getAlimenti().isEmpty()) {
	        logger.info("Dispensa vuota!!! Riempila prima");
	        return ricetteTrovate;
	    }

	    ResultSet risultati;

	    if (dispensa != null) {
	        // Query per alimenti e difficoltà
	        risultati = QuerySQLUtente.trovaRicette(connessione, dispensa.getAlimenti(), difficolta);
	    } else {
	        // Query per autore
	        risultati = QuerySQLChef.ottieniRicettePersonali(connessione, autore);
	    }

	    String nomeCorrente = null;
	    Ricetta ricettaCorrente = null;

	    while (risultati.next()) {
	        String nome = risultati.getString("nome");

	        if (!nome.equals(nomeCorrente)) {
	            // Nuova ricetta
	            String descrizione = risultati.getString("descrizione");
	            int difficoltaRicetta = risultati.getInt("difficolta");
	            String autore1 = risultati.getString("autore");

	            ricettaCorrente = new Ricetta(nome, descrizione, difficoltaRicetta, new ArrayList<>(), autore1, new ArrayList<>());
	            ricetteTrovate.add(ricettaCorrente);

	            nomeCorrente = nome;
	        }

	        // Aggiungo ingrediente alla ricetta corrente
	        String alimento = risultati.getString("alimento");
	        String quantita = risultati.getString("quantita");

	        if (alimento != null && ricettaCorrente != null) {
	            ricettaCorrente.aggiungiIngrediente(new Alimento(alimento), quantita);
	        }
	    }

	    if (ricetteTrovate.isEmpty()) {
	        logger.info("Nessuna ricetta trovata");
	    } else {
	        logger.info("Ricette trovate: " + ricetteTrovate.size());
	    }

	    return ricetteTrovate;
	}

	@Override
	public void aggiungiRicetta(Ricetta ricetta) throws SQLException, RicettaDuplicataException {
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
        ResultSet risultati= null;
        int codiceDiRitorno=0;
        risultati = QuerySQLChef.selezionaRicetteDalNomeAutore(connessione);  //CONTROLLO SE GIA' ESISTE UNA RICETTA CON LA STESSA DESCRIZIONE
        while (risultati.next()) {  //CONTROLLO I RISULTATI
        	String nome = risultati.getString("nome");
            String autore= risultati.getString("autore");
            if (nome.equals(ricetta.getNome()) && autore.equals(ricetta.getAutore())){
            	throw new RicettaDuplicataException("Ricetta già esistente nel database!");
            }
        }
        risultati.close(); 
        if((codiceDiRitorno = QuerySQLChef.inserisciRicetta(connessione, ricetta))==1) {  //EFFETTUO LA QUERY PER INSERIRE LA RICETTA
        	logger.info("Ricetta aggiunta al database");  //SE 1 OK
        }
        else if(codiceDiRitorno == 0){
        	logger.info("Ricetta non aggiunta al database");  //SE 0 ERRORE
        }
        else {
        	eliminaRicetta(ricetta.getNome(),ricetta.getAutore());  //SE ALTRO SIGNIFICA CHE NON HA RIEMPITO ENTRAMBI I DB E QUINDI PROCEDE AD ELIMINARLA DAL PRIMO(IL DBMS GESTISCE L'ELIMINAZIONE A CASCATA)
            logger.info("Ricetta aggiunta al database solo parzialmente-->procedo a eliminarla");
        }
	}
	
	public void aggiungiRicettaNew(Ricetta ricetta) throws SQLException, RicettaDuplicataException {
	}

	@Override
	public void eliminaRicetta(String nome, String autore) throws SQLException { 
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
		if(QuerySQLChef.rimuoviRicetta(connessione, nome,autore) > 0) {  //query per eliminare ricetta dal DB
			logger.info("Ricetta eliminata dal database");  
        }
        else {
         	logger.info("Ricetta non eliminata dal database o non presente");
        }
	}

	@Override
	public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException {
		Connection connessione = DBConnection.ottieniIstanza().getConnection();
		ResultSet risultati = null;
		Ricetta ricetta = new Ricetta();
		risultati = QuerySQLUtente.ottieniRicetta(connessione, nome ,autore);  //query per ottenere la ricetta
		while(risultati.next()) {  //scorro i risultati e creo la ricetta 
			String nomeRicetta = risultati.getString("nome");
		    String descrizione = risultati.getString("descrizione");
		    int difficoltaRicetta = risultati.getInt("difficolta");
		    String autoreRicetta = risultati.getString("autore");
		    ricetta.setNome(nomeRicetta);
		    ricetta.setDescrizione(descrizione);
		    ricetta.setDifficolta(difficoltaRicetta);
		    ricetta.setAutore(autoreRicetta);
		    do {
		    	String alimento = risultati.getString("alimento");
		        String quantita = risultati.getString("quantita");
		        ricetta.aggiungiIngrediente(new Alimento(alimento), quantita);
		    } while (risultati.next());
		}
			return ricetta;
	}

}
