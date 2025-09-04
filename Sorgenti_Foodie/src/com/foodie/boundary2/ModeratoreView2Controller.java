package com.foodie.boundary2;

import java.util.List;
import com.foodie.bean.RicettaBean;
import com.foodie.boundary.components.AlertUtils;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.exception.OperazioneNonEseguitaException;
import com.foodie.model.Observer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ModeratoreView2Controller implements Observer{
	
	private PubblicaRicettaController controller = new PubblicaRicettaController();
	private static final String FORMATO = "Arial";
	@FXML
	private TextArea descrizioneTextArea;
	@FXML
	private Label usernameLabel;
	@FXML
	private VBox contenitoreRicetteDaApprovare;
	
	@FXML
	public void initialize() {
	    try {
	        controller.registraOsservatoreModeratore(this);
	        controller.caricaRicetteDaApprovare();
	        aggiornaView();
	    } catch (OperazioneNonEseguitaException e) {
	    	Platform.runLater(() ->
	        AlertUtils.showAlert(
	            Alert.AlertType.INFORMATION, 
	            "Nessuna ricetta da approvare", 
	            e.getMessage()
	        )
	    );
	    }
	}
	
	@FXML
	public void tornaAlLogin(MouseEvent event) { 
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);

	}

	@Override
	public void aggiornaView() {  //AGGIORNA VIEW IN FUNZIONE DELLE RICETTE DA APPROVARE , LE MOSTRA SUBITO COMPLETE 
		contenitoreRicetteDaApprovare.getChildren().clear();
		List<RicettaBean> ricetteBean = controller.mostraRicetteDaApprovare();
		if(!ricetteBean.isEmpty()) {
			for(RicettaBean r: ricetteBean) {
				VBox contenitoreRicetta = new VBox();
				contenitoreRicetta.setStyle("-fx-background-color: rgba(217, 217, 217, 0.75);-fx-border-color: black;");
				contenitoreRicetta.setSpacing(10);
				
			    Label labelNome = new Label(r.getNome());
			    labelNome.setFont(Font.font(FORMATO, 20));

			    Label labelAutore = new Label(r.getAutore());
			    labelAutore.setFont(Font.font(FORMATO, 20));			    
			 
			    Label labelDescrizione = new Label("Descrizione: "+r.getDescrizione());
			    labelDescrizione.setFont(Font.font(FORMATO, 20));
			    labelDescrizione.setWrapText(true);
			    
			    contenitoreRicetta.getChildren().addAll(labelNome,labelAutore,labelDescrizione);
			    contenitoreRicetteDaApprovare.getChildren().add(contenitoreRicetta);
			}
		}
		impostaVBox();  //LE RENDE CLICCABILI
	}
	
	private void impostaVBox() {  //RENDE CLICCABILI LE RICETTE PER APPROVARLE
		 if (!contenitoreRicetteDaApprovare.getChildren().isEmpty()) {
			 contenitoreRicetteDaApprovare.getChildren().forEach(node -> {
		            VBox contenitoreRicetta = (VBox) node;
		            contenitoreRicetta.setOnMouseClicked(event -> pubblicaRicetta(contenitoreRicetta));
		        });
		    }
	}
	
	private void pubblicaRicetta(VBox contenitoreRicetta) {  //PUBBLICA LA RICETTA
		if(!contenitoreRicetteDaApprovare.getChildren().isEmpty()) {
			contenitoreRicetteDaApprovare.getChildren().clear();
		}
		String nome="";
		String autore="";
		int index=1;
		for(Node nodo: contenitoreRicetta.getChildren()) {
			if(index==1) {
				nome=((Label)nodo).getText();
			}
			else if(index==2) {
				autore=((Label)nodo).getText();
			}
			else {
				break;
			}
			index++;
		}
		RicettaBean ricettaBean = new RicettaBean();
		ricettaBean.setNome(nome);
		ricettaBean.setAutore(autore);
		ricettaBean.setPublish(true);
		try {
			controller.pubblicaRicetta(ricettaBean);
		} catch (OperazioneNonEseguitaException e) {
			AlertUtils.showAlert(Alert.AlertType.ERROR, "Pubblicazione fallita", e.getMessage());
		}
	}
	
	@FXML
	private void scarta(ActionEvent event) {  //SE CLICCATO CHIUDI SCARTA TUTTE LE RICETTE
		if(!contenitoreRicetteDaApprovare.getChildren().isEmpty()) {
			contenitoreRicetteDaApprovare.getChildren().clear();
		}
		String nome="";
		String autore="";
		for(Node nodo: contenitoreRicetteDaApprovare.getChildren()) {
			int index=1;
			VBox contenitoreRicetta = (VBox) nodo;
			for(Node nodo2: contenitoreRicetta.getChildren()) {
				if(index==1) {
					nome=((Label)nodo2).getText();
				}
				else if(index==2) {
					autore=((Label)nodo2).getText();
				}
				else {
					break;
				}
				index++;
			}
			RicettaBean ricettaBean = new RicettaBean();
			ricettaBean.setNome(nome);
			ricettaBean.setAutore(autore);
			ricettaBean.setPublish(false);
			try {
				controller.pubblicaRicetta(ricettaBean);
			} catch (OperazioneNonEseguitaException e) {
				//non faccio nulla
			}
		}
	}
	
}