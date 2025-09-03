package com.foodie.model;

import java.util.ArrayList;


public abstract class SubjectPatternObserver {        //CLASSE ASTRATTA DEL PATTERN OBSERVER CHE METTE 
													  //A DISPOSIZIONE METODI PER GESTIRE GLI 
	protected ArrayList<Observer> viewRegistrate; 	  //OSSERVATORI DI CUI NON SI SA NULLA

	
	protected SubjectPatternObserver() {
		this.viewRegistrate = new ArrayList<>();
	}
	
	public void registra(Observer o) {  //REGISTRA L'OSSERVATORE NELLA PROPRIA LISTA SE NON PRESENTE
		if(!viewRegistrate.contains(o)) {
			this.viewRegistrate.add(o);
		}
	}
	
	public void cancella(Observer o) {  //CANCELLA L'OSSERVATORE DALLA PROPRIA LISTA SE PRESENTE
		this.viewRegistrate.remove(o); 
	}
	
	protected void notifica() {         //NOTIFICA TUTTI GLI OSSERVATORI DI UNA EVENTUALE MODIFICA DELL'OSSERVATO
		if(!viewRegistrate.isEmpty()) {
			for(Observer o : viewRegistrate) {
				o.aggiornaView();
			}
		}
	}
	
}