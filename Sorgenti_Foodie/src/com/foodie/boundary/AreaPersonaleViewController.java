package com.foodie.boundary;

import java.util.Map;
import com.foodie.controller.LoginController;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.model.UtenteBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.AdattatoreFactory;
import com.foodie.controller.ControllerAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

//import javafx.stage.Stage;

public class AreaPersonaleViewController{
	
	private AdattatoreFactory factory = AdattatoreFactory.ottieniIstanza();
	private LoginController controller = new LoginController(); //tolto singleton
//	private ControllerAdapter adattatoreLoginController = factory.creaLoginAdapter();

	@FXML
    private ImageView tornaAlLoginImageView;
	@FXML
	private TextArea descrizioneTextField;
	@FXML
	private Label usernameLabel;
	
	@FXML
	public void initialize() {
		caricaAreaPersonale();
		aggiornaLabel();	
	}

	@FXML
    private void tornaAlLogin(MouseEvent event) { 
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) {  
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
    }
	
	@FXML
	private void modificaProfilo(ActionEvent event) {  // pulsante modifica
		if(!descrizioneTextField.isEditable()) {
			descrizioneTextField.setEditable(true);
		}
		else {
			descrizioneTextField.setEditable(false);  //quando ripremuto salva area personale
			salvaAreaPersonale();
		}
	}
	
	@FXML
	private void caricaViewRicetta(ActionEvent event) {  
		PubblicaRicettaController.creaRicetta(); //messo io
		ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA1);
	}
	
	public void aggiornaLabel() {  //aggiorna label con proprio username
		UtenteBean utenteBean = controller.getUtente();
		usernameLabel.setText(utenteBean.getUsername());
	}
	
	private void salvaAreaPersonale() {  //salva descrizione area personale
		controller.salvaAreaPersonale(descrizioneTextField.getText());
	}
	
	public void caricaAreaPersonale() { 
		UtenteBean utenteBean = controller.getUtente();
		Map<String,String> areaPersonaleMap=controller.caricaAreaPersonale();
		String descrizione="";
		if(!areaPersonaleMap.isEmpty()) {
			descrizione= areaPersonaleMap.get(utenteBean.getUsername());
		}
		descrizioneTextField.setText(descrizione);
	}
	
}