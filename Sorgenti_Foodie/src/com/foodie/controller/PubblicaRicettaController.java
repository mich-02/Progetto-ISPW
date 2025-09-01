package com.foodie.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.foodie.applicazione.LoginViewController;
import com.foodie.model.Alimento;
import com.foodie.model.AlimentoBean;
import com.foodie.model.AreaPersonaleDao;
import com.foodie.model.AreaPersonaleImplementazioneDao;
import com.foodie.model.CatalogoRicetteChefDao;
import com.foodie.model.CatalogoRicetteImplementazione2Dao;
import com.foodie.model.CatalogoRicetteImplementazioneDao;
import com.foodie.model.Dispensa;
import com.foodie.model.LoggedUser;
import com.foodie.model.Moderatore;
import com.foodie.model.Observer;
import com.foodie.model.Ricetta;
import com.foodie.model.RicettaBean;
import com.foodie.model.RicettaDuplicataException;
import com.foodie.model.RicetteDaApprovare;
import com.foodie.model.UtenteBean;
import com.foodie.model.dao.DaoFactoryProvider;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.RicetteDaApprovareDao;

@SuppressWarnings("unused")
public class PubblicaRicettaController {  

    //BALDUZ
    //ORA LA CLASSE MODERATORE QUI NON SERVE A NULLA, DEVI MODIFCARE LA CLASSE PER RICORDARTI DI CARICARE PRIMA LO STATO DELLE
    //RICETTE DA APPROVARE E POI CI LAVORI OGNI VOLTA CHE NE AGGIUNGI UNA O RIMUOVI UNA , POI OGNI VOLTA SALVA LO STATO SU EPRSISTENZAM, COSì HAI SIA
    //CONSISTENZA IN RAM SIA SU FILE CONTEMPORANEAMENTE


//	private static PubblicaRicettaController istanza;
//	private static CatalogoRicetteChefDao database;
//	private CatalogoRicetteChefDao database;
//	private static Moderatore moderatore;  //BALDUZ QUESTA INTENDO !!!
	private static Ricetta ricetta = new Ricetta();
	private static final Logger logger = Logger.getLogger(PubblicaRicettaController.class.getName());
	private static RicetteDaApprovare ricetteDaApprovare;
	private RicetteDaApprovareDao ricetteDaApprovareDao;
	private RicettaDao ricettaDao;
//	private static AreaPersonaleDao databaseAreaPersonale = AreaPersonaleImplementazioneDao.ottieniIstanza(); 
	
	/*
	private PubblicaRicettaController() {
	}
	
	public static synchronized PubblicaRicettaController ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza==null) {
			istanza=new PubblicaRicettaController();
			try {
				database= CatalogoRicetteImplementazioneDao.ottieniIstanza();
			} catch (IOException e) {
				logger.severe("PROBLEMA CON IL COLLEGAMENTO DEL DB! TERMINO L'APPLICAZIONE");
				System.exit(0);
			}
			//database= CatalogoRicetteImplementazione2Dao.ottieniIstanza(); //SU FILE
			moderatore=Moderatore.ottieniIstanza();
		}
		return istanza;
	}
	
	public PubblicaRicettaController() {
		try {
			database = CatalogoRicetteImplementazioneDao.ottieniIstanza();
			//database = new CatalogoRicetteImplementazioneDao();
		} catch (IOException e) {
			logger.severe("PROBLEMA CON IL COLLEGAMENTO DEL DB! TERMINO L'APPLICAZIONE");
			System.exit(0);
		}
		//database = CatalogoRicetteImplementazione2Dao.ottieniIstanza(); //SU FILE
		moderatore=Moderatore.ottieniIstanza();
	}
	*/
	public PubblicaRicettaController() {
		ricettaDao = DaoFactoryProvider.ottieniIstanza().creaRicettaDao();
		//moderatore = Moderatore.ottieniIstanza();
		ricetteDaApprovare = new RicetteDaApprovare();
		ricetteDaApprovareDao = DaoFactoryProvider.ottieniIstanza().creaRicetteDaApprovareDao();
		
	}
	
	/*
	public void salvaAreaPersonale(String descrizione) {  //SALVA LA DESCRIZIONE DELL'AREA PERSONALE NEL DB
		String username = LoggedUser.ottieniIstanza().getUsername();
		databaseAreaPersonale.salvaAreaPersonale(username, descrizione);
	}
	
	public Map<String,String> caricaAreaPersonale() {  //CARICA LA DESCRIZIONE DELL'AREA PERSONALE DA DB
		return databaseAreaPersonale.caricaAreaPersonale();
    }
    */
	
	public static void creaRicetta() {  //CREA LA RICETTA
		ricetta = new Ricetta();
	}
	
	public Ricetta getRicetta() {  //RESTITUISCE L'ISTANZA DELLA RICETTA
		return ricetta;
	}
	
	
	/*
	public void compilaRicetta(Ricetta ricettaCompilata) {  //COMPILA LA RICETTA
		if(ricetta!=null) {
			ricetta.setNome(ricettaCompilata.getNome());
			ricetta.setDescrizione(ricettaCompilata.getDescrizione());
			ricetta.setDifficolta(ricettaCompilata.getDifficolta());
			ricetta.setAutore(ricettaCompilata.getAutore());
			notificaModeratore();
		}
	}
	*/
	
	public void compilaRicetta(RicettaBean ricettaBean) { //old
		ricetta.setNome(ricettaBean.getNome());
		ricetta.setDescrizione(ricettaBean.getDescrizione());
		ricetta.setDifficolta(ricettaBean.getDifficolta());
		ricetta.setAutore(ricettaBean.getAutore());
		notificaModeratore();
}

	
	/*
	private List<Alimento> getAlimentiRicetta() {  //MOSTRA GLI INGREDIENTI DELLA RICETTA
		List<Alimento> alimentiRicetta=ricetta.getIngredienti();
		if(!alimentiRicetta.isEmpty()) {
			return alimentiRicetta;
		}
		else {
			return new ArrayList<>();
		}
	}
	*/
	
	
	public ArrayList<AlimentoBean> mostraIngredientiRicetta() {
		List<Alimento> alimentiRicetta = ricetta.getIngredienti();
		if(alimentiRicetta != null && !alimentiRicetta.isEmpty()) {
			ArrayList<AlimentoBean> alimentiRicettaBean =new ArrayList<>();
			for(Alimento a: alimentiRicetta) {
				AlimentoBean alimentoBean=new AlimentoBean();
				alimentoBean.setNome(a.getNome());
				alimentiRicettaBean.add(alimentoBean);
			}
			return alimentiRicettaBean;
		}
		else {
			return new ArrayList<>();
		}
	}
	
//	public void aggiungiIngredienteRicetta(AlimentoBean alimentoBean, String quantita) {  //AGGIUNGE INGREDIENTE ALLA RICETTA
//		Alimento alimento = new Alimento(alimentoBean.getNome());
//		ricetta.aggiungiIngrediente(alimento, quantita);
//	}

    public void aggiungiIngredienteRicetta(AlimentoBean alimentoBean) {  //MODIFICATO DA BALDUZ MESSO QUANTITA NEL BEAN
        Alimento alimento = new Alimento(alimentoBean.getNome());
        ricetta.aggiungiIngrediente(alimento, alimentoBean.getQuantita());
    }
	
	public void eliminaIngredienteRicetta(AlimentoBean alimentoBean) {
		Alimento alimento = new Alimento(alimentoBean.getNome());
		ricetta.eliminaIngrediente(alimento);
	}
	
	private void notificaModeratore() {  //NOTIFICA IL MODERATORE DOPO AVER COMPILATO
		logger.info("MODERATORE NOTIFICATO");
		//ricetteDaApprovare.aggiungiRicettaDaVerificare(ricetta);
		salvaRicettaDaApprovare(ricetta);
		//moderatore.aggiungiRicettaDaVerificare(ricetta);
		creaRicetta(); //resetto la ricetta corrente
	}
	
	private void notificaChef(boolean bool) {  //NOTIFICA LO CHEF DOPO AVER APPROVATO LA RICETTA
		String notifica = "CHEF NOTIFICATO: "+bool;
		logger.info(notifica);
	}
	
//	public void pubblicaRicetta(String nome,String autore,boolean bool) {  //PUBBLICA LA RICETTA APPROVATA NEL DATABASE
//		Ricetta ricettaDaPubblicare = Moderatore.ottieniRicetta(nome, autore);
//		try {
//			if(bool) {
//				ricettaDao.aggiungiRicetta(ricettaDaPubblicare);
//			}
//			moderatore.ricettaVerificata(ricettaDaPubblicare);
//			notificaChef(bool);
//		}catch(RicettaDuplicataException e) {
//			e.suggerimento();
//			logger.severe("RICETTA GIA' ESISTENTE");
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.severe("ERRORE NELLA PUBBLICAZIONE DELLA RICETTA");
//			logger.info("Problema con il DB");
//		}
//	}

    public void pubblicaRicetta(RicettaBean ricettaBean) {  //MODIFICATO DA BALDUZ AGGIUNTO BOOL
        //Ricetta ricettaDaPubblicare = Moderatore.ottieniRicetta(ricettaBean.getNome(), ricettaBean.getAutore());
    	Ricetta ricettaDaPubblicare = ricetteDaApprovare.ottieniRicetta(ricettaBean.getNome(), ricettaBean.getAutore());
        try {
            if(ricettaBean.getPublish()) {
                ricettaDao.aggiungiRicetta(ricettaDaPubblicare);
            }
            ricetteDaApprovare.ricettaVerificata(ricettaDaPubblicare);
            notificaChef(ricettaBean.getPublish());
        }catch(RicettaDuplicataException e) {
            e.suggerimento();
            logger.severe("RICETTA GIA' ESISTENTE");
        }catch (Exception e) {
            e.printStackTrace();
            logger.severe("ERRORE NELLA PUBBLICAZIONE DELLA RICETTA");
            logger.info("Problema con il DB");
        }
    }
	
//	public void eliminaRicetta(String nome, String autore) {    //ELIMINA LA RICETTA DAL DATABASE
//
//		try {
//			ricettaDao.eliminaRicetta(nome,autore);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.severe("ERRORE NELL'ELIMINAZIONE DELLA RICETTA");
//			logger.info("Problema con il DB");
//		}
//	}

    public void eliminaRicetta(RicettaBean ricettaBean) {    //MODIFICATO DA BALDUZ

        try {
            ricettaDao.eliminaRicetta(ricettaBean.getNome(),ricettaBean.getAutore());
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("ERRORE NELL'ELIMINAZIONE DELLA RICETTA");
            logger.info("Problema con il DB");
        }
    }
	
	/*
	public List<Ricetta> mostraRicetteDaApprovare() {  //MOSTRA LE RICETTE CHE DEVONO ESSERE APPROVATE
		List<Ricetta> ricette=Moderatore.getRicetteDaVerificare();
		if(ricette!=null && !ricette.isEmpty()) {
			return ricette;
		}
		else {
			return new ArrayList<>();
		}
	}
	*/

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
	
	public ArrayList<RicettaBean> mostraRicetteDaApprovare() {
		List<Ricetta> ricette = null;
		//ricetteDaApprovare.getRicetteDaVerificare();
		try {
			ricetteDaApprovareDao.caricaRicetteDaApprovare();
			ricette = ricetteDaApprovare.getRicetteDaVerificare();
			if(ricette!=null && !ricette.isEmpty()) {
				ArrayList<RicettaBean> ricetteBean = new ArrayList<>();
				for(Ricetta r: ricette) {
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
					ricetteBean.add(ricettaBean);
				}
				return ricetteBean;
			}
			else {
				return new ArrayList<>();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("ERRORE NELL'OTTENIMENTO DEI DATI DELLA RICETTA");
			logger.info("Problema con il DB");
			return null;
		}
	}
	
	public ArrayList<RicettaBean> trovaRicetteChef(UtenteBean utenteBean) {
		List<Ricetta> ricetteTrovate = null;
		try {
			ricetteTrovate = ricettaDao.trovaRicettePerAutore(utenteBean.getUsername());
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
	
	public void registraOsservatoreRicetta(Observer observer) {
		ricetta.registra(observer);
	}
	
//	public void registraOsservatoreModeratore(Observer observer) {
//		moderatore.registra(observer);
//	}

	
    public void registraOsservatoreModeratore(Observer observer) {  //DA BALDUZ TI REGISTRI ALLE RICETTE DA APPROVARE DOPO AVERLE CARICATE INIZIALMENTE
    	ricetteDaApprovare.registra(observer);
    }
    


    //CREATI LE CLASSI DAO

    private void salvaRicettaDaApprovare(Ricetta ricetta){  // DA BALDUZ  ,CHIAMALO OGNI VOLTA CHE APPROVI O RIFIUTI UNA RICETTA
			try {
				ricetteDaApprovareDao.salvaRicettaDaApprovare(ricetta);
			} catch (Exception e) {
				e.printStackTrace();
			}


        //RicetteDaApprovare ricetteDaApprovare = RicettaDaApprovareDao.ottieniIstanza();

        //USA DAO PER SALVARE LA CLASSE RICETTEDAAPPROVARE   ----->SCEGLI TE SE FARE SU FILE,DB O ENTRAMBI
    }

    public void caricaRicetteDaApprovare(){  //DA BALDUZ, CHIAMALO ALL'AVVIO DELL'APPLICAZIONE
    	
    	

        //USA DAO PER CARICARE LA CLASSE RICETTEDAAPPROVARE  ----->SCEGLI TE SE FARE SU FILE,DB O ENTRAMBI
        //RicetteDaApprovare ricetteDaApprovare = RicettaDaApprovareDao.ottieniIstanza();
        //RISETTA LO STATO
    }
	
}