package com.foodie.boundary;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.AdattatoreFactory;
import com.foodie.controller.ControllerAdapter;
import com.foodie.model.RicettaBean;
import com.foodie.model.UtenteBean;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NuovaRicettaViewController {
	
	//private static NuovaRicettaViewController istanza;
	private AdattatoreFactory factory = AdattatoreFactory.ottieniIstanza();
	private PubblicaRicettaController controller = PubblicaRicettaController.ottieniIstanza();
	private ControllerAdapter adattatorePubblicaRicettaController = factory.creaPubblicaRicettaAdapter();
	private ControllerAdapter adattatoreLoginController = factory.creaLoginAdapter();
	//private CaricaView caricaView = CaricaView.ottieniIstanza();
	private Stage primaryStage;
	private static final Logger logger = Logger.getLogger(NuovaRicettaViewController.class.getName());
	@FXML
	private Button areaPersonaleButton;
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
	
	private VBox ingredientiRicetta;
	
	
	public void initData(VBox contenitoreIngredienti) {
		this.ingredientiRicetta = contenitoreIngredienti;
	}
	
	/*
	private NuovaRicettaViewController() {
	}
	
	public static synchronized NuovaRicettaViewController ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza == null) {
			istanza = new NuovaRicettaViewController();
		}
		return istanza;
	}
	*/
	
	
	public void setPrimaryStage(Stage primaryStage) { //PASSO LO STAGE
		this.primaryStage= primaryStage;
	}
	
	public void aggiornaView(String nome, String descrizione, int diff) {  //AGGIORNA I VARI CAMPI
		this.nome.setText(nome);
		this.descrizione.setText(descrizione);
		switch(diff) {
		case 1:
				facile.setSelected(true);
				disabilitaPulsanti(null);
				break;
		case 2:
				medio.setSelected(true);
				disabilitaPulsanti(null);
				break;
		case 3:
				difficile.setSelected(true);
				disabilitaPulsanti(null);
				break;
		default:
			logger.severe("difficoltà non riconosciuta");
			disabilitaPulsanti(null);
		}
	}
	
	/*
	@FXML
	private void caricaViewIngredienti(ActionEvent event) {  // tasto in basso a sx
		InserisciIngredienteViewController inserisciIngredienteViewController = new InserisciIngredienteViewController();
		if(nome.getText()!=null) {  //PASSA LO STATO ATTUALE PER RIPOPOLARLO IN SEGUITO
			inserisciIngredienteViewController.setNome(nome.getText());
		}
		if(descrizione.getText()!=null) {
			inserisciIngredienteViewController.setDescrizione(descrizione.getText());
		}
		if(facile.isSelected()) {
			inserisciIngredienteViewController.setDifficolta(1);
		}
		else if(medio.isSelected()) {
			inserisciIngredienteViewController.setDifficolta(2);
		}
		else if(difficile.isSelected()) {
			inserisciIngredienteViewController.setDifficolta(3);
		}
		
		try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("InserisciIngredienteView.fxml"));
          loader.setController(inserisciIngredienteViewController);
          controller.registraOsservatore(inserisciIngredienteViewController, 2);
          Parent root = loader.load();
          inserisciIngredienteViewController.setPrimaryStage(primaryStage);
          inserisciIngredienteViewController.aggiornaView();
          Scene nuovaScena = new Scene(root);
          primaryStage.setScene(nuovaScena);
          primaryStage.show();
		} catch (Exception e) {
          e.printStackTrace(); 
      }	
	}
	*/ //old
	
	@FXML
	private void caricaViewIngredienti(ActionEvent event) {  // tasto in basso a sx
		
		//InserisciIngredienteViewController inserisciIngredienteViewController = loader.getController();
		//inserisciIngredienteViewController.initData();
		ViewLoader.caricaView(ViewInfo.INSERISCI_INGR);
	}
	
	@FXML
	private void compilaRicetta(ActionEvent event) {  //tasto Richiedi pubblicazione
		RicettaBean ricettaBean= new RicettaBean();
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
		UtenteBean utenteBean=adattatoreLoginController.ottieniUtente();
		ricettaBean.setAutore(utenteBean.getUsername());  //AUTORE PASSATO IN AUTOMATICO
		//VBox ingredienti= InserisciIngredienteViewController.ottieniIstanza().getContenitoreIngredienti();
		VBox ingredienti= ingredientiRicetta;
        if(ingredienti!=null && !(ingredienti.getChildren().isEmpty())) { 
        	adattatorePubblicaRicettaController.compilaLaRicetta(ricettaBean);
        	areaPersonaleButton.fire();  //SE INGREDIENTI MESSI, ALLORA PUBBLICA E SIMULA CLICK PER TORNARE ALL'AREA PERSONALE
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
	private void caricaViewAreaPersonale(ActionEvent event) { 	
		//caricaView.caricaViewAreaPersonale(primaryStage);
		ViewLoader.caricaView(ViewInfo.AREA_CHEF1);
	}
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) { 
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
		//caricaView.caricaViewGestisciRicette(primaryStage);
    }
	
	@FXML
	private void tornaAlLogin(MouseEvent event) {  
		//caricaView.tornaAlLogin(primaryStage);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}
	
}