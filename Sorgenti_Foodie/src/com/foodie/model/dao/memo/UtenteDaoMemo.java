package com.foodie.model.dao.memo;

import java.sql.SQLException;

import com.foodie.model.dao.UtenteDao;

public class UtenteDaoMemo implements UtenteDao {

	@Override
	public int validazioneLogin(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int controllaUsername(String username) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registraUtente(String nome, String cognome, String username, int ruolo, String password)
			throws SQLException {
		// TODO Auto-generated method stub

	}

}
