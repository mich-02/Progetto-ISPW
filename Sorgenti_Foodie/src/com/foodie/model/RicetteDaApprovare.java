package com.foodie.model;

import java.util.ArrayList;
import java.util.List;

public class RicetteDaApprovare extends SubjectPatternObserver{  

    private static ArrayList<Ricetta> ricetteDaVerificare;

    public RicetteDaApprovare(){
    	ricetteDaVerificare = new ArrayList<Ricetta>();
    }

    public void aggiungiRicettaDaVerificare(Ricetta ricetta) {  //AGGIUNGI LE RICETTE DA VERIFICARE
        if(ricetteDaVerificare!=null && !ricetteDaVerificare.contains(ricetta)) {
            ricetteDaVerificare.add(ricetta);
            notifica();
        }
    }

    public void ricettaVerificata(Ricetta ricetta) {  //RIMUOVE LA RICETTA SE VERIFICATA
        if(ricetteDaVerificare.remove(ricetta)) {
            notifica();
        }
    }

    public List<Ricetta> getRicetteDaVerificare(){  //OTTIENI LE RICETTE DA VERIFICARE
        if(ricetteDaVerificare!=null && !ricetteDaVerificare.isEmpty()) {
            return ricetteDaVerificare;
        }
        else {
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