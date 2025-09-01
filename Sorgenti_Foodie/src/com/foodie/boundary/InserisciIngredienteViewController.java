package com.foodie.boundary;

import java.util.List;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.controller.TrovaRicettaController;
import com.foodie.model.AlimentoBean;
import com.foodie.model.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class InserisciIngredienteViewController implements Observer{
	

	private PubblicaRicettaController pubblicaRicettaController = new PubblicaRicettaController();
	private TrovaRicettaController trovaRicettaController = new TrovaRicettaController();
	private boolean bottoneModifica = true;
	private static final String FORMATO = "Arial";
	private static final String SFONDOBIANCO = "-fx-background-color: white;";
	@FXML
	private Label labelIngredienti;
	@FXML
	private VBox contenitoreIngredienti;
	@FXML
	private VBox contenitoreAlimentiTrovati;
	@FXML
	private TextField barraDiRicerca;
	@FXML
	private TextField quantita;
	private String nome;
	private String descrizione;
	private int difficolta;
	
	@FXML
	public void initialize() {
		pubblicaRicettaController.registraOsservatoreRicetta(this);
		aggiornaView();
	}
	
/*	
	private InserisciIngredienteViewController() {
	}
	
	public static synchronized InserisciIngredienteViewController ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza == null) {
			istanza = new InserisciIngredienteViewController();
		}
		return istanza;
	}
*/	
	
	@FXML
	private void gestioneRisultati(KeyEvent event) {  //GESTISCE LA BARRA DI RICERCA DEGLI ALIMENTI
		if(event.getCode() == KeyCode.ENTER) {//GETCODE() TI RESTITUISCE IL TASTO PREMUTO
			trovaAlimenti();
		}
		else if(event.getCode() == KeyCode.BACK_SPACE) {
			eliminaAlimenti();
		}
	}
	
	private void eliminaAlimenti() {  //PULISCE GLI ALIMENTI TROVATI
		if(!contenitoreAlimentiTrovati.getChildren().isEmpty()) {
			this.quantita.setDisable(true);
			contenitoreAlimentiTrovati.getChildren().clear();
		}
	}
	
	private void salvaAlimento(String nomeAlimento, String quantita) { //SALVA ALIMENTO NELLA RICETTA
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
/*
	@FXML
	public void caricaViewRicetta(ActionEvent event) {  //tasto Torna alla ricetta
		try {
			if(!bottoneModifica) { //resettare il bottone modifica se attivo
				bottoneModifica=true;
				labelIngredienti.setFont(Font.font(FORMATO,30));
				labelIngredienti.setText("La mia Dispensa");
			}
			
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuovaRicettaView.fxml"));
            NuovaRicettaViewController nuovaRicettaViewController = new NuovaRicettaViewController();
            NuovaRicettaViewController nuovaRicettaViewController= NuovaRicettaViewController.ottieniIstanza();
            loader.setController(nuovaRicettaViewController);
            Parent root = loader.load();
            nuovaRicettaViewController.setPrimaryStage(primaryStage);
            nuovaRicettaViewController.aggiornaView(nome, descrizione, difficolta);  //RIPOPOLA LO STATO
            Scene nuovaScena = new Scene(root);
            primaryStage.setScene(nuovaScena);
            primaryStage.show();
        } catch (Exception e) {
          e.printStackTrace(); 
	}
       
      }
*/ //old
	
	@FXML
	public void caricaViewRicetta(ActionEvent event) {  //tasto Torna alla ricetta
			if(!bottoneModifica) { //resettare il bottone modifica se attivo
				bottoneModifica = true;
				labelIngredienti.setFont(Font.font(FORMATO,30));
				labelIngredienti.setText("La mia Dispensa");
			}
			ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA1);
			//FXMLLoader loader =  ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA1);
			//NuovaRicettaViewController nuovaRicettaController = loader.getController();
			//nuovaRicettaController.initData(contenitoreIngredienti);
	}
	
	@FXML
	public void tornaAlLogin(MouseEvent event) { 
		//caricaView.tornaAlLogin(primaryStage);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}
	
	private void trovaAlimenti() { //GESTISCE IL TROVA ALIMENTI
		eliminaAlimenti();
		AlimentoBean alimentoBean = new AlimentoBean();
		alimentoBean.setNome(barraDiRicerca.getText());
		List<AlimentoBean> alimentiBeanTrovati = trovaRicettaController.trovaAlimenti(alimentoBean);
		if(!alimentiBeanTrovati.isEmpty()) {
			quantita.setDisable(false);
			for(AlimentoBean a: alimentiBeanTrovati) {
				Label labelAlimento = new Label(a.getNome());
				labelAlimento.setStyle(SFONDOBIANCO);
				labelAlimento.setMaxWidth(Double.MAX_VALUE);
				labelAlimento.setMinHeight(30);
				labelAlimento.setWrapText(true);
				labelAlimento.setFont(Font.font(FORMATO));
				labelAlimento.setAlignment(Pos.CENTER);
				labelAlimento.setOnMouseClicked(event2->salvaAlimento(labelAlimento.getText(),quantita.getText()));
				contenitoreAlimentiTrovati.getChildren().add(labelAlimento);
			}
		}
		else {//NESSUN RISULTATO
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
	public void aggiornaView() {  //AGGIORNA GLI INGREDIENTI DELLA RICETTA
		contenitoreIngredienti.getChildren().clear();
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
				contenitoreIngredienti.getChildren().add(labelAlimento);
			}
			impostaLabel();
		}
		if(contenitoreIngredienti.getChildren().isEmpty() && !bottoneModifica) { //PER EVITARE CHE SE LA DISPENSA è VUOTA RIMANGA ATTIVO IL BOTTONE E IL TESTO DELLA LABEL
			bottoneModifica=true;
			labelIngredienti.setFont(Font.font(FORMATO,30));//ESEMPIO PREMI MODIFICA CANCELLI L'ULTIMO ELEMENTO DELLA DISPENSA ALLORA SI DEVE DISATTIVARE LA MODIFICA
			labelIngredienti.setText("Ingredienti");
		}
	}
	
	private void impostaLabel() {  //IMPOSTA LE LABEL DELLA RICETTA CLICCABILI
		if(!bottoneModifica) {
			if(!contenitoreIngredienti.getChildren().isEmpty()) {
				contenitoreIngredienti.getChildren().forEach(node->{
					Label labelAlimento= (Label)node;
					labelAlimento.setOnMouseClicked(event->eliminaAlimento(labelAlimento.getText()));
				});
			}
		}
		else {
			if(!contenitoreIngredienti.getChildren().isEmpty()) {
				contenitoreIngredienti.getChildren().forEach(node->{
					Label labelAlimento= (Label)node;
					labelAlimento.setOnMouseClicked(null);
				});
			}
		}
	}
	
	private void eliminaAlimento(String nomeAlimento) {  //ELIMINA ALIMENTO RICETTA
		AlimentoBean alimentoBean = new AlimentoBean();
		alimentoBean.setNome(nomeAlimento);
		pubblicaRicettaController.eliminaIngredienteRicetta(alimentoBean);
	}
	
	@FXML
	private void modificaIngredienti(ActionEvent e) {  //tasto Modifica
		if(bottoneModifica && !contenitoreIngredienti.getChildren().isEmpty()) {
			bottoneModifica=false;
			labelIngredienti.setFont(Font.font(FORMATO,20));
			labelIngredienti.setText("CLICCA L'ALIMENTO DA ELIMINARE");
			impostaLabel();
		}
		else if(!bottoneModifica && !contenitoreIngredienti.getChildren().isEmpty()) {
			bottoneModifica=true;
			labelIngredienti.setFont(Font.font(FORMATO,30));
			labelIngredienti.setText("Ingredienti");
			impostaLabel();
		}
	}
	
	public void setNome(String text) {  //SETTERS PER SALVARE LO STATO DEL NUOVA RICETTA VIEW
		this.nome=text;
	}
	
	public void setDescrizione(String text) {
		this.descrizione=text;
	}
	
	public void setDifficolta(int i) {
		this.difficolta=i;
	}
	
	public VBox getContenitoreIngredienti() {  //RESTITUISCE CONTENITORE INGREDIENTI
		return contenitoreIngredienti;
	}
	
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) {  //CARICA VIEW GESTISCI RICETTE
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
    }
	
}