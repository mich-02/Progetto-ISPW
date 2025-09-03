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

	/*
	public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws SQLException {
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
	    }

	    if (ricetteTrovate.isEmpty()) {
	        logger.info("Nessuna ricetta trovata per utente " + username);
	    } else {
	        logger.info("Ricette trovate: " + ricetteTrovate.size() + " per utente " + username);
	    }

	    return ricetteTrovate;
	}
	*/
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

	/*
	public List<Ricetta> trovaRicettePerAutore(String autore) throws SQLException {
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
	    }

	    if (ricetteTrovate.isEmpty()) {
	        logger.info("Nessuna ricetta trovata per autore: " + autore);
	    } else {
	        logger.info("Ricette trovate per autore " + autore + ": " + ricetteTrovate.size());
	    }

	    return ricetteTrovate;
	}
	*/


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

	/*
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
	                    throw new RicettaDuplicataException("Ricetta già esistente nel database!");
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
	        logger.info("Ricetta e ingredienti aggiunti con successo");

	    } catch (SQLException | RicettaDuplicataException ex) {
	        // Rollback in caso di errore
	        if (connessione != null) {
	            try {
	                connessione.rollback();
	                logger.info("Transazione annullata: rollback eseguito");
	            } catch (SQLException e) {
	                logger.severe("Errore durante il rollback: " + e.getMessage());
	            }
	        }
	        throw ex; // rilancio l'eccezione
	    } finally {
	        // Riattiva l'auto-commit
	        if (connessione != null) {
	            try {
	                connessione.setAutoCommit(true);
	            } catch (SQLException e) {
	                logger.severe("Errore nel ripristinare auto-commit: " + e.getMessage());
	            }
	        }
	    }
	}
	*/
	
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
	/*
	public void eliminaRicetta(String nome, String autore) throws SQLException { 
		String sqlDelete = "DELETE FROM ricette WHERE nome = ? AND autore = ?";
		Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try (PreparedStatement ps = connessione.prepareStatement(sqlDelete)) {

	        ps.setString(1, nome);
	        ps.setString(2, autore);

	        int righeEliminate = ps.executeUpdate();

	        if (righeEliminate > 0) {
	            logger.info("Ricetta eliminata dal database");
	        } else {
	            logger.info("Ricetta non eliminata dal database o non presente");
	        }
	    }
	}
	*/
	
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

	/*
	public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException {
	    String sqlQuery = "SELECT a.nome, a.autore, a.descrizione, a.difficolta, b.alimento, b.quantita "
	                    + "FROM ricette a "
	                    + "LEFT JOIN ingredienti b ON a.nome = b.nome_ricetta AND a.autore = b.autore_ricetta "
	                    + "WHERE a.nome = ? AND a.autore = ?";

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
	    }

	    // Se la ricetta non è stata trovata, ritorna un oggetto vuoto oppure null
	    if (ricetta == null) {
	        ricetta = new Ricetta();
	    }

	    return ricetta;
	}
	*/
	
	/*
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
	*/
	
	
	/*
	public List<Ricetta> trovaRicette(Dispensa dispensa, int difficolta, String autore) throws SQLException {
	    List<Ricetta> ricetteTrovate = new ArrayList<>();

	    if (dispensa != null && dispensa.getAlimenti().isEmpty()) {
	        logger.info("Dispensa vuota!!! Riempila prima");
	        return ricetteTrovate;
	    }

	    	Connection connessione = DBConnection.ottieniIstanza().getConnection();

	        if (dispensa != null) {
	            // Query per alimenti e difficoltà
	            StringJoiner joiner = new StringJoiner(",", "", "");
	            for (int i = 0; i < dispensa.getAlimenti().size(); i++) {
	                joiner.add("?");
	            }

	            String query = "SELECT r.nome, r.descrizione, r.difficolta, r.autore, i.alimento, i.quantita " +
	                           "FROM Ricette r " +
	                           "JOIN Ingredienti i ON r.nome = i.nome_ricetta AND r.autore = i.autore_ricetta " +
	                           "WHERE r.nome NOT IN ( " +
	                           "    SELECT nome_ricetta FROM Ingredienti WHERE alimento NOT IN (" + joiner.toString() + ") " +
	                           ") AND r.difficolta = ?";

	            try (PreparedStatement ps = connessione.prepareStatement(query)) {

	                int index = 1;
	                for (Alimento a : dispensa.getAlimenti()) {
	                    ps.setString(index++, a.getNome());
	                }
	                ps.setInt(index, difficolta);

	                try (ResultSet rs = ps.executeQuery()) {
	                    popolaListaRicette(rs, ricetteTrovate);
	                }
	            }

	        } else {
	            // Query per autore
	            String sqlQuery = "SELECT a.nome, a.autore, a.descrizione, a.difficolta, b.alimento, b.quantita " +
	                              "FROM ricette a " +
	                              "LEFT JOIN ingredienti b ON a.nome = b.nome_ricetta AND a.autore = b.autore_ricetta " +
	                              "WHERE a.autore = ?";

	            try (PreparedStatement ps = connessione.prepareStatement(sqlQuery)) {
	                ps.setString(1, autore);

	                try (ResultSet rs = ps.executeQuery()) {
	                    popolaListaRicette(rs, ricetteTrovate);
	                }
	            }
	    }

	    if (ricetteTrovate.isEmpty()) {
	        logger.info("Nessuna ricetta trovata");
	    } else {
	        logger.info("Ricette trovate: " + ricetteTrovate.size());
	    }

	    return ricetteTrovate;
	}
	*/
	
	/*
	public List<Ricetta> trovaRicettePerDispensa(Dispensa dispensa, int difficolta) throws SQLException {
	    List<Ricetta> ricetteTrovate = new ArrayList<>();

	    if (dispensa == null || dispensa.getAlimenti() == null || dispensa.getAlimenti().isEmpty()) {
	        logger.info("Dispensa vuota. Aggiungi alimenti prima della ricerca.");
	        return ricetteTrovate;
	    }

	    String placeholders = generaPlaceholders(dispensa.getAlimenti().size());

	    String query =
	        "SELECT r.nome, r.descrizione, r.difficolta, r.autore, i.alimento, i.quantita " +
	        "FROM Ricette r " +
	        "JOIN Ingredienti i ON r.nome = i.nome_ricetta AND r.autore = i.autore_ricetta " +
	        "WHERE r.nome NOT IN ( " +
	        "  SELECT nome_ricetta FROM Ingredienti WHERE alimento NOT IN (" + placeholders + ") " +
	        ") AND r.difficolta = ?";

	    Connection conn = DBConnection.ottieniIstanza().getConnection();
	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        int idx = 1;
	        for (Alimento a : dispensa.getAlimenti()) {
	            ps.setString(idx++, a.getNome());
	        }
	        ps.setInt(idx, difficolta);

	        try (ResultSet rs = ps.executeQuery()) {
	            popolaListaRicette(rs, ricetteTrovate);
	        }
	    }

	    if (ricetteTrovate.isEmpty()) {
	        logger.info("Nessuna ricetta trovata con gli alimenti della dispensa.");
	    } else {
	        logger.info("Ricette trovate: " + ricetteTrovate.size());
	    }

	    return ricetteTrovate;
	}
	// Helper "Sonar-friendly": genera la stringa "?, ?, ?, ..."
	private String generaPlaceholders(int count) {
	    return String.join(",", java.util.Collections.nCopies(count, "?"));
	}
	*/
	
	/*
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
	*/
	/*
	@Override
	public void aggiungiRicetta(Ricetta ricetta) throws SQLException, RicettaDuplicataException {
		String sqlSeleziona = "SELECT nome, autore FROM ricette";
	    String sqlInserisci = "INSERT INTO ricette (nome, descrizione, difficolta, autore) VALUES (?, ?, ?, ?)";

	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    // Controllo se esiste già la ricetta
	    try (Statement stmt = connessione.createStatement();
	    		ResultSet rs = stmt.executeQuery(sqlSeleziona)) {

	            while (rs.next()) {
	                String nome = rs.getString("nome");
	                String autore = rs.getString("autore");
	                if (nome.equals(ricetta.getNome()) && autore.equals(ricetta.getAutore())) {
	                    throw new RicettaDuplicataException("Ricetta già esistente nel database!");
	                }
	            }
	        }
	    
	    // Inserimento della ricetta
	    int codiceDiRitorno = 0;
	    try (PreparedStatement ps = connessione.prepareStatement(sqlInserisci)) {
	    	ps.setString(1, ricetta.getNome());
	        ps.setString(2, ricetta.getDescrizione());
	        ps.setInt(3, ricetta.getDifficolta());
	        ps.setString(4, ricetta.getAutore());

	        codiceDiRitorno = ps.executeUpdate();
	    }

	    // Gestione del risultato dell'inserimento
	    if (codiceDiRitorno == 1) {
	    	logger.info("Ricetta aggiunta al database");
	    } else if (codiceDiRitorno == 0) {
	        logger.info("Ricetta non aggiunta al database");
	    } else {
	        // Inserimento parziale: log e gestione eventuale cancellazione
	        eliminaRicetta(ricetta.getNome(), ricetta.getAutore());
	        logger.info("Ricetta aggiunta al database solo parzialmente --> procedo a eliminarla");
	    }    
	}
	*/
	
	/*
	@Override
	public void aggiungiRicetta(Ricetta ricetta) throws SQLException, RicettaDuplicataException {
	    String sqlInserisci = "INSERT INTO ricette (nome, descrizione, difficolta, autore) VALUES (?, ?, ?, ?)";
	    String sqlInserisciIngrediente = "INSERT INTO ingredienti (nome_ricetta, autore_ricetta, alimento, quantita) VALUES (?, ?, ?, ?)";

	    Connection connessione = DBConnection.ottieniIstanza().getConnection();

	    try {
	        // Inizio transazione
	        connessione.setAutoCommit(false);

	        // Controllo se esiste già la ricetta
	        try (PreparedStatement ps = connessione.prepareStatement(
	                "SELECT 1 FROM ricette WHERE nome = ? AND autore = ?")) {
	            ps.setString(1, ricetta.getNome());
	            ps.setString(2, ricetta.getAutore());
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    throw new RicettaDuplicataException("Ricetta già esistente nel database!");
	                }
	            }
	        }

	        // Inserimento della ricetta
	        try (PreparedStatement ps = connessione.prepareStatement(sqlInserisci)) {
	            ps.setString(1, ricetta.getNome());
	            ps.setString(2, ricetta.getDescrizione());
	            ps.setInt(3, ricetta.getDifficolta());
	            ps.setString(4, ricetta.getAutore());
	            ps.executeUpdate();
	        }

	        // Inserimento degli ingredienti
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
	        logger.info("Ricetta e ingredienti aggiunti con successo");

	    } catch (SQLException | RicettaDuplicataException ex) {
	        // Rollback in caso di errore
	        if (connessione != null) {
	            try {
	                connessione.rollback();
	                logger.info("Transazione annullata: rollback eseguito");
	            } catch (SQLException e) {
	                logger.severe("Errore durante il rollback: " + e.getMessage());
	            }
	        }
	        throw ex; // rilancio l'eccezione
	    } finally {
	        // Riattiva l'auto-commit
	        if (connessione != null) {
	            try {
	                connessione.setAutoCommit(true);
	            } catch (SQLException e) {
	                logger.severe("Errore nel ripristinare auto-commit: " + e.getMessage());
	            }
	        }
	    }
	}
	*/
	
	/*
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
	*/
	
	/*
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
	*/


}
