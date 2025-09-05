package com.foodie.boundary2;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.foodie.bean.AlimentoBean;
import com.foodie.bean.RicettaBean;
import com.foodie.bean.UtenteBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.LoginController;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.controller.TrovaRicettaController;
import com.foodie.exception.OperazioneNonEseguitaException;
import com.foodie.model.Observer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class NuovaRicettaView2Controller implements Observer{
	
	private LoginController loginController = new LoginController();
	private TrovaRicettaController trovaRicettaController = new TrovaRicettaController();
	private PubblicaRicettaController pubblicaRicettaController = new PubblicaRicettaController();

	private static final String FORMATO = "Arial";
	private static final String SFONDOBIANCO = "-fx-background-color: white;";
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
	private VBox ingredienti;
	@FXML
	private VBox contenitoreAlimentiTrovati;
	@FXML
	private TextField barraDiRicerca;
	@FXML
	private TextField quantita;
	
	@FXML
	public void initialize() {
		pubblicaRicettaController.registraOsservatoreRicetta(this);
		aggiornaView();
	}
	
	@FXML
	private void tornaAlLogin(MouseEvent event) {  
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}
	
	@FXML
	private void disabilitaPulsanti(ActionEvent event) {  //gestisci pulsanti difficolta'
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
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE2);
    }
	
	@FXML
	private void compilaRicetta(ActionEvent event) {  
		RicettaBean ricettaBean= new RicettaBean();
		String testo = nome.getText().trim();
		if(!testo.isEmpty()) {
			ricettaBean.setNome(nome.getText());
		}
		else {  //tutti gli else servono per creare avvertimenti grafici
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
		int diff = 0;
		if(facile.isSelected()) {
			diff = 1;
		}
		else if(medio.isSelected()) {
			diff = 2;
		}
		else if(difficile.isSelected()) {
			diff = 3;
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
        if(ingredienti!=null && !(ingredienti.getChildren().isEmpty())) { 
        	pubblicaRicettaController.compilaRicetta(ricettaBean); //richiesta pubblicazione
        	gestisciRicetteButton.fire();  //simula click view gestisci ricette
        	
		}
		else {  //avvertimento grafico ingredienti
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
	private void gestioneRisultati(KeyEvent event) {  //gestisce barra di ricerca alimenti
		if(event.getCode() == KeyCode.ENTER) { //getcode() ti restituisce il tasto premuto
			trovaAlimenti();
		}
		else if(event.getCode() == KeyCode.BACK_SPACE) {
			eliminaAlimenti();
		}
	}
	private void eliminaAlimenti() { 
		if(!contenitoreAlimentiTrovati.getChildren().isEmpty()) {
			this.quantita.setDisable(true);
			contenitoreAlimentiTrovati.getChildren().clear();
		}
	}
	private void salvaAlimento(String nomeAlimento, String quantita) {  //salva alimento alla ricetta
		if(!quantita.isEmpty()) {
			AlimentoBean alimentoBean = new AlimentoBean();
			alimentoBean.setNome(nomeAlimento);
			alimentoBean.setQuantita(quantita);
			pubblicaRicettaController.aggiungiIngredienteRicetta(alimentoBean);
			this.quantita.clear();
			this.quantita.setPromptText("Quantita");
			eliminaAlimenti();
		}
		else {
			this.quantita.setPromptText("QUANTITA?");
		}
	}	
	
	private void trovaAlimenti() {  
		try {
			eliminaAlimenti();
			AlimentoBean alimentoBean = new AlimentoBean();
			alimentoBean.setNome(barraDiRicerca.getText());
			List<AlimentoBean> alimentiBeanTrovati = trovaRicettaController.trovaAlimenti(alimentoBean);
			quantita.setDisable(false);
			for(AlimentoBean a: alimentiBeanTrovati) {
				Label labelAlimento = new Label(a.getNome());
				labelAlimento.setStyle(SFONDOBIANCO);
				labelAlimento.setMaxWidth(Double.MAX_VALUE);
				labelAlimento.setMinHeight(30);
				labelAlimento.setWrapText(true);
				labelAlimento.setFont(Font.font(FORMATO));
				labelAlimento.setAlignment(Pos.CENTER);  //li rende cliccabili
				labelAlimento.setOnMouseClicked(event2->salvaAlimento(labelAlimento.getText(),quantita.getText()));
				contenitoreAlimentiTrovati.getChildren().add(labelAlimento);
			}
		} catch (OperazioneNonEseguitaException e) {
			Label label = new Label("NESSUN RISULTATO");
			label.setStyle(SFONDOBIANCO);
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(30);
			label.setWrapText(true);
			label.setFont(Font.font(FORMATO));
			label.setAlignment(Pos.CENTER);
			contenitoreAlimentiTrovati.getChildren().add(label);

		}
	}

	@Override
	public void aggiornaView() {  //aggiorna gli ingredienti in funzione dei cambiamenti della ricetta
		ingredienti.getChildren().clear();
		List<AlimentoBean> alimentiBeanRicetta = pubblicaRicettaController.mostraIngredientiRicetta();
		if(!alimentiBeanRicetta.isEmpty()) {
			for(AlimentoBean a: alimentiBeanRicetta) {
				Label labelAlimento = new Label(a.getNome());
				labelAlimento.setStyle(SFONDOBIANCO);
				labelAlimento.setMaxWidth(Double.MAX_VALUE);
				labelAlimento.setMinHeight(50);
				labelAlimento.setWrapText(true);
				labelAlimento.setFont(Font.font(FORMATO,20));
				labelAlimento.setAlignment(Pos.CENTER);
				ingredienti.getChildren().add(labelAlimento);
			}
			impostaLabel();
		}
	}
	
	private void impostaLabel() {  //imposta label ingredienti ricetta cliccabili
		if(!ingredienti.getChildren().isEmpty()) {
			ingredienti.getChildren().forEach(node->{
				Label labelAlimento = (Label)node;
				labelAlimento.setOnMouseClicked(event->eliminaAlimento(labelAlimento.getText()));
			});
		}
	}
	
	private void eliminaAlimento(String nomeAlimento) { 
		AlimentoBean alimentoBean = new AlimentoBean();
		alimentoBean.setNome(nomeAlimento);
		pubblicaRicettaController.eliminaIngredienteRicetta(alimentoBean);
	}
	
}