package com.foodie.boundary2;

import java.util.List;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.TrovaRicettaController;
import com.foodie.model.AlimentoBean;
import com.foodie.model.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DispensaView2Controller implements Observer{
	
	private TrovaRicettaController trovaRicettaController = new TrovaRicettaController();
	@FXML
	private VBox contenitoreDispensa;
	
	@FXML
	public void initialize() {
		trovaRicettaController.caricaDispense();
		trovaRicettaController.registraOsservatoreDispensa(this);
		aggiornaView();
	}
	
	@FXML
    private void tornaAlLogin(MouseEvent event) {
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
    private void caricaViewAlimenti(ActionEvent event) { 
		ViewLoader.caricaView(ViewInfo.AGGIUNGI_ALIMENTO);

    }
	
	@FXML
    private void caricaViewTrovaRicetta(ActionEvent event) { 
		ViewLoader.caricaView(ViewInfo.TROVA_RICETTE2);
    }
	
	@FXML
	private void svuotaDispensa(ActionEvent event) {  //SVUOTA DISPENSA
		trovaRicettaController.svuotaDispensa();
		trovaRicettaController.salvaDispensa(); //SALVO DISPENSA 
	}
	
	public void aggiornaView() {  //AGGIORNA LA VIEW IN FUNZIONE DELLA DISPENSA
		contenitoreDispensa.getChildren().clear();
		List<AlimentoBean> alimentiBeanDispensa = trovaRicettaController.mostraDispensa();
		if(!alimentiBeanDispensa.isEmpty()) {
			for(AlimentoBean a: alimentiBeanDispensa) {
				Label labelAlimento = new Label(a.getNome());
				labelAlimento.setStyle("-fx-background-color: white;");
				labelAlimento.setMaxWidth(Double.MAX_VALUE);
				labelAlimento.setMinHeight(50);
				labelAlimento.setWrapText(true);
				labelAlimento.setFont(Font.font("Arial",20));
				labelAlimento.setAlignment(Pos.CENTER);
				contenitoreDispensa.getChildren().add(labelAlimento);
			}
			impostaLabel();
		}
	}
	
	private void impostaLabel() {  //IMPOSTA LABEL DISPENSA CLICCABILI
			if(!contenitoreDispensa.getChildren().isEmpty()) {
				contenitoreDispensa.getChildren().forEach(node->{
					Label labelAlimento= (Label)node;
					labelAlimento.setOnMouseClicked(event->eliminaAlimento(labelAlimento.getText()));
				});
			}
		}
	
	private void eliminaAlimento(String nomeAlimento) {  //ELIMINA ALIMENTO DALLA DISPENSA
		AlimentoBean alimentoBean = new AlimentoBean();
		alimentoBean.setNome(nomeAlimento);
		trovaRicettaController.rimuoviDallaDispensa(alimentoBean);
		trovaRicettaController.salvaDispensa(); //SALVO DISPENSA 
	}
	
}