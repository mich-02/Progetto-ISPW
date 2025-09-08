package com.foodie.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.foodie.bean.AlimentoBean;
import com.foodie.bean.RicettaBean;
import com.foodie.exception.DaoException;
import com.foodie.exception.NessunAlimentoTrovatoException;
import com.foodie.exception.OperazioneNonEseguitaException;
import com.foodie.model.Alimento;
import com.foodie.model.Dispensa;
import com.foodie.model.LoggedUser;
import com.foodie.model.Observer;
import com.foodie.model.Ricetta;
import com.foodie.model.dao.DaoFactoryProvider;
import com.foodie.model.dao.DispensaDao;
import com.foodie.model.dao.RicettaDao;


public class TrovaRicettaController {  
	
	private Dispensa dispensa;
	private AlimentiAPI databaseAlimenti;
	private static final Logger logger = Logger.getLogger(TrovaRicettaController.class.getName());
	private RicettaDao ricettaDao;
	private DispensaDao dispensaDao;
	
	public TrovaRicettaController() {
		dispensa = Dispensa.ottieniIstanza();
		dispensaDao = DaoFactoryProvider.ottieniIstanza().creaDispensaDao();
		ricettaDao = DaoFactoryProvider.ottieniIstanza().creaRicettaDao();
		databaseAlimenti = new AlimentiAPI();
	}
	
	public void aggiungiInDispensa(AlimentoBean alimentoBean) {  
		Alimento alimento = new Alimento(alimentoBean.getNome());
		dispensa.aggiungiAlimento(alimento);
	}
	public void rimuoviDallaDispensa(AlimentoBean alimentoBean) {
		Alimento alimento = new Alimento(alimentoBean.getNome());
		dispensa.eliminaAlimento(alimento);
	}
	
	public void svuotaDispensa() {  
		dispensa.svuotaDispensa();
	}
	
	public List<RicettaBean> trovaRicetteUtente(int difficolta) {
		List<Ricetta> ricetteTrovate = null;
		LoggedUser utente = LoggedUser.ottieniIstanza();
		try {
			ricetteTrovate = ricettaDao.trovaRicettePerDispensa(difficolta, utente.getUsername());
			if(ricetteTrovate!=null) {
				ArrayList<RicettaBean> ricetteTrovateBean = new ArrayList<>();
				for(Ricetta r:ricetteTrovate) {
					RicettaBean ricettaBean = new RicettaBean();
					ricettaBean.setNome(r.getNome());
					ricettaBean.setDescrizione(r.getDescrizione());
					ricettaBean.setDifficolta(r.getDifficolta());
					ArrayList<AlimentoBean> alimentiTrovatiBean = new ArrayList<>();
					List<Alimento> alimentiTrovati = r.getIngredienti();
					for(Alimento a:alimentiTrovati) {
						AlimentoBean alimentoBean = new AlimentoBean();
						alimentoBean.setNome(a.getNome());
						alimentiTrovatiBean.add(alimentoBean);
					}
					ricettaBean.setIngredienti(alimentiTrovatiBean);
					ricettaBean.setAutore(r.getAutore());
					ricettaBean.setQuantita(r.getQuantita());
					ricetteTrovateBean.add(ricettaBean);
				}	
				mostraRicette(ricetteTrovate);
				return ricetteTrovateBean;
			}
			else {
				return new ArrayList<>();
			}
		}
		catch (DaoException e) {
			logger.severe("Errore nell'ottenimento delle ricette: " + e.getMessage());
			return new ArrayList<>();
		}
	}
	
	public void caricaDispense() {  //CARICA GLI INGREDIENTI DELLA DISPENSA DA DB
	    dispensa.svuotaDispensa(); // svuota prima di ricaricare
	    LoggedUser utente = LoggedUser.ottieniIstanza();

	    List<Alimento> alimentiDispensa;
	    try {
	        alimentiDispensa = dispensaDao.caricaDispensa(utente.getUsername());
	        if (alimentiDispensa != null && !alimentiDispensa.isEmpty()) {
		        for (Alimento a : alimentiDispensa) {
		            dispensa.aggiungiAlimento(a); 
		        }
		        logger.info("Dispensa caricata con successo per l'utente: " + utente.getUsername());
		    } else {
		        logger.info("La dispensa dell'utente " + utente.getUsername() + " è vuota.");
		    }
	    } catch (DaoException e) {
	    	logger.severe("Errore durante il caricamento della dispensa: " + e.getMessage());
	    }
	}
	
	public void salvaDispensa() {//SALVA GLI INGREDIENTI DELLA DISPENSA NEL DB
		LoggedUser utente = LoggedUser.ottieniIstanza();
		try {
			dispensaDao.salvaDispensa(utente.getUsername());
		} catch (DaoException e) {
			logger.severe("Errore durante il salvataggio della dispensa: " + e.getMessage());
		}
	}
	
	private void mostraRicette(List<Ricetta> ricette) {  //METODO PRIVATO PER STAMPARE SU CONSOLE TUTTE LE RICETTE
		for(Ricetta r: ricette) {  //UTILIZZATO PER COMODITA' NEL PROGETTO
			logger.info("nome: "+r.getNome()+"\ndescrizione: "+r.getDescrizione()+"\ndifficolta: "+r.getDifficolta()+"\nautore: "+r.getAutore()+"\nIngredienti: ");
			for(Alimento a: r.getIngredienti()) {
				logger.info(a.getNome());
			}
			logger.info("Quantità: ");
			for(String s: r.getQuantita()) {
				logger.info(s);
			}
		}
	}
	
	public List<AlimentoBean> trovaAlimenti(AlimentoBean alimentoBeanInserito) throws OperazioneNonEseguitaException {
		List<AlimentoBean> alimentiTrovatiBean = null;
		try {
			alimentiTrovatiBean = databaseAlimenti.trovaAlimenti(alimentoBeanInserito.getNome());
			return alimentiTrovatiBean;
		} catch (NessunAlimentoTrovatoException e) {
			throw new OperazioneNonEseguitaException("Nessun alimento trovato che inizi per " + alimentoBeanInserito.getNome());
		}
	}
	
	public List<AlimentoBean> mostraDispensa() {
		List<Alimento> alimentiInDispensa = null;
		alimentiInDispensa = dispensa.getAlimenti();
		if(alimentiInDispensa != null && !alimentiInDispensa.isEmpty()) {
			ArrayList<AlimentoBean> alimentiInDispensaBean = new ArrayList<>();
			for(Alimento a: alimentiInDispensa) {
				AlimentoBean alimentoBean = new AlimentoBean();
				alimentoBean.setNome(a.getNome());
				alimentiInDispensaBean.add(alimentoBean);
			}
			return alimentiInDispensaBean;
		}
		else {
			return new ArrayList<>();
		}
	}
	
	public RicettaBean ottieniRicetta(RicettaBean ricettaBean) { //METODO PER OTTENERE I DATI DI UNA RICETTA IN FUNZIONE DEL NOME-AUTORE
		Ricetta ricetta = null;
		try {
			ricetta = ricettaDao.ottieniDatiRicetta(ricettaBean.getNome(), ricettaBean.getAutore());
			if(ricetta!=null) {
				ricettaBean.setNome(ricetta.getNome());
				ricettaBean.setDescrizione(ricetta.getDescrizione());
				ricettaBean.setDifficolta(ricetta.getDifficolta());
				ArrayList<AlimentoBean> alimentiTrovatiBean = new ArrayList<>();
				List<Alimento> alimentiTrovati = ricetta.getIngredienti();
				for(Alimento a:alimentiTrovati) {
					AlimentoBean alimentoBean = new AlimentoBean();
					alimentoBean.setNome(a.getNome());
					alimentiTrovatiBean.add(alimentoBean);
				}
				ricettaBean.setIngredienti(alimentiTrovatiBean);
				ricettaBean.setAutore(ricetta.getAutore());
				ricettaBean.setQuantita(ricetta.getQuantita());
				return ricettaBean;
			}
			else {
				return null;
			}
		} catch (DaoException e) {
			logger.severe("errore nell'ottenimento dei dati della ricetta: " + e.getMessage());
			return null;
		}
	}
	
	public void registraOsservatoreDispensa(Observer observer) {
		dispensa.registra(observer);
	}	
}