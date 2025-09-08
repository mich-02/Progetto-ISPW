package com.foodie.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.foodie.bean.AlimentoBean;
import com.foodie.bean.RicettaBean;
import com.foodie.bean.UtenteBean;
import com.foodie.exception.DaoException;
import com.foodie.exception.NessunaRicettaDaApprovareException;
import com.foodie.exception.OperazioneNonEseguitaException;
import com.foodie.exception.RicettaDuplicataException;
import com.foodie.model.Alimento;
import com.foodie.model.Observer;
import com.foodie.model.Ricetta;
import com.foodie.model.RicetteDaApprovare;
import com.foodie.model.dao.DaoFactoryProvider;
import com.foodie.model.dao.RicettaDao;
import com.foodie.model.dao.RicetteDaApprovareDao;


public class PubblicaRicettaController {  
	
	private static Ricetta ricetta = new Ricetta();
	private RicetteDaApprovare ricetteDaApprovare = new RicetteDaApprovare();
	private RicetteDaApprovareDao ricetteDaApprovareDao;
	private RicettaDao ricettaDao;
	private static final Logger logger = Logger.getLogger(PubblicaRicettaController.class.getName());
	
	public PubblicaRicettaController() {
		ricettaDao = DaoFactoryProvider.ottieniIstanza().creaRicettaDao();
		ricetteDaApprovareDao = DaoFactoryProvider.ottieniIstanza().creaRicetteDaApprovareDao();
		
	}
	
	public void compilaRicetta(RicettaBean ricettaBean) { 
		ricetta.setNome(ricettaBean.getNome());
		ricetta.setDescrizione(ricettaBean.getDescrizione());
		ricetta.setDifficolta(ricettaBean.getDifficolta());
		ricetta.setAutore(ricettaBean.getAutore());
		notificaModeratore();
	}

	
	public List<AlimentoBean> mostraIngredientiRicetta() {
		List<Alimento> alimentiRicetta = ricetta.getIngredienti();
		if(alimentiRicetta != null && !alimentiRicetta.isEmpty()) {
			ArrayList<AlimentoBean> alimentiRicettaBean = new ArrayList<>();
			for(Alimento a: alimentiRicetta) {
				AlimentoBean alimentoBean = new AlimentoBean();
				alimentoBean.setNome(a.getNome());
				alimentiRicettaBean.add(alimentoBean);
			}
			return alimentiRicettaBean;
		}
		else {
			return new ArrayList<>();
		}
	}

    public void aggiungiIngredienteRicetta(AlimentoBean alimentoBean) {  
        Alimento alimento = new Alimento(alimentoBean.getNome());
        ricetta.aggiungiIngrediente(alimento, alimentoBean.getQuantita());
    }
	
	public void eliminaIngredienteRicetta(AlimentoBean alimentoBean) {
		Alimento alimento = new Alimento(alimentoBean.getNome());
		ricetta.eliminaIngrediente(alimento);
	}
	
	private void notificaModeratore() {  // notifica moderatore dopo aver compilato
		logger.info("MODERATORE NOTIFICATO");
		salvaRicettaDaApprovare(ricetta);
		creaRicetta(); //resetto la ricetta corrente
	}
	
	private void notificaChef(boolean bool) {  // notifica lo chef dopo aver approvato la ricetta
		String notifica = "CHEF NOTIFICATO: " + bool;
		logger.info(notifica);
	}

    public void pubblicaRicetta(RicettaBean ricettaBean) throws OperazioneNonEseguitaException{  
    	Ricetta ricettaDaPubblicare = ricetteDaApprovare.ottieniRicetta(ricettaBean.getNome(), ricettaBean.getAutore());
        try {
            if (ricettaBean.getPublish()) {
                ricettaDao.aggiungiRicetta(ricettaDaPubblicare);
            } else {
                ricetteDaApprovareDao.rifiutaRicetta(ricettaDaPubblicare);
            }
            ricetteDaApprovare.ricettaVerificata(ricettaDaPubblicare);
            notificaChef(ricettaBean.getPublish());

        } catch (RicettaDuplicataException e) {
            logger.severe("Errore in pubblicaRicetta: " + e.getMessage());
            try {
                // Rifiuto la ricetta duplicata automaticamente
                ricetteDaApprovareDao.rifiutaRicetta(ricettaDaPubblicare);
                ricetteDaApprovare.ricettaVerificata(ricettaDaPubblicare);
                throw new OperazioneNonEseguitaException("Ricetta già esistente per questo chef");
            } catch (DaoException ex) {
                logger.severe("Errore durante il rifiuto automatico della ricetta duplicata: " + ex.getMessage());
            }

        } catch (DaoException e) {
            logger.severe("Errore in pubblicaRicetta: " + e.getMessage());
        }
    }

    public void eliminaRicetta(RicettaBean ricettaBean) {    

        try {
            ricettaDao.eliminaRicetta(ricettaBean.getNome(),ricettaBean.getAutore());
        } catch (DaoException e) {
        	logger.severe("Errore nell'eliminazione della ricetta: " + e.getMessage());
        }
    }

	public RicettaBean ottieniRicetta(RicettaBean ricettaBean) { // metodo per ottenere i dati di una ricetta in funzione del nome-autore
		Ricetta ricettaCur = null;
		try {
			ricettaCur = ricettaDao.ottieniDatiRicetta(ricettaBean.getNome(), ricettaBean.getAutore());
			if(ricettaCur != null) {
				ricettaBean.setNome(ricettaCur.getNome());
				ricettaBean.setDescrizione(ricettaCur.getDescrizione());
				ricettaBean.setDifficolta(ricettaCur.getDifficolta());
				ArrayList<AlimentoBean> alimentiTrovatiBean = new ArrayList<>();
				List<Alimento> alimentiTrovati = ricettaCur.getIngredienti();
				for(Alimento a:alimentiTrovati) {
					AlimentoBean alimentoBean = new AlimentoBean();
					alimentoBean.setNome(a.getNome());
					alimentiTrovatiBean.add(alimentoBean);
				}
				ricettaBean.setIngredienti(alimentiTrovatiBean);
				ricettaBean.setAutore(ricettaCur.getAutore());
				ricettaBean.setQuantita(ricettaCur.getQuantita());
				return ricettaBean;
			}
			else {
				return null;
			}
		} catch (DaoException e) {
			logger.severe("Errore nell'ottenimento dei dati della ricetta: " + e.getMessage());
			return null;
		}
	}
	
	public List<RicettaBean> getRicetteDaApprovare() {
		List<Ricetta> ricette = null;
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
	}
	
	public List<RicettaBean> trovaRicetteChef(UtenteBean utenteBean) {
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
	
	public static void creaRicetta() { 
		ricetta = new Ricetta();
	}
	
	public void registraOsservatoreRicetta(Observer observer) {
		ricetta.registra(observer);
	}
	
    public void registraOsservatoreRicetteDaApprovare(Observer observer) {  
    	ricetteDaApprovare.registra(observer);
    }

    private void salvaRicettaDaApprovare(Ricetta ricetta){ 
			try {
				ricetteDaApprovareDao.salvaRicettaDaApprovare(ricetta);
			} catch (DaoException e) {
				logger.severe("Errore durante il salvataggio della ricetta da approvare: " + e.getMessage());
			}
    }
    
    public void caricaRicetteDaApprovare() throws OperazioneNonEseguitaException {
    	try {
    		ricetteDaApprovareDao.caricaRicetteDaApprovare();		
    	} catch (DaoException e) {
			logger.severe("Errore durante il caricamento delle ricette da approvare: " + e.getMessage());
		} catch (NessunaRicettaDaApprovareException ex) {
			throw new OperazioneNonEseguitaException("Al momento non ci sono ricette da approvare. Torna più tardi.");
		}
    }
}