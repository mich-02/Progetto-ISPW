package com.foodie.model;

import java.util.ArrayList;
import java.util.List;

public class RicetteDaApprovare extends SubjectPatternObserver{  

    private static ArrayList<Ricetta> ricetteDaVerificare = new ArrayList<>();

    public void aggiungiRicettaDaVerificare(Ricetta ricetta) {  // aggiungi le ricette da verificare
        if(ricetteDaVerificare != null && !ricetteDaVerificare.contains(ricetta)) {
            ricetteDaVerificare.add(ricetta);
            notifica();
        }
    }

    public void ricettaVerificata(Ricetta ricetta) {  // rimuove la ricetta se verificata
        if(ricetteDaVerificare.remove(ricetta)) {
            notifica();
        }
    }

    public List<Ricetta> getRicetteDaVerificare(){  // ottieni le ricette da verificare
        if(ricetteDaVerificare != null && !ricetteDaVerificare.isEmpty()) {
            return ricetteDaVerificare;
        }
        else {
            return new ArrayList<>();
        }
    }

    public Ricetta ottieniRicetta(String nome,String autore) {  // ottiene i dati della ricetta che si deve verificare
        if(ricetteDaVerificare != null && !ricetteDaVerificare.isEmpty()) {
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