package com.foodie.model.dao.memo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.foodie.model.dao.UtenteDao;

public class UtenteDaoMemo implements UtenteDao {

	private static final Map<String, String> usernamePassword = new HashMap<>();
    private static final Map<String, Integer> usernameRuolo = new HashMap<>();

    @Override
    public int validazioneLogin(String username, String password) throws SQLException {
        String storedPassword = usernamePassword.get(username);
        if (storedPassword != null && storedPassword.equals(password)) {
            return usernameRuolo.get(username); //restituisce il ruolo associato
        }
        return -1; //login fallito
    }

    @Override
    public int controllaUsername(String username) throws SQLException {
        return usernamePassword.containsKey(username) ? 0 : 1;
    }

    @Override
    public void registraUtente(String nome, String cognome, String username, int ruolo, String password) throws SQLException {
    	//per la versione demo nome e cognome sono stati ignorati
        if (usernamePassword.containsKey(username)) {
            return; //username già presente, non registrato
        }
        usernamePassword.put(username, password);
        usernameRuolo.put(username, ruolo);
    }

}
