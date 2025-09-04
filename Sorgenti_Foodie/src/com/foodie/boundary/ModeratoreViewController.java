package com.foodie.boundary;

import java.util.List;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.exception.OperazioneNonEseguitaException;
import com.foodie.model.Observer;
import com.foodie.bean.RicettaBean;
import com.foodie.boundary.components.AlertUtils;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class ModeratoreViewController implements Observer {
	
	private PubblicaRicettaController controller = new PubblicaRicettaController();
	private static final String FORMATO = "Arial";
	private static final String SFONDOBIANCO = "-fx-background-color: white;";
	@FXML
	private VBox contenitoreRicetteDaApprovare;
	@FXML
	private VBox contenitoreContenutoRicetta;
	
	@FXML
	public void initialize() {
	    try {
	        controller.registraOsservatoreModeratore(this);
	        controller.caricaRicetteDaApprovare();
	        aggiornaView();
	    } catch (OperazioneNonEseguitaException e) {
	        Platform.runLater(() -> {
	            AlertUtils.showAlert(
	                Alert.AlertType.INFORMATION, 
	                "Nessuna ricetta da approvare", 
	                e.getMessage()
	            );
	        });
	    }
	}

	
	@Override
	public void aggiornaView() {  //AGGIORNA VIEW IN BASE ALLE RICHIESTE DA APPROVARE DEL MODERATORE
		contenitoreRicetteDaApprovare.getChildren().clear();
		contenitoreContenutoRicetta.getChildren().clear();
		List<RicettaBean> ricetteBean = controller.mostraRicetteDaApprovare();
		if(!ricetteBean.isEmpty()) {
			for(RicettaBean r: ricetteBean) {
				Label labelRicetta = new Label(r.getNome());
				labelRicetta.setStyle(SFONDOBIANCO);
				labelRicetta.setMaxWidth(Double.MAX_VALUE);
				labelRicetta.setMinHeight(50);
				labelRicetta.setWrapText(true);
				labelRicetta.setFont(Font.font(FORMATO,20));
				labelRicetta.setAlignment(Pos.CENTER);
				labelRicetta.setOnMouseClicked(event->apriContenuto(r));  //RENDE CLICCABILI PER APRIRE IL CONTENUTO
				contenitoreRicetteDaApprovare.getChildren().add(labelRicetta);
			}
		}
	}
	
	private void apriContenuto(RicettaBean ricettaBean) {  //APRE IL COTNENUTO DELLA RICETTA CLICCATA
		contenitoreContenutoRicetta.getChildren().clear();
		Label labelNome= new Label(ricettaBean.getNome());
		labelNome.setStyle(SFONDOBIANCO);
		labelNome.setMaxWidth(Double.MAX_VALUE);
		labelNome.setMinHeight(50);
		labelNome.setWrapText(true);
		labelNome.setFont(Font.font(FORMATO,20));
		Label labelAutore= new Label(ricettaBean.getAutore());
		labelAutore.setStyle(SFONDOBIANCO);
		labelAutore.setMaxWidth(Double.MAX_VALUE);
		labelAutore.setMinHeight(50);
		labelAutore.setWrapText(true);
		labelAutore.setFont(Font.font(FORMATO,20));
		Label labelDescrizione= new Label(ricettaBean.getDescrizione());
		labelDescrizione.setStyle(SFONDOBIANCO);
		labelDescrizione.setMaxWidth(Double.MAX_VALUE);
		labelDescrizione.setMinHeight(200);
		labelDescrizione.setWrapText(true);
		labelDescrizione.setFont(Font.font(FORMATO,15));
		contenitoreContenutoRicetta.getChildren().addAll(labelNome,labelAutore,labelDescrizione);
	}
	
	@FXML
	private void pubblica(ActionEvent event) {  //PUBBLICA LA RICETTA 
		if(!contenitoreContenutoRicetta.getChildren().isEmpty()) {
			int indiceLabel=1;
			String nome="";
			String autore="";
			for (Node labelNode : contenitoreContenutoRicetta.getChildren()) {	        
                Label label = (Label) labelNode;
                if(indiceLabel==1)
                	nome=label.getText();
                else if(indiceLabel==2)
                	autore=label.getText();
                indiceLabel++;
			}
			RicettaBean ricettaBean = new RicettaBean();
			ricettaBean.setNome(nome);
			ricettaBean.setAutore(autore);
			ricettaBean.setPublish(true);
			try {
				controller.pubblicaRicetta(ricettaBean);
			} catch (OperazioneNonEseguitaException e) {
				AlertUtils.showAlert(Alert.AlertType.ERROR, "Pubblicazione fallita", "Ricetta già esistente per questo chef");
			}
		}
	}
	
	@FXML
	private void scarta(ActionEvent event) {  //SCARTA LA RICETTA
		if(!contenitoreContenutoRicetta.getChildren().isEmpty()) {
			int indiceLabel=1;
			String nome="";
			String autore="";
			for (Node labelNode : contenitoreContenutoRicetta.getChildren()) {	        
                Label label = (Label) labelNode;
                if(indiceLabel==1)
                	nome=label.getText();
                else if(indiceLabel==2)
                	autore=label.getText();
                indiceLabel++;
			}
			RicettaBean ricettaBean = new RicettaBean();
			ricettaBean.setNome(nome);
			ricettaBean.setAutore(autore);
			ricettaBean.setPublish(false);
			try {
				controller.pubblicaRicetta(ricettaBean);
			} catch (OperazioneNonEseguitaException e) {
			}
		}
	}
	
	@FXML  
	private void tornaAlLogin(MouseEvent event) {  //CARICA VIEW LOGIN
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
		
	}
	
}