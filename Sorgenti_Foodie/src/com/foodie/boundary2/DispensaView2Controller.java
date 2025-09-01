package com.foodie.boundary2;



import java.util.List;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.LoginController;
import com.foodie.controller.PubblicaRicettaController;
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
import javafx.stage.Stage;

public class DispensaView2Controller implements Observer{
	
//	PubblicaRicettaController controller1 = new PubblicaRicettaController();
	private TrovaRicettaController trovaRicettaController = new TrovaRicettaController();
//	private LoginController loginController= new LoginController(); //tolto singleton
//	private ControllerAdapter adattatoreTrovaRicettaController= factory.creaTrovaRicettaAdapter();
	
	@FXML
	private VBox contenitoreDispensa;
	
	/*
	private DispensaView2Controller() {	
	}
	
	public static synchronized DispensaView2Controller ottieniIstanza() { //SINGLETON  METODO PER OTTENERE L'ISTANZA
		if(istanza == null) {
			istanza = new DispensaView2Controller();
		}
		return istanza;
	}
	*/
	
	@FXML
	public void initialize() {
		trovaRicettaController.registraOsservatoreDispensa(this);
		aggiornaView();
	}
	
	
	@FXML
    private void tornaAlLogin(MouseEvent event) { //CARICA VIEW LOGIN
        //caricaView2.tornaAlLogin(primaryStage);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
    private void caricaViewAlimenti(ActionEvent event) {  //CARICA VIEW TROVA ALIMENTI
        //caricaView2.caricaViewAlimenti(primaryStage);
		ViewLoader.caricaView(ViewInfo.AGGIUNGI_ALIMENTO);

    }
	
	@FXML
    private void caricaViewTrovaRicetta(ActionEvent event) {  //CARICA VIEW TROVA RICETTA
        //caricaView2.caricaViewTrovaRicetta(primaryStage);
		ViewLoader.caricaView(ViewInfo.TROVA_RICETTE2);
    }
	
	@FXML
	private void svuotaDispensa(ActionEvent event) {  //SVUOTA DISPENSA
		trovaRicettaController.svuotaDispensa();
		trovaRicettaController.salvaDispensa(); //SALVO DISPENSA SU FILE IN AUTOMATICO
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
		trovaRicettaController.salvaDispensa(); //SALVO DISPENSA SU FILE IN AUTOMATICO
	}
	
}