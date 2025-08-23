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
	
//	private static AreaPersonaleViewController istanza; //tolto singletom
	private AdattatoreFactory factory = AdattatoreFactory.ottieniIstanza();
	private LoginController controller = LoginController.ottieniIstanza(); //tolto singleton
	private ControllerAdapter adattatoreLoginController = factory.creaLoginAdapter();
//	private CaricaView caricaView = CaricaView.ottieniIstanza();
//	private Stage primaryStage;
	@FXML
    private ImageView tornaAlLoginImageView;
	@FXML
	private TextArea descrizioneTextField;
	@FXML
	private Label usernameLabel;
	
	
	/*
	private AreaPersonaleViewController() {	
	}
	
	public static synchronized AreaPersonaleViewController ottieniIstanza() { //SINGLETON	
		if(istanza == null) {
			istanza = new AreaPersonaleViewController();
		}
		return istanza;
	}
	*/
	
	@FXML
	public void initialize() {
		caricaAreaPersonale();
		aggiornaLabel();	
	}
/*
	public void setPrimaryStage(Stage primaryStage) {  //PASSO LO STAGE
		this.primaryStage= primaryStage;
	}
*/
	@FXML
    private void tornaAlLogin(MouseEvent event) { // carica view Login
        //caricaView.tornaAlLogin(primaryStage);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
    }
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) {  // carica view Gestisci ricette
        //caricaView.caricaViewGestisciRicette(primaryStage);
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
    }
	
	@FXML
	private void modificaProfilo(ActionEvent event) {  //PULSANTE MODIFICA
		if(!descrizioneTextField.isEditable()) {
			descrizioneTextField.setEditable(true);
		}
		else {
			descrizioneTextField.setEditable(false);  //QUANDO RIPREMUTO SALVA L'AREA PERSONALE
			salvaAreaPersonale();
		}
	}
	
	@FXML
	private void caricaViewRicetta(ActionEvent event) {  // carica view della nuova ricetta da creare 
		PubblicaRicettaController.creaRicetta(); //messo io
		//caricaView.caricaViewRicetta(primaryStage);
		ViewLoader.caricaView(ViewInfo.NUOVA_RICETTA1);
	}
	
	public void aggiornaLabel() {  //AGGIORNA LABEL CON IL PROPRIO USERNAME
		UtenteBean utenteBean = adattatoreLoginController.ottieniUtente();
		usernameLabel.setText(utenteBean.getUsername());
	}
	
	private void salvaAreaPersonale() {  //SALVA L'AREA PERSONALE(DESCRIZIONE)
		controller.salvaAreaPersonale(descrizioneTextField.getText());
	}
	
	public void caricaAreaPersonale() { //CARICA L'AREA PERSONALE(DESCRIZIONE)
		UtenteBean utenteBean=adattatoreLoginController.ottieniUtente();
		Map<String,String> areaPersonaleMap=controller.caricaAreaPersonale();
		String descrizione="";
		if(!areaPersonaleMap.isEmpty()) {
			descrizione= areaPersonaleMap.get(utenteBean.getUsername());
		}
		descrizioneTextField.setText(descrizione);
	}
	
}