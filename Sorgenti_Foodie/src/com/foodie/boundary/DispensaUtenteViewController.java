package com.foodie.boundary;

import java.util.List;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.TrovaRicettaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import com.foodie.model.AlimentoBean;
import com.foodie.model.Observer;

public class DispensaUtenteViewController implements Observer {
	

	private TrovaRicettaController trovaRicettaController = new TrovaRicettaController();
	private boolean bottoneModifica = true;
	private static final String FORMATO = "Arial";
	private static final String DISPENSA = "La mia Dispensa";
	private static final String SFONDOBIANCO = "-fx-background-color: white;";
	
	@FXML
	private TextField barraDiRicerca;
	@FXML
	private VBox contenitoreAlimentiTrovati;
	@FXML
	private VBox contenitoreDispensa;
	@FXML
	private Label labelDispensa;
	
	@FXML
	public void initialize() {
		trovaRicettaController.caricaDispense();
		trovaRicettaController.registraOsservatoreDispensa(this);
		aggiornaView();
	}
	
	@FXML
	private void svuotaDispensa(ActionEvent event) {  //SVUOTA DISPENSA
		trovaRicettaController.svuotaDispensa();
	}
	
	/*
	@FXML
	private void caricaViewTrovaRicetta(ActionEvent event) {  //CARICA LA VIEW TROVA RICETTA
		try {
			if(!bottoneModifica) { //resettare il bottone modifica se attivo
				bottoneModifica=true;
				labelDispensa.setFont(Font.font(FORMATO,30));
				labelDispensa.setText(DISPENSA);
			}
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TrovaRicetteView.fxml"));
            TrovaRicetteViewController trovaRicetteViewController = TrovaRicetteViewController.ottieniIstanza();
            loader.setController(trovaRicetteViewController);
            Parent root = loader.load();
            trovaRicette(trovaRicetteViewController);  //TROVO LE RICETTE SUBITO
            trovaRicetteViewController.setPrimaryStage(primaryStage);
            Scene nuovaScena = new Scene(root);
            primaryStage.setScene(nuovaScena);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
	}
	*/ //old
	
	@FXML
	private void caricaViewTrovaRicetta(ActionEvent event) {  //CARICA LA VIEW TROVA RICETTA
		
			if(!bottoneModifica) { //resettare il bottone modifica se attivo
				bottoneModifica=true;
				labelDispensa.setFont(Font.font(FORMATO,30));
				labelDispensa.setText(DISPENSA);
			}
			ViewLoader.caricaView(ViewInfo.TROVA_RICETTE);
	}
	
	/*
	private void trovaRicette(TrovaRicetteViewController trovaRicetteViewController) {  //AGGIORNO DINAMICAMENTE L'FXML TROVANDO SUBITO LE RICETTE 									//LE RICETTE TROVATE SONO PER TUTTE LE DIFFICOLTA' INIZIALMENTE
		int count=0;
		VBox contenitoreRicette=trovaRicetteViewController.getContenitoreRicette();
		for(int i=1;i<4;i++) {  //TROVO TUTTE LE DIFFICOLTA'
			if(trovaRicetteViewController.creaRicetteTrovate(i)==-1) {
				count++;
			}
		}
		if(count==3) {  //SE NON TROVO 3 VOLTE RISULTATI
			Label label=new Label("NESSUN RISULTATO");
			label.setStyle("-fx-background-color: rgba(217, 217, 217, 0.75);-fx-border-color: black;");
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(110);
			label.setWrapText(true);
			label.setFont(Font.font(FORMATO,50));
			label.setAlignment(Pos.CENTER);
			contenitoreRicette.getChildren().add(label);
		}
	}
	*/
	
	@FXML
	private void tornaAlLogin(MouseEvent event) {  
		trovaRicettaController.svuotaDispensa();
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}
	
	@FXML  
	private void gestioneRisultati(KeyEvent event) {  //GESTISCE LA BARRA DI RICERCA DEGLI ALIMENTI
		if(event.getCode() == KeyCode.ENTER) {//GETCODE() TI RESTITUISCE IL TASTO PREMUTO
			trovaAlimenti();
		}
		else if(event.getCode() == KeyCode.BACK_SPACE) {
			eliminaAlimenti();
		}
	}
	
	private void trovaAlimenti() {  //GESTISCE IL TROVA ALIMENTI
			eliminaAlimenti();
			AlimentoBean alimentoBean = new AlimentoBean();
			alimentoBean.setNome(barraDiRicerca.getText());
			List<AlimentoBean> alimentiBeanTrovati = trovaRicettaController.trovaAlimenti(alimentoBean);
			if(!alimentiBeanTrovati.isEmpty()) {
				for(AlimentoBean a: alimentiBeanTrovati) {
					Label labelAlimento = new Label(a.getNome());
					labelAlimento.setStyle(SFONDOBIANCO);
					labelAlimento.setMaxWidth(Double.MAX_VALUE);
					labelAlimento.setMinHeight(30);
					labelAlimento.setWrapText(true);
					labelAlimento.setFont(Font.font(FORMATO));
					labelAlimento.setAlignment(Pos.CENTER);
					labelAlimento.setOnMouseClicked(event2->{salvaAlimento(labelAlimento.getText());eliminaAlimenti();});
					contenitoreAlimentiTrovati.getChildren().add(labelAlimento);
				}
			}
			else {  //NESSUN RISULTATO
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
	
	private void salvaAlimento(String nomeAlimento) {  //SALVA ALIMENTO IN DISPENSA
		AlimentoBean alimentoBean = new AlimentoBean();
		alimentoBean.setNome(nomeAlimento);
		trovaRicettaController.aggiungiInDispensa(alimentoBean);
	}
	
	private void eliminaAlimenti() {  //PULISCE GLI ALIMENTI TROVATI
		if(!contenitoreAlimentiTrovati.getChildren().isEmpty()) {
			contenitoreAlimentiTrovati.getChildren().clear();
		}
	}
	
	public void aggiornaView() {  //AGGIORNA LA VIEW IN BASE ALLA DISPENSA
		if(contenitoreDispensa!=null && !contenitoreDispensa.getChildren().isEmpty()) {
			contenitoreDispensa.getChildren().clear();
		}
		List<AlimentoBean> alimentiBeanDispensa = trovaRicettaController.mostraDispensa();
		if(!alimentiBeanDispensa.isEmpty()) {
			for(AlimentoBean a: alimentiBeanDispensa) {
				Label labelAlimento = new Label(a.getNome());
				labelAlimento.setStyle(SFONDOBIANCO);
				labelAlimento.setMaxWidth(Double.MAX_VALUE);
				labelAlimento.setMinHeight(50);
				labelAlimento.setWrapText(true);
				labelAlimento.setFont(Font.font(FORMATO,20));
				labelAlimento.setAlignment(Pos.CENTER);
				contenitoreDispensa.getChildren().add(labelAlimento);
			}
			impostaLabel();
		}
		if(contenitoreDispensa.getChildren().isEmpty() && !bottoneModifica) { //PER EVITARE CHE SE LA DISPENSA è VUOTA RIMANGA ATTIVO IL BOTTONE E IL TESTO DELLA LABEL
			bottoneModifica=true;
			labelDispensa.setFont(Font.font(FORMATO,30));//ESEMPIO PREMI MODIFICA CANCELLI L'ULTIMO ELEMENTO DELLA DISPENSA ALLORA SI DEVE DISATTIVARE LA MODIFICA
			labelDispensa.setText(DISPENSA);
		}
	}
	
	@FXML
	private void modificaDispensa(ActionEvent e) {  //GESTISCE IL BOTTONE MODIFICA
		if(bottoneModifica && !contenitoreDispensa.getChildren().isEmpty()) {
			bottoneModifica=false;
			labelDispensa.setFont(Font.font(FORMATO,20));
			labelDispensa.setText("CLICCA L'ALIMENTO DA ELIMINARE");
			impostaLabel();
		}
		else if(!bottoneModifica && !contenitoreDispensa.getChildren().isEmpty()) {
			bottoneModifica=true;
			labelDispensa.setFont(Font.font(FORMATO,30));
			labelDispensa.setText(DISPENSA);
			impostaLabel();
		}
	}
	
	private void eliminaAlimento(String nomeAlimento) {  //ELIMINA ALIMENTO DISPENSA
		AlimentoBean alimentoBean = new AlimentoBean();
		alimentoBean.setNome(nomeAlimento);
		trovaRicettaController.rimuoviDallaDispensa(alimentoBean);
	}
	
	private void impostaLabel() {  //IMPOSTA LE LABEL DELLA DISPENSA CLICCABILI
		if(!bottoneModifica) {
			if(!contenitoreDispensa.getChildren().isEmpty()) {
				contenitoreDispensa.getChildren().forEach(node->{
					Label labelAlimento= (Label)node;
					labelAlimento.setOnMouseClicked(event->eliminaAlimento(labelAlimento.getText()));
				});
			}
		}
		else {
			if(!contenitoreDispensa.getChildren().isEmpty()) {
				contenitoreDispensa.getChildren().forEach(node->{
					Label labelAlimento= (Label)node;
					labelAlimento.setOnMouseClicked(null);
				});
			}
		}
	}
	
	@FXML
	private void salvaDispensa(ActionEvent event) {  //SALVA LA DISPENSA
		trovaRicettaController.salvaDispensa();
	}
	
}