package com.foodie.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import com.foodie.model.AreaPersonaleImplementazioneDao;
import com.foodie.model.Chef;
import com.foodie.model.Dispensa;
import com.foodie.model.DispensaDao;
import com.foodie.model.Alimento;
import com.foodie.model.AlimentoSerializzabile;
import com.foodie.model.AreaPersonaleDao;
import com.foodie.model.DispensaImplementazioneDao;
import com.foodie.model.LoginDao;
import com.foodie.model.LoginImplementazioneDao;
import com.foodie.model.Moderatore;
import com.foodie.model.Standard;
import com.foodie.model.Utente;
import com.foodie.model.UtenteEsistenteException;

public class LoginController {  //SINGLETON, IL CONTROLLER DEVE AVERE SOLO 1 ISTANZA!
	
	private static LoginController istanza;
	private static Utente utente=null;
	private static LoginDao database;
	private static DispensaDao databaseDispensa= DispensaImplementazioneDao.ottieniIstanza();
	private static AreaPersonaleDao databaseAreaPersonale= AreaPersonaleImplementazioneDao.ottieniIstanza();
	private static final String MESSAGGIO= "PROBLEMA CON IL DB";
	private static final Logger logger = Logger.getLogger(LoginController.class.getName());
	
	private LoginController() {
	}
	
	public static synchronized LoginController ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza == null) {
			istanza = new LoginController();
			try {
				database= LoginImplementazioneDao.ottieniIstanza();
			} catch (IOException e) {
				logger.severe("PROBLEMA CON IL COLLEGAMENTO DEL DB! TERMINO L'APPLICAZIONE");
				System.exit(0);
			}
		}
		return istanza;
	}
	
	public static void setUtente(String username, String tipo) {  //ISTANZIA L'UTENTE IN FUNZIONE DEL TIPO
		if(tipo.equals("Standard")) {
			utente= new Standard(username);
		}
		else if(tipo.equals("Chef")) {
			utente= new Chef(username);
		}
		else {
			utente= Moderatore.ottieniIstanza(username);
		}
		logger.info(username);
	}
	
	public String ottieniView(int interfaccia) {  //RESTITUISCE LA VIEW DA CARICARE
		return utente.getViewIniziale(interfaccia);
	}
	
	public int effettuaLogin(String username, String pwd) {  //EFFETTUA IL LOGIN
		try {
			return database.validazioneLogin(username, pwd);
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELLA FASE DI LOGIN");
			logger.info(MESSAGGIO);
			return -1;
		}
	}
	
	public int controllaUsername(String username) {  //CONTROLLA L'USERNAME SE ESISTE
		try {
			if(database.controllaUsername(username)==0) {
				throw new UtenteEsistenteException("Ricetta già esistente nel database!");
			}
			return 1;
		}catch(UtenteEsistenteException e) {
			e.suggerimento();
			logger.severe("USERNAME GIA' IN USO");
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NEL CONTROLLO DELL'USERNAME-->RIPROVA,LA GUI TI DIRA' ESISTENTE");
			logger.info(MESSAGGIO);
			return 0;  //LO FACCIO PASSARE PER ESISTENTE
		}
	}
	
	public void registraUtente(String nome,String cognome,String username,int ruolo,String password) {  //REGISTRA L'UTENTE SUL DB
		try {
			database.registraUtente(nome, cognome, username, ruolo, password);
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELLA REGISTRAZIONE DELL'UTENTE");
			logger.info(MESSAGGIO);
		}
	}
	
	public void salvaDispensa() {  //SALVA GLI INGREDIENTI DELLA DISPENSA NEL DB
		databaseDispensa.salvaDispensa(utente.getUsername());
	}
	
	public void caricaDispense() {  //CARICA GLI INGREDIENTI DELLA DISPENSA DA DB
		Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap=databaseDispensa.caricaDispense(); 
		Dispensa dispensa= Dispensa.ottieniIstanza();
		ArrayList<AlimentoSerializzabile> dispensaOld=dispensaMap.get(utente.getUsername());
		if(dispensaOld!=null) {
			for(AlimentoSerializzabile a: dispensaOld) {
				dispensa.aggiungiAlimento(new Alimento(a.getNome()));
			}
			logger.info("dispensa caricata");
		}
	}
	
	public Utente getUtente() {
		return utente;
	}
	
	public void salvaAreaPersonale(String descrizione) {  //SALVA LA DESCRIZIONE DELL'AREA PERSONALE NEL DB
		databaseAreaPersonale.salvaAreaPersonale(utente.getUsername(), descrizione);
	}
	
	public Map<String,String> caricaAreaPersonale() {  //CARICA LA DESCRIZIONE DELL'AREA PERSONALE DA DB
		return databaseAreaPersonale.caricaAreaPersonale();
    }
	
}