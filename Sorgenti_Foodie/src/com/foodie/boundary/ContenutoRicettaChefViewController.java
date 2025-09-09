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


public class ContenutoRicettaChefViewController {
	
	private static final String FORMATO = "Arial";
	@FXML
	private Label nome;
	@FXML
	private Label descrizione;
	@FXML
	private VBox contenitoreIngredienti;
	
	@FXML  
    private void tornaAlLogin(MouseEvent event) { 
        ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) { 
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
    }
	
	@FXML
	private void caricaViewRicetta(ActionEvent event) { 
		ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA1);
	}
	
	
	public void caricaDatiRicetta(RicettaBean ricettaBean) {  //METODO PER POPOLARE GRAFICAMENTE I DATI DELLA RICETTA
		nome.setText(ricettaBean.getNome());
		descrizione.setText(ricettaBean.getDescrizione());
		for(int i = 0; i < ricettaBean.getIngredienti().size(); i++) {
			Label label = new Label(ricettaBean.getIngredienti().get(i).getNome()+": "+ricettaBean.getQuantita().get(i));
			label.setStyle("-fx-background-color: white;");
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(50);
			label.setWrapText(true);
			label.setFont(Font.font(FORMATO,20));
			contenitoreIngredienti.getChildren().add(label);
		}
	}	
}