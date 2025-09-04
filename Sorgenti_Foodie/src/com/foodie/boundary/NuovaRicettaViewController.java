package com.foodie.boundary;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.bean.AlimentoBean;
import com.foodie.bean.RicettaBean;
import com.foodie.bean.UtenteBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.LoginController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NuovaRicettaViewController {
	
	private PubblicaRicettaController pubblicaRicettacontroller = new PubblicaRicettaController();
	private LoginController loginController = new LoginController();
	
	@FXML
	private Button gestisciRicetteButton;
	@FXML
	private RadioButton facile;
	@FXML
	private RadioButton medio;
	@FXML
	private RadioButton difficile;
	@FXML
	private TextField nome;
	@FXML
	private TextArea descrizione;
	@FXML
	private Button pubblica;
	
	@FXML
	private void caricaViewIngredienti(ActionEvent event) {  // tasto in basso a sx
		ViewLoader.caricaView(ViewInfo.INSERISCI_INGR);
	}
	
	@FXML
	private void compilaRicetta(ActionEvent event) {  //tasto Richiedi pubblicazione
		RicettaBean ricettaBean = new RicettaBean();
		String testo = nome.getText().trim();
		if(!testo.isEmpty()) {
			ricettaBean.setNome(nome.getText());
		}
		else {  //MOSTRO GRAFICAMENTE UN AVVERTIMENTO! ANCHE PER GLI ELSE SUCCESSIVI!
			nome.setPromptText("INSERISCI NOME");
			 // Creazione di un oggetto ScheduledExecutorService
	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	        // Creare una task da eseguire dopo 2 secondi
	        Runnable task = () -> 
	            // Codice da eseguire dopo 2 secondi
	            Platform.runLater(() -> nome.setPromptText("Nome Ricetta"));
	        

	        // Programmare la task per essere eseguita dopo 2 secondi
	        scheduler.schedule(task, 2, TimeUnit.SECONDS);

	        // Chiudere il thread scheduler dopo l'esecuzione della task
	        scheduler.shutdown();
		}
		testo = descrizione.getText().trim();
		if(!testo.isEmpty()) {
			ricettaBean.setDescrizione(descrizione.getText());
		}
		else {
			descrizione.setPromptText("INSERISCI DESCRIZIONE");
			 // Creazione di un oggetto ScheduledExecutorService
	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	        // Creare una task da eseguire dopo 2 secondi
	        Runnable task = () -> 
	            // Codice da eseguire dopo 2 secondi
	            Platform.runLater(() -> descrizione.setPromptText("Descrizione"));
	        

	        // Programmare la task per essere eseguita dopo 2 secondi
	        scheduler.schedule(task, 2, TimeUnit.SECONDS);

	        // Chiudere il thread scheduler dopo l'esecuzione della task
	        scheduler.shutdown();	
	        return;
		}
		int diff=0;
		if(facile.isSelected()) {
			diff=1;
		}
		else if(medio.isSelected()) {
			diff=2;
		}
		else if(difficile.isSelected()) {
			diff=3;
		}
		else {
			pubblica.setText("DIFFICOLTA?");
			 // Creazione di un oggetto ScheduledExecutorService
	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	        // Creare una task da eseguire dopo 2 secondi
	        Runnable task = () -> 
	            // Codice da eseguire dopo 2 secondi
	            Platform.runLater(() -> pubblica.setText("Pubblica"));
	        

	        // Programmare la task per essere eseguita dopo 2 secondi
	        scheduler.schedule(task, 2, TimeUnit.SECONDS);

	        // Chiudere il thread scheduler dopo l'esecuzione della task
	        scheduler.shutdown();	
	        return;
		}
		ricettaBean.setDifficolta(diff);
		UtenteBean utenteBean = loginController.getUtente();
		ricettaBean.setAutore(utenteBean.getUsername());
		ArrayList<AlimentoBean> alimentiBean = pubblicaRicettacontroller.mostraIngredientiRicetta();
		if(!alimentiBean.isEmpty()) {
		
        //if(ingredienti!=null && !(ingredienti.getChildren().isEmpty())) { 
			ricettaBean.setIngredienti(alimentiBean);
        	pubblicaRicettacontroller.compilaRicetta(ricettaBean);
        	gestisciRicetteButton.fire();  //SE INGREDIENTI MESSI, ALLORA PUBBLICA E SIMULA CLICK PER TORNARE ALL'AREA PERSONALE
		}
		else {  //MOSTRA GARFICAMENTE L'AVVERTIMENTO PER GLI INGREDIENTI
			pubblica.setText("INGREDIENTI?");
			 // Creazione di un oggetto ScheduledExecutorService
	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	        // Creare una task da eseguire dopo 2 secondi
	        Runnable task = () -> 
	            // Codice da eseguire dopo 2 secondi
	            Platform.runLater(() -> pubblica.setText("Pubblica"));
	        

	        // Programmare la task per essere eseguita dopo 2 secondi
	        scheduler.schedule(task, 2, TimeUnit.SECONDS);

	        // Chiudere il thread scheduler dopo l'esecuzione della task
	        scheduler.shutdown();	
	        
		}
	}
	
	@FXML
	private void disabilitaPulsanti(ActionEvent event) {  //per cliccare solo una difficoltà alla volta 
		if (facile.isSelected()) {
		    medio.setDisable(true);
		    difficile.setDisable(true);
		} else if (medio.isSelected()) {
		    facile.setDisable(true);
		    difficile.setDisable(true);
		} else if (difficile.isSelected()) {
		    medio.setDisable(true);
		    facile.setDisable(true);
		} else {
		    medio.setDisable(false);
		    difficile.setDisable(false);
		    facile.setDisable(false);
		}
	}
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) { 
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
    }
	
	@FXML
	private void tornaAlLogin(MouseEvent event) {  
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}

}