package com.foodie.boundary2;

import java.util.Map;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.AdattatoreFactory;
import com.foodie.controller.ControllerAdapter;
import com.foodie.controller.LoginController;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.model.UtenteBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AreaPersonaleView2Controller {
	
	//private static AreaPersonaleView2Controller istanza;
	private AdattatoreFactory factory = AdattatoreFactory.ottieniIstanza();
	private LoginController controller = new LoginController(); //tolto singleton
	private PubblicaRicettaController pubblicaRicettaController = new PubblicaRicettaController();
//	private ControllerAdapter adattatoreLoginController= factory.creaLoginAdapter();

	@FXML
	private TextArea descrizioneTextArea;
	@FXML
	private Label usernameLabel;
	
	/*
	private AreaPersonaleView2Controller() {
		
	}
	
	public static synchronized AreaPersonaleView2Controller ottieniIstanza() { //SINGLETON METODO PER OTTENERE L'ISTANZA
		if(istanza == null) {
			istanza = new AreaPersonaleView2Controller();
		}
		return istanza;
	}
	*/
	
	@FXML
	public void initialize() {
		caricaAreaPersonale();
		aggiornaView();	
	}
	
	@FXML
    private void tornaAlLogin(MouseEvent event) { //CARICA VIEW LOGIN
       // caricaView2.tornaAlLogin(primaryStage);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) {  //CARICA VIEW GESTISCI RICETTE
        //caricaView2.caricaViewGestisciRicette(primaryStage);
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE2);
    }
	
	@FXML
	private void caricaViewRicetta(ActionEvent event) {  //CARICA VIEW RICETTA
		//caricaView2.caricaViewRicetta(primaryStage);
		ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA2);
	}
	
	public void aggiornaView() {  //AGGIORNA USERNAME AUTOMATICAMENTE
		UtenteBean utenteBean = controller.getUtente();
		usernameLabel.setText(utenteBean.getUsername());
	}

	@FXML
	private void salvaAreaPersonale(KeyEvent event) {  //PREMUTO ENTER SALVA AREA PERSONALE(DESCRIZIONE)
		if(event.getCode() == KeyCode.ENTER) {
			pubblicaRicettaController.salvaAreaPersonale(descrizioneTextArea.getText());
		}
	}
	
	public void caricaAreaPersonale() {  //CARICA AREA PERSONALE(DESCRIZIONE)
		Map<String,String> areaPersonaleMap = pubblicaRicettaController.caricaAreaPersonale();
		String descrizione="";
		if(areaPersonaleMap!=null) {
			UtenteBean utenteBean = controller.getUtente();
			descrizione= areaPersonaleMap.get(utenteBean.getUsername());
		}
		descrizioneTextArea.setText(descrizione);
	}
		
}