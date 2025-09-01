package com.foodie.controller;

import java.util.logging.Logger;
import com.foodie.model.Chef;
import com.foodie.model.LoggedUser;
import com.foodie.model.Moderatore;
import com.foodie.model.Standard;
import com.foodie.model.Utente;
import com.foodie.model.UtenteBean;
import com.foodie.model.UtenteEsistenteException;
import com.foodie.model.dao.DaoFactoryProvider;
import com.foodie.model.dao.UtenteDao;


public class LoginController {
	
//	private static Utente utente = null;
	private static final String MESSAGGIO= "PROBLEMA CON IL DB";
	private static final Logger logger = Logger.getLogger(LoginController.class.getName());
	private LoggedUser utenteCorrente = LoggedUser.ottieniIstanza();
	private Utente utente;
	private UtenteDao utenteDao;
//	private DispensaDao dispensaDao;
	
	/*
	private LoginController() {
		try {
			database = LoginImplementazioneDao.ottieniIstanza();
		} catch (IOException e) {
			logger.severe("Problema nel collegamento con il DB, termino l'applicazione");
			System.exit(0);
		}
	}
	
	public static synchronized LoginController ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza == null) {
			istanza = new LoginController();
			try {
				database = LoginImplementazioneDao.ottieniIstanza();
			} catch (IOException e) {
				logger.severe("PROBLEMA CON IL COLLEGAMENTO DEL DB! TERMINO L'APPLICAZIONE");
				System.exit(0);
			}
		}
		return istanza;
	}
	*/
	
	public LoginController() {
		utenteDao = DaoFactoryProvider.ottieniIstanza().creaUtenteDao();
	}
	
	
	public void setUtente(String username, String tipo) {  //ISTANZIA L'UTENTE IN FUNZIONE DEL TIPO
		if(tipo.equals("Standard")) {
			utente = new Standard(username);
		}
		else if(tipo.equals("Chef")) {
			utente = new Chef(username);
		}
		else {
			//utente = Moderatore.ottieniIstanza(username);
			utente = new Moderatore(username);
		}
		utenteCorrente.setUtente(utente);
		logger.info(username);
	}
	
	
	public int effettuaLogin(String username, String pwd) {  //EFFETTUA IL LOGIN
		try {
			return utenteDao.validazioneLogin(username, pwd);
		} catch (Exception e) {
			logger.severe("Errore durante la fase di login.");
			return -1;
		}
	}
	
	public int controllaUsername(String username) {  //CONTROLLA L'USERNAME SE ESISTE
		try {
			if(utenteDao.controllaUsername(username) == 0) {
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
			utenteDao.registraUtente(nome, cognome, username, ruolo, password);
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELLA REGISTRAZIONE DELL'UTENTE");
			logger.info(MESSAGGIO);
		}
	}
	
	/*
	public void salvaDispensa() {  //SALVA GLI INGREDIENTI DELLA DISPENSA NEL DB
		databaseDispensa.salvaDispensa(utente.getUsername());
	}
	*/
	/*
	public void salvaDispensa() {  //SALVA GLI INGREDIENTI DELLA DISPENSA NEL DB
		try {
			dispensaDao.salvaDispensa(utente.getUsername());
		} catch (SQLException e) {
			logger.severe("Errore durante il salvataggio della dispensa: " + e.getMessage());
		}
	}
	*/
	
	/*
	public void caricaDispense() {  //CARICA GLI INGREDIENTI DELLA DISPENSA DA DB
		Dispensa dispensa = Dispensa.ottieniIstanza();
    	dispensa.svuotaDispensa();
		Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap = databaseDispensa.caricaDispense(); 
		//Dispensa dispensa = Dispensa.ottieniIstanza();
		ArrayList<AlimentoSerializzabile> dispensaOld = dispensaMap.get(utente.getUsername());
		if(dispensaOld!=null) {
			for(AlimentoSerializzabile a: dispensaOld) {
				dispensa.aggiungiAlimento(new Alimento(a.getNome()));
			}
			logger.info("dispensa caricata");
		}
	}
	*/
	/*
	public void caricaDispense() {  //CARICA GLI INGREDIENTI DELLA DISPENSA DA DB
		Dispensa dispensa = Dispensa.ottieniIstanza();
	    dispensa.svuotaDispensa(); // svuota prima di ricaricare

	    List<Alimento> alimentiDispensa;
	    try {
	        alimentiDispensa = dispensaDao.caricaDispensa(utente.getUsername());
	    } catch (SQLException e) {
	    	logger.severe("Errore durante il caricamento della dispensa: " + e.getMessage());
	        return; // esce dal metodo se c'è un errore
	    }

	    if (alimentiDispensa != null && !alimentiDispensa.isEmpty()) {
	        for (Alimento a : alimentiDispensa) {
	            dispensa.aggiungiAlimento(a); // usa direttamente l'oggetto recuperato
	        }
	        logger.info("Dispensa caricata con successo per l'utente: " + utente.getUsername());
	    } else {
	        logger.info("La dispensa dell'utente " + utente.getUsername() + " è vuota.");
	    }
	}
	*/
	
	
	public UtenteBean getUtente() {
		UtenteBean utenteBean = new UtenteBean();
		utenteBean.setUsername(LoggedUser.ottieniIstanza().getUsername());
		return utenteBean;
	}
	
	public String ottieniView(int interfacciaSelezionata) {  // restituisce la view iniziale da caricare
		return utente.getViewIniziale(interfacciaSelezionata);
	}
	
}