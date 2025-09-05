package com.foodie.boundary2;

import java.util.List;

import com.foodie.bean.AlimentoBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.TrovaRicettaController;
import com.foodie.exception.OperazioneNonEseguitaException;

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

public class AggiungiAlimentoView2Controller {
	
	private TrovaRicettaController trovaRicettaController = new TrovaRicettaController();

	@FXML
	private TextField barraDiRicerca;
	@FXML
	private VBox contenitoreAlimentiTrovati;
	
	@FXML
    private void tornaAlLogin(MouseEvent event) {  
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
	private void caricaViewDispensa(ActionEvent event) {  
		ViewLoader.caricaView(ViewInfo.DISPENSA2);
	}
	
	@FXML
    private void caricaViewTrovaRicetta(ActionEvent event) {  
		ViewLoader.caricaView(ViewInfo.TROVA_RICETTE2);
    }
	
	@FXML
	private void gestioneRisultati(KeyEvent event) {  //gestisce barra di ricerca
		if(event.getCode() == KeyCode.ENTER) {//getcode() ti restituisce il tasto premuto
			trovaAlimenti();
		}
		else if(event.getCode() == KeyCode.BACK_SPACE) {
			eliminaAlimenti();
		}
	}

	private void trovaAlimenti() { 
		try {
			eliminaAlimenti();
			AlimentoBean alimentoBean = new AlimentoBean();
			alimentoBean.setNome(barraDiRicerca.getText());
			List<AlimentoBean> alimentiBeanTrovati;
			alimentiBeanTrovati = trovaRicettaController.trovaAlimenti(alimentoBean);
			for(AlimentoBean a: alimentiBeanTrovati) {
				Label labelAlimento = new Label(a.getNome());
				labelAlimento.setStyle("-fx-background-color: white;");
				labelAlimento.setMaxWidth(Double.MAX_VALUE);
				labelAlimento.setMinHeight(30);
				labelAlimento.setWrapText(true);
				labelAlimento.setFont(Font.font("Arial"));
				labelAlimento.setAlignment(Pos.CENTER);  //li rende cliccabili
				labelAlimento.setOnMouseClicked(event2->{salvaAlimento(labelAlimento.getText());eliminaAlimenti();});
				contenitoreAlimentiTrovati.getChildren().add(labelAlimento);
			}
		} catch (OperazioneNonEseguitaException e) {
			Label label = new Label("NESSUN RISULTATO");
			label.setStyle("-fx-background-color: white;");
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(30);
			label.setWrapText(true);
			label.setFont(Font.font("Arial"));
			label.setAlignment(Pos.CENTER);
			contenitoreAlimentiTrovati.getChildren().add(label);
		}
		
	}
	
	private void salvaAlimento(String nomeAlimento) {  
		AlimentoBean alimentoBean = new AlimentoBean();
		alimentoBean.setNome(nomeAlimento);
		trovaRicettaController.aggiungiInDispensa(alimentoBean);
		trovaRicettaController.salvaDispensa(); 
		
	}
	
	private void eliminaAlimenti() { 
		if(!contenitoreAlimentiTrovati.getChildren().isEmpty()) {
			contenitoreAlimentiTrovati.getChildren().clear();
		}
	}
	
}