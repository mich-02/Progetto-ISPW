package com.foodie.model;

public class Moderatore extends Utente { 

    public Moderatore(String username){
    	super(username);
    }

	@Override
	public String getViewIniziale(int i) {  //view iniziale
		if(i==1) {
			return "/com/foodie/boundary/ModeratoreView.fxml";
		}
		else {
			return "/com/foodie/boundary2/ModeratoreView2.fxml";
		}
	}
//
//	public void aggiungiRicettaDaVerificare(Ricetta ricetta) {  //AGGIUNGI LE RICETTE DA VERIFICARE
//		if(ricetteDaVerificare!=null && !ricetteDaVerificare.contains(ricetta)) {
//			ricetteDaVerificare.add(ricetta);
//			logger.info("Ricetta da Verificare aggiunta");
//			notifica();
//		}
//		else {
//			logger.info("Ricetta già in corso di verifica");
//		}
//	}
//
//	public void ricettaVerificata(Ricetta ricetta) {  //RIMUOVE LA RICETTA SE VERIFICATA
//		if(ricetteDaVerificare.remove(ricetta)) {
//			logger.info("Ricetta verificata");
//			notifica();
//		}
//		else {
//			logger.info("Ricetta già verificata o non inviata al moderatore");
//		}
//	}
//
//	public static List<Ricetta> getRicetteDaVerificare(){  //OTTIENI LE RICETTE DA VERIFICARE
//		if(ricetteDaVerificare!=null && !ricetteDaVerificare.isEmpty()) {
//			logger.info("Ecco le ricette da verificare");
//			return ricetteDaVerificare;
//		}
//		else {
//			logger.info("Nessuna ricetta da verificare");
//			return new ArrayList<>();
//		}
//	}
//
//	public static Ricetta ottieniRicetta(String nome,String autore) {  //OTTIENE I DATI DELLA RICETTA CHE SI DEVE VERIFICARE
//		if(ricetteDaVerificare!=null && !ricetteDaVerificare.isEmpty()) {
//			for(Ricetta r: ricetteDaVerificare) {
//				if(r.getNome().equals(nome) && r.getAutore().equals(autore)) {
//					return r;
//				}
//			}
//			return null;
//		}
//		else {
//			return null;
//		}
//	}
}