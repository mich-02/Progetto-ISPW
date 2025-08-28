package com.foodie.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.foodie.model.Alimento;
import com.foodie.model.AlimentoBean;
import com.foodie.model.CatalogoAlimentiDao;
import com.foodie.model.CatalogoAlimentiNutrixionixImplementazioneDao;
import com.foodie.model.CatalogoRicetteChefDao;
import com.foodie.model.CatalogoRicetteImplementazione2Dao;
import com.foodie.model.CatalogoRicetteImplementazioneDao;
import com.foodie.model.Dispensa;
import com.foodie.model.Ricetta;
import com.foodie.model.RicettaBean;
import com.foodie.model.UtenteBean;
import com.foodie.model.dao.ChefDao;
import com.foodie.model.dao.DaoFactoryProvider;
import com.foodie.model.dao.RicettaDao;

@SuppressWarnings("unused")
public class TrovaRicettaController {  //SINGLETON, IL CONTROLLER DEVE AVERE SOLO 1 ISTANZA!
	
//	private static TrovaRicettaController istanza;
	private static Dispensa dispensa;
//	private static CatalogoRicetteChefDao database;
//	private CatalogoRicetteChefDao database;
	private static CatalogoAlimentiDao databaseAlimenti;
	private static final Logger logger = Logger.getLogger(TrovaRicettaController.class.getName());
	
	private RicettaDao ricettaDao;
	
	/*
	private TrovaRicettaController() {
	}
	
	public static synchronized TrovaRicettaController ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza==null) {
			istanza=new TrovaRicettaController();
			dispensa= Dispensa.ottieniIstanza();
			try {
				database= CatalogoRicetteImplementazioneDao.ottieniIstanza();
			} catch (IOException e) {
				logger.severe("PROBLEMA CON IL COLLEGAMENTO DEL DB! TERMINO L'APPLICAZIONE");
				System.exit(0);
			}
			//database= CatalogoRicetteImplementazione2Dao.ottieniIstanza(); //SU FILE
			databaseAlimenti=CatalogoAlimentiNutrixionixImplementazioneDao.ottieniIstanza();
		}
		return istanza;
	}
	*/
	
	/*
	public TrovaRicettaController() {
		dispensa = Dispensa.ottieniIstanza();
		try {
			//database = new CatalogoRicetteImplementazioneDao();
			database = CatalogoRicetteImplementazioneDao.ottieniIstanza();
		} catch (IOException e) {
			logger.severe("PROBLEMA CON IL COLLEGAMENTO DEL DB! TERMINO L'APPLICAZIONE");
			System.exit(0);
		}
		//database= CatalogoRicetteImplementazione2Dao.ottieniIstanza(); //SU FILE
		databaseAlimenti=CatalogoAlimentiNutrixionixImplementazioneDao.ottieniIstanza();
	}
	*/
	public TrovaRicettaController() {
		dispensa = Dispensa.ottieniIstanza();
		ricettaDao = DaoFactoryProvider.ottieniIstanza().creaRicettaDao();
		databaseAlimenti = CatalogoAlimentiNutrixionixImplementazioneDao.ottieniIstanza();
	}
	
	
	/*
	public void aggiornaDispensa(Alimento alimento,int x) {  //old
		if(x==0) {
			dispensa.aggiungiAlimento(alimento);  //SE X E' 0 AGGIUNGO
		}
		else {
			dispensa.eliminaAlimento(alimento);  //SE X E' 1 ELIMINO
		}
	}
	*/
	public void aggiungiInDispensa(AlimentoBean alimentoBean) {  
		Alimento alimento = new Alimento(alimentoBean.getNome());
		dispensa.aggiungiAlimento(alimento);
	}
	public void rimuoviDallaDispensa(AlimentoBean alimentoBean) {
		Alimento alimento = new Alimento(alimentoBean.getNome());
		dispensa.eliminaAlimento(alimento);
	}
	
	public void svuotaDispensa() {  //METODO PER SVUOTARE LA DISPENSA
		dispensa.svuotaDispensa();
	}
	
	/*
	public List<Ricetta> trovaLeRicette(int difficolta, String autore){  //METODO PER TROVARE LE RICETTE
		List<Ricetta> ricetteTrovate= null;
		try {
			if(autore==null) {  //SE NON PASSO L'AUTORE VOGLIO EFFETTUARE LA RICERCA PER ALIMENTI-DIFFICOLTA'
				ricetteTrovate = ricettaDao.trovaRicette(dispensa, difficolta, null);
			}
			else {  //SE PASSO L'AUTORE VOGLIO EFFETTUARE LA RICERCA PER AUTORE
				ricetteTrovate = ricettaDao.trovaRicette(null,0,autore);
			}
			if(ricetteTrovate!=null) {
				mostraRicette(ricetteTrovate);
				return ricetteTrovate;
			}
			else {
				return new ArrayList<>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELL'OTTENIMENTO DELLE RICETTE");
			logger.info("Problema con il DB");
			return new ArrayList<>();
		}
	}
	*/
	
	public ArrayList<RicettaBean> trovaRicette(int difficolta, UtenteBean utenteBean) {
		List<Ricetta> ricetteTrovate = null;
		try {
			//if(utenteBean.getUsername() == null) { //SE NON PASSO L'AUTORE VOGLIO EFFETTUARE LA RICERCA PER ALIMENTI-DIFFICOLTA'
			if (utenteBean == null) {
				ricetteTrovate = ricettaDao.trovaRicette(dispensa, difficolta, null);
			}
			else {  //SE PASSO L'AUTORE VOGLIO EFFETTUARE LA RICERCA PER AUTORE
				ricetteTrovate = ricettaDao.trovaRicette(null, 0, utenteBean.getUsername());
			}	
		
			if(ricetteTrovate!=null) {
				ArrayList<RicettaBean> ricetteTrovateBean= new ArrayList<>();
				for(Ricetta r:ricetteTrovate) {
					RicettaBean ricettaBean=new RicettaBean();
					ricettaBean.setNome(r.getNome());
					ricettaBean.setDescrizione(r.getDescrizione());
					ricettaBean.setDifficolta(r.getDifficolta());
					ArrayList<AlimentoBean> alimentiTrovatiBean=new ArrayList<>();
					List<Alimento> alimentiTrovati=r.getIngredienti();
					for(Alimento a:alimentiTrovati) {
						AlimentoBean alimentoBean= new AlimentoBean();
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
		catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELL'OTTENIMENTO DELLE RICETTE");
			logger.info("Problema con il DB");
			return new ArrayList<>();
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
	
	
	/*
	public List<Alimento> trovaAlimentiOld(String nomeAlimento) {  //tolto string e messo bean
		List<Alimento> alimentiTrovati=null;
		alimentiTrovati=databaseAlimenti.trovaAlimenti(nomeAlimento); 
		if(alimentiTrovati!=null && !alimentiTrovati.isEmpty()) {
			mostraAlimenti(alimentiTrovati);
			return alimentiTrovati;
		}
		else {
			return new ArrayList<>();
		}
	}
	*/
	public ArrayList<AlimentoBean> trovaAlimenti(AlimentoBean alimentoBeanInserito) {
		List<Alimento> alimentiTrovati = null;
		ArrayList<AlimentoBean> alimentiTrovatiBean = null;
		alimentiTrovati = databaseAlimenti.trovaAlimenti(alimentoBeanInserito.getNome());
		if(alimentiTrovati!=null && !alimentiTrovati.isEmpty()) {
			alimentiTrovatiBean = new ArrayList<>();
			for(Alimento a:alimentiTrovati) {
				AlimentoBean alimentoBean= new AlimentoBean();
				alimentoBean.setNome(a.getNome());
				alimentiTrovatiBean.add(alimentoBean);
			}
			mostraAlimenti(alimentiTrovati);
			return alimentiTrovatiBean;
		}
		else {
			return new ArrayList<>();
		}
	}
	
	private void mostraAlimenti(List<Alimento> alimenti) {  //METODO PRIVATO PER STAMPARE SU CONSOLE TUTTI GLI ALIMENTI
		for(Alimento a: alimenti) {   //UTILIZZATO PER COMODITA' NEL PROGETTO
			logger.info(a.getNome());
		}
	}
	
	public List<Alimento> mostraLaDispensa(){  //METODO PER OTTENERE GLI ALIMENTI NELLA DISPENSA
		List<Alimento> alimentiInDispensa = null;
		alimentiInDispensa = dispensa.getAlimenti();
		if(alimentiInDispensa!=null && !alimentiInDispensa.isEmpty()) {
			return alimentiInDispensa;
		}
		else {
			return new ArrayList<>();
		}
	}
	
	public ArrayList<AlimentoBean> mostraDispensa() {
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
	
	public Ricetta ottieniRicetta(String nome,String autore) {  //METODO PER OTTENERE I DATI DI UNA RICETTA IN FUNZIONE DEL NOME-AUTORE
		Ricetta ricetta=null; 
		try {
			ricetta = ricettaDao.ottieniDatiRicetta(nome,autore);
			if(ricetta!=null) {
				return ricetta;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELL'OTTENIMENTO DEI DATI DELLA RICETTA");
			logger.info("Problema con il DB");
			return null;
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELL'OTTENIMENTO DEI DATI DELLA RICETTA");
			logger.info("Problema con il DB");
			return null;
		}
	}
	
	
}