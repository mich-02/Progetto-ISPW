package com.foodie.boundary;

import javafx.fxml.FXML;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.model.RicettaBean;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
//import javafx.stage.Stage;

public class ContenutoRicettaChefViewController {
	
	private static final String FORMATO = "Arial";
	@FXML
	private Label nome;
	@FXML
	private Label descrizione;
	@FXML
	private VBox contenitoreIngredienti;
	
	private RicettaBean ricettaSelezionata;
	
	/*
	private ContenutoRicettaChefViewController() {	
	}
	
	public static synchronized ContenutoRicettaChefViewController ottieniIstanza() { //SINGLETON	
		if(istanza == null) {
			istanza = new ContenutoRicettaChefViewController();
		}
		return istanza;
	}
	*/
	
	public Label getNome() {  //RESTITUISCO LABEL E INFINE VBOX
		return nome;
	}
	
	public Label getDescrizione() {
		return descrizione;
	}
	
	public VBox getContenitoreIngredienti() {
		return contenitoreIngredienti;
	}
	
	@FXML  
    private void tornaAlLogin(MouseEvent event) { 
        //caricaView.tornaAlLogin(primaryStage);
        ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) {  //CARICA VIEW GESTISCI RICETTE
        //caricaView.caricaViewGestisciRicette(primaryStage);
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
    }
	
	@FXML
	private void caricaViewRicetta(ActionEvent event) {  //CARICA VIEW NUOVA RICETTA DA CREARE
		//caricaView.caricaViewRicetta(primaryStage);
		ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA1);
	}
	
	@FXML
	private void caricaViewAreaPersonale(ActionEvent event) {  //CARICA VIEW AREA PERSONALE	
		//caricaView.caricaViewAreaPersonale(primaryStage);
		ViewLoader.caricaView(ViewInfo.AREA_CHEF1);
	}
	
	private void caricaDatiRicetta(RicettaBean ricettaBean) {  //METODO PER POPOLARE GRAFICAMENTE I DATI DELLA RICETTA
		Label labelNome = getNome();
		Label labelDescrizione = getDescrizione();
		VBox contenitoreIngredienti= getContenitoreIngredienti();
		labelNome.setText(ricettaBean.getNome());
		labelDescrizione.setText(ricettaBean.getDescrizione());
		for(int i=0;i<ricettaBean.getIngredienti().size();i++) {
			Label label=new Label(ricettaBean.getIngredienti().get(i).getNome()+": "+ricettaBean.getQuantita().get(i));
			label.setStyle("-fx-background-color: white;");
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(50);
			label.setWrapText(true);
			label.setFont(Font.font(FORMATO,20));
			contenitoreIngredienti.getChildren().add(label);
		}
	}
	
	public void initData(RicettaBean ricettaBean) {
        this.ricettaSelezionata = ricettaBean;  // memorizzo il bean
        caricaDatiRicetta(ricettaSelezionata);  // popolo la GUI
    }
	
}