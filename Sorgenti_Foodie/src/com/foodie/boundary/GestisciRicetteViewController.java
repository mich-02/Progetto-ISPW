package com.foodie.boundary;

import java.util.List;
import java.util.logging.Logger;

import com.foodie.bean.RicettaBean;
import com.foodie.bean.UtenteBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.LoginController;
import com.foodie.controller.PubblicaRicettaController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class GestisciRicetteViewController {
	
	private PubblicaRicettaController pubblicaRicettaController = new PubblicaRicettaController();
	private LoginController loginController = new LoginController();
	private boolean bottoneModifica = true;
	private static final String FORMATO = "Arial";
	private static final Logger logger = Logger.getLogger(GestisciRicetteViewController.class.getName());
	@FXML
	private VBox contenitoreRicette;
	@FXML
	private Label eliminaLabel;
	
	@FXML
	private void initialize() {
		aggiornaView();
	}
	
	private void aggiornaView() {  // trovo tutte le ricette dello chef e aggiorno la view 
		List<RicettaBean> ricetteTrovate = null;
		contenitoreRicette.getChildren().clear();
		UtenteBean utenteBean = loginController.getUtente();
		ricetteTrovate = pubblicaRicettaController.trovaRicetteChef(utenteBean);  // ricerco ricette per username dello chef
		if(!ricetteTrovate.isEmpty()) {
			for(RicettaBean r: ricetteTrovate) {  // creazioni delle parti grafiche
				HBox contenitoreRicettaSingola = new HBox();
				contenitoreRicettaSingola.setAlignment(Pos.CENTER_LEFT);
				contenitoreRicettaSingola.setStyle("-fx-background-color: rgba(217, 217, 217, 0.75);-fx-border-color: black;");
				contenitoreRicettaSingola.setMinHeight(110);
				contenitoreRicettaSingola.setMaxWidth(Double.MAX_VALUE);
				Label labelNome = new Label(r.getNome());
				labelNome.setMinWidth(313);
				labelNome.setMinHeight(65);
				labelNome.setFont(Font.font(FORMATO,20));
				labelNome.setAlignment(Pos.CENTER);
				String difficolta = "";
				switch(r.getDifficolta()) {
					case 1:
						difficolta = "facile";
						break;
					case 2:
						difficolta = "intermedio";
						break;
					case 3:
						difficolta = "difficile";
						break;
					default:
						logger.severe("difficoltà non riconosciuta");
						contenitoreRicette.getChildren().clear();
						return;
				}
				Label labelDifficolta = new Label(difficolta);
				labelDifficolta.setMinWidth(313);
				labelDifficolta.setMinHeight(65);
				labelDifficolta.setFont(Font.font(FORMATO,20));
				labelDifficolta.setAlignment(Pos.CENTER);
				contenitoreRicettaSingola.getChildren().addAll(labelNome,labelDifficolta);
				contenitoreRicette.getChildren().add(contenitoreRicettaSingola);
				
			}
			impostaHBox(); // rendo ricette cliccabili
		}
		if(contenitoreRicette.getChildren().isEmpty() && !bottoneModifica) { // gestione bottone e label di modifica delle ricette
			bottoneModifica = true;
			eliminaLabel.setFont(Font.font(FORMATO,30));
			eliminaLabel.setText("");
		}
	}
	
	private void caricaViewContenutoRicetta(String nomeRicetta) {  
		UtenteBean utenteBean = loginController.getUtente();
		RicettaBean ricettaBean = new RicettaBean();
		ricettaBean.setNome(nomeRicetta);
		ricettaBean.setAutore(utenteBean.getUsername());
		RicettaBean ricettaSelezionata = pubblicaRicettaController.ottieniRicetta(ricettaBean);
		
			if(!bottoneModifica) { //resettare il bottone modifica se attivo
				bottoneModifica=true;
				eliminaLabel.setFont(Font.font(FORMATO,30));
				eliminaLabel.setText("");
			}
			
			FXMLLoader loader = ViewLoader.caricaView(ViewInfo.CONTENUTO_RIC_CHEF);
			ContenutoRicettaChefViewController contenutoRicettaChefViewController = loader.getController();
			contenutoRicettaChefViewController.caricaDatiRicetta(ricettaSelezionata);

	}
	
	@FXML
	private void modifica(ActionEvent event) {  //GESTISCE IL PULSANTE ELIMINA
		if(bottoneModifica && !contenitoreRicette.getChildren().isEmpty()) {
			bottoneModifica=false;
			eliminaLabel.setFont(Font.font(FORMATO,20));
			eliminaLabel.setText("CLICCA LA RICETTA DA ELIMINARE");
		}
		else if(!bottoneModifica && !contenitoreRicette.getChildren().isEmpty()) {
			bottoneModifica=true;
			eliminaLabel.setFont(Font.font(FORMATO,30));
			eliminaLabel.setText("");
		}
		impostaHBox();
	}
	
	private void eliminaRicetta(String nome,String autore) {  
		if(!contenitoreRicette.getChildren().isEmpty()) {
			contenitoreRicette.getChildren().clear();
		}
		RicettaBean ricettaBean = new RicettaBean();
		ricettaBean.setNome(nome);
		ricettaBean.setAutore(autore);
		pubblicaRicettaController.eliminaRicetta(ricettaBean);
		aggiornaView();
	}
	
	private void impostaHBox() {  // imposta ricette cliccabili 
		if(!bottoneModifica) {  //IMPOSTO DI ELIMINARE LE RICETTE
			if(!contenitoreRicette.getChildren().isEmpty()) {
				scorriLabel();  // per smell complessità
			}
		}
		else {  //IMPOSTO DI APRIRE LE RICETTE CLICCATE
			contenitoreRicette.getChildren().forEach(node -> {
		        HBox contenitoreRicetta = (HBox) node;
		        contenitoreRicetta.setOnMouseClicked(event -> {
		            int indiceLabel = 1;
		            popolaLabel(indiceLabel,contenitoreRicetta);
		        });
		    });
		}
	}
	
	private void scorriLabel() {  //SCORRE LE LABEL E LE RENDE CLICCABILI
		for(Node nodo1: contenitoreRicette.getChildren()) {
			int indice = 1;
			HBox hBoxRicetta = (HBox)nodo1;
			for(Node nodo: hBoxRicetta.getChildren()) {
				Label labelNome = (Label)nodo;  //PRENDO SOLO IL NOME 
				UtenteBean utenteBean = loginController.getUtente();
				hBoxRicetta.setOnMouseClicked(event -> eliminaRicetta(labelNome.getText(),utenteBean.getUsername()));
				if(indice == 1) {
					break;
				}
				indice++;
			}
		}
	}
	
	private void popolaLabel(int indiceLabel,HBox contenitoreRicetta) {
		String nomeRicetta = "";
		for (Node labelNode : contenitoreRicetta.getChildren()) {        
            Label label = (Label) labelNode;
            if(indiceLabel==1)
            	nomeRicetta = label.getText();
            else {
            	break;
            }
            indiceLabel++;
		}
		caricaViewContenutoRicetta(nomeRicetta);
	}
	
	@FXML
    private void tornaAlLogin() {  
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
	private void caricaViewRicetta() {  
		ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA1);
	}
	
	
}