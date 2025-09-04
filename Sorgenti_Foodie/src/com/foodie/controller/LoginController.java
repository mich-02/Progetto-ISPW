package com.foodie.controller;

import java.util.logging.Logger;
import com.foodie.bean.CredenzialiBean;
import com.foodie.bean.UtenteBean;
import com.foodie.exception.DaoException;
import com.foodie.exception.UtenteEsistenteException;
import com.foodie.model.Chef;
import com.foodie.model.LoggedUser;
import com.foodie.model.Moderatore;
import com.foodie.model.Standard;
import com.foodie.model.Utente;
import com.foodie.model.dao.DaoFactoryProvider;
import com.foodie.model.dao.UtenteDao;


public class LoginController {
	
	private static final Logger logger = Logger.getLogger(LoginController.class.getName());
	private LoggedUser utenteCorrente = LoggedUser.ottieniIstanza();
	private Utente utente;
	private UtenteDao utenteDao;
	
	public LoginController() {
		utenteDao = DaoFactoryProvider.ottieniIstanza().creaUtenteDao();
	}
	
	private void setUtente(String username, int tipo) {
	//private void setUtente(String username, String tipo) {  //ISTANZIA L'UTENTE IN FUNZIONE DEL TIPO
		if(tipo == 0) {
			utente = new Standard(username);
		}
		else if(tipo == 1) {
			utente = new Chef(username);
		}
		else {
			utente = new Moderatore(username);
		}
		utenteCorrente.setUtente(utente); //a ogni login sovrascrivo
		logger.info(username);
	}
	
	public int effettuaLogin(CredenzialiBean credenzialiBean) {
	//public int effettuaLogin(String username, String pwd) {  
		try {
			int ruolo = utenteDao.validazioneLogin(credenzialiBean.getUsername(), credenzialiBean.getPassword());
			setUtente(credenzialiBean.getUsername(), ruolo);
			return ruolo;
			//return utenteDao.validazioneLogin(credenzialiBean.getUsername(), credenzialiBean.getPassword());
		} catch (DaoException e) {
			logger.severe("Errore durante la fase di login: " + e.getMessage());
			return -1;
		}
	}
	
	public int controllaUsername(CredenzialiBean credenzialiBean) throws UtenteEsistenteException {  //controlla se esiste già l'username
		try {
			if(utenteDao.controllaUsername(credenzialiBean.getUsername()) == 0) {
				logger.severe("username già presente");	
				throw new UtenteEsistenteException();
			}
			return 1;
		} catch (DaoException e) {
			logger.severe("errore nel controllo username: " + e.getMessage());
			return 0; //username esistente 
		}
	}
	
	public void registraUtente(CredenzialiBean credenzialiBean) { 
	//public void registraUtente(String nome,String cognome,String username,int ruolo,String password) {  
		try {
			utenteDao.registraUtente(credenzialiBean.getNome(), credenzialiBean.getCognome(), credenzialiBean.getUsername(), credenzialiBean.getRuolo(), credenzialiBean.getPassword());
		} catch (DaoException e) {
			logger.severe("errore nella registrazione dell'utente: " + e.getMessage());
		}
	}	
	
	public UtenteBean getUtente() {
		UtenteBean utenteBean = new UtenteBean();
		utenteBean.setUsername(LoggedUser.ottieniIstanza().getUsername());
		return utenteBean;
	}
	
	public String ottieniView(int interfacciaSelezionata) {  //restituisce la view iniziale da caricare
		return utente.getViewIniziale(interfacciaSelezionata);
	}
	
}