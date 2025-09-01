package com.foodie.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RicetteDaApprovare extends SubjectPatternObserver{  

    //private static RicetteDaApprovare istanza;  //SINGLETON
    private static ArrayList<Ricetta> ricetteDaVerificare;
    private static final Logger logger = Logger.getLogger(RicetteDaApprovare.class.getName());

    public RicetteDaApprovare(){
    	ricetteDaVerificare = new ArrayList<Ricetta>();
    }

    /*
    public static synchronized RicetteDaApprovare ottieniIstanza() {

        if(istanza==null) {

            istanza=new RicetteDaApprovare();
            ricetteDaVerificare =new ArrayList<>();
        }

        return istanza;
    }
    */

    public void aggiungiRicettaDaVerificare(Ricetta ricetta) {  //AGGIUNGI LE RICETTE DA VERIFICARE
        if(ricetteDaVerificare!=null && !ricetteDaVerificare.contains(ricetta)) {
            ricetteDaVerificare.add(ricetta);
            logger.info("Ricetta da Verificare aggiunta");
            notifica();
        }
        else {
            logger.info("Ricetta già in corso di verifica");
        }
    }

    public void ricettaVerificata(Ricetta ricetta) {  //RIMUOVE LA RICETTA SE VERIFICATA
        if(ricetteDaVerificare.remove(ricetta)) {
            logger.info("Ricetta verificata");
            notifica();
        }
        else {
            logger.info("Ricetta già verificata o non inviata al moderatore");
        }
    }

    public List<Ricetta> getRicetteDaVerificare(){  //OTTIENI LE RICETTE DA VERIFICARE
        if(ricetteDaVerificare!=null && !ricetteDaVerificare.isEmpty()) {
            logger.info("Ecco le ricette da verificare");
            return ricetteDaVerificare;
        }
        else {
            logger.info("Nessuna ricetta da verificare");
            return new ArrayList<>();
        }
    }

    public Ricetta ottieniRicetta(String nome,String autore) {  //OTTIENE I DATI DELLA RICETTA CHE SI DEVE VERIFICARE
        if(ricetteDaVerificare!=null && !ricetteDaVerificare.isEmpty()) {
            for(Ricetta r: ricetteDaVerificare) {
                if(r.getNome().equals(nome) && r.getAutore().equals(autore)) {
                    return r;
                }
            }
            return null;
        }
        else {
            return null;
        }
    }
}