package com.foodie.boundary;

import javafx.fxml.FXML;

import com.foodie.bean.RicettaBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
//import javafx.stage.Stage;

public class ContenutoRicettaViewController {
	
//	private static ContenutoRicettaViewController istanza;
//	private Stage primaryStage;
//	private CaricaView caricaView= CaricaView.ottieniIstanza();
	@FXML
	private Label nome;
	@FXML
	private Label descrizione;
	@FXML
	private VBox contenitoreIngredienti;
	
	private static final String FORMATO = "Arial";
	
	/*
	private ContenutoRicettaViewController() {	
	}
	
	public static synchronized ContenutoRicettaViewController ottieniIstanza() { //SINGLETON
		if(istanza == null) {
			istanza = new ContenutoRicettaViewController();
		}
		return istanza;
	}
	*/
	
	/*
	public void setPrimaryStage(Stage primaryStage) {  //PASSO LO STAGE
		this.primaryStage = primaryStage;
	}
	*/
	
	@FXML
	public void caricaViewDispensa(ActionEvent event) {  //CARICA VIEW DISPENSA
		//caricaView.caricaViewDispensa(primaryStage);
		ViewLoader.caricaView(ViewInfo.DISPENSA_UTENTE);
	}
	
	@FXML
	public void tornaAlLogin(MouseEvent event) {  
		//caricaView.tornaAlLogin(primaryStage);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}
	
	public Label getNome() { //RESTITUICONO LABEL E INFINE VBOX
		return nome;
	}
	
	public Label getDescrizione() {
		return descrizione;
	}
	
	public VBox getContenitoreIngredienti() {
		return contenitoreIngredienti;
	}
	
	private void caricaDatiRicetta(RicettaBean ricettaBean) {  //POPOLA GRAFICAMENTE IL CONTENUTO DELLA RICETTA NEL FXML
		Label labelNome= getNome();
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

	public void initData(RicettaBean ricettaSelezionata) {
		caricaDatiRicetta(ricettaSelezionata);
	}
	
}