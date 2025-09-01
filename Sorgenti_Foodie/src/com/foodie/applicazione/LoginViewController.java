package com.foodie.applicazione;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.logging.Logger;
import com.foodie.boundary.DispensaUtenteViewController;
import com.foodie.boundary.ModeratoreViewController;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.boundary2.AggiungiAlimentoView2Controller;
import com.foodie.boundary2.ModeratoreView2Controller;
import com.foodie.controller.LoginController;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.model.Dispensa;

public class LoginViewController {
	
	private int interfaccia;
	
	@FXML
	private Button registratiButton;
	@FXML
	private Button loginButton;
	@FXML
	private Label loginMessageLabel;
	@FXML 
	private TextField usernameTextField;
	@FXML
	private PasswordField enterPasswordField;
	@FXML
	private Label chiudiLabel;
	@FXML
    private RadioButton interfaccia1RadioButton;
    @FXML
    private RadioButton interfaccia2RadioButton;
    @FXML
    private ToggleGroup userTypeToggleGroup;
    
    private LoginController loginController = new LoginController();
	
    /*
    private LoginViewController() {
    }
    
    public static synchronized LoginViewController ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza==null) {
			istanza=new LoginViewController();
		}
		return istanza;
	}
	MODIFICATO
	*/ 
    
	@FXML
    private void initialize() {  //METODO PER INIZIALIZZARE
        chiudiLabel.setOnMouseClicked(event -> closeApplication());
     
        // Creazione del Toggle Group
    	userTypeToggleGroup = new ToggleGroup();
        
    	// Associazione dei Radio Button al Toggle Group
    	interfaccia1RadioButton.setToggleGroup(userTypeToggleGroup);
    	interfaccia2RadioButton.setToggleGroup(userTypeToggleGroup);
        
        // Imposto primo bottone come quello selezionato di default
        userTypeToggleGroup.selectToggle(interfaccia1RadioButton);
        interfaccia = 1;
    }
	
    private void closeApplication() {  // chiusura applicazione
        Stage stage = (Stage)chiudiLabel.getScene().getWindow();
        stage.close();
    }
	
	@FXML
	public void loginButtonOnAction(ActionEvent event) {  //SE CLICCATO CHIAMO IL METODO
		
		if(!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
			validazioneLogin();
		} else {
			loginMessageLabel.setText("Inserisci username e password");
		}
		
	}
	
	@FXML
	public void registratiButtonOnAction(ActionEvent event) {  // operazione invocata alla pressione del tasto Registrati
		creaAccount();
	}
	
	public void validazioneLogin() {  //METODO PER FARE IL LOGIN E VIENE IMPOSTATO L'UTENTE CORRENTE
		String username = usernameTextField.getText();
		String password = enterPasswordField.getText();
		int tipo = loginController.effettuaLogin(username, password);  //CONTROLLO SE CORRETTO
		String ruolo = ""; //TIPO DI UTENTE
		if(tipo != -1) {
			if(tipo == 0) {
				ruolo = "Standard";		
			}
			else if(tipo == 1){
				ruolo = "Chef";
			}
			else {
				ruolo = "Moderatore";
			}
			loginController.setUtente(username.toLowerCase(), ruolo); //METTO USERNAME IN MINUSCOLE PER EVITARE INCONGRUENZE NEL CARICAMENTO DELLA DISPENSA O AREA PERSONALE
			
			if(interfaccia1RadioButton.isSelected()) { // verifico la GUI da attivare 
				interfaccia = 1;
			} else {
				interfaccia = 2;
			}
			/*
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(controller.ottieniView(interfaccia)));  // SFRUTTI POLIMORFISMO NEL CONTROLLER PER OTTENERE LA VIEW CORRETTA	            
	            caricaViewEController(loader,ruolo); //CARICA VIEW CORRETTA
	        }catch (IOException e) {
	            e.printStackTrace();
	        }
	        */
			String viewIniziale = loginController.ottieniView(interfaccia);
			if(tipo == 0) {
				//Dispensa dispensa = Dispensa.ottieniIstanza();
            	//dispensa.svuotaDispensa();
            //	loginController.caricaDispense();
			}
			ViewLoader.caricaView(ViewInfo.fromFxmlPath(viewIniziale));
		}
		
		else {  	//SE TIPO ==-1 ERRORE NEL LOGIN
			loginMessageLabel.setText("credenziali errate. Se non hai ancora un account, registrati.");
		}
	}
	/*
	private void caricaViewEController(FXMLLoader loader,String ruolo) throws IOException{ //METODO PER CARICARE LA VIEW CORRETTA  PER EVITARE COMPLESSITA' SMELL       
        Parent root = null;
		switch (ruolo) { 
            case "Standard":  //SE STANDARD CARICHI LE INTERFACCE RELATIVE PER L'UTENTE STANDARD
            	Dispensa dispensa = Dispensa.ottieniIstanza();
            	dispensa.svuotaDispensa();
                if (interfaccia == 1) { //risolto
                	DispensaUtenteViewController controllerDispensa = DispensaUtenteViewController.ottieniIstanza();
                    loader.setController(controllerDispensa);
                    controller.registraOsservatore(controllerDispensa, 1);
                    root = loader.load();
                    controllerDispensa.setPrimaryStage(primaryStage);
                } else {
                    AggiungiAlimentoView2Controller controllerAlimenti = AggiungiAlimentoView2Controller.ottieniIstanza();
                    loader.setController(controllerAlimenti);
                    root = loader.load();
                    controllerAlimenti.setPrimaryStage(primaryStage);
                }
                loginController.caricaDispense();
                break;
            case "Chef": //SE CHEF CARICHI LE INTERFACCE RELATIVE PER L'UTENTE CHEF
                if (interfaccia == 1) { //risolto
                	AreaPersonaleViewController controllerAreaPersonale = AreaPersonaleViewController.ottieniIstanza();
                    loader.setController(controllerAreaPersonale);
                    root = loader.load();
                    controllerAreaPersonale.setPrimaryStage(primaryStage);
                    controllerAreaPersonale.caricaAreaPersonale();
                    controllerAreaPersonale.aggiornaView();
                } else { //risolto
                    AreaPersonaleView2Controller areaPersonaleController = AreaPersonaleView2Controller.ottieniIstanza();
                    loader.setController(areaPersonaleController);
                    root = loader.load();
                    areaPersonaleController.setPrimaryStage(primaryStage);
                    areaPersonaleController.caricaAreaPersonale();
                    areaPersonaleController.aggiornaView();
                }
                break;
            case "Moderatore": //SE MODERATORE CARICHI LE INTERFACCE RELATIVE PER L'UTENTE MODERATORE    
            	if (interfaccia == 1) { //risolto
                	ModeratoreViewController controllerModeratore = ModeratoreViewController.ottieniIstanza();
                    loader.setController(controllerModeratore);
                    controller.registraOsservatore(controllerModeratore,3);
                    root = loader.load();
                    controllerModeratore.setPrimaryStage(primaryStage);
                    controllerModeratore.aggiornaView();
                } else { 
                	ModeratoreView2Controller controllerModeratore2= ModeratoreView2Controller.ottieniIstanza();
                    loader.setController(controllerModeratore2);
                    controller.registraOsservatore(controllerModeratore2,3);
                    root = loader.load();
                    controllerModeratore2.setPrimaryStage(primaryStage);
                    controllerModeratore2.aggiornaView();
                }
                break;
            default:
            	String errore="Ruolo non riconosciuto: " + ruolo;
            	logger.severe(errore);
                throw new IllegalArgumentException("Ruolo non riconosciuto: " + ruolo);
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	*/
	
	private void creaAccount() {  //CARICA LA VIEW PER LA PAGINA DI REGISTRAZIONE
	/*
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistratiView.fxml"));
			RegistratiViewController controllerRegistrazione = new RegistratiViewController();
			loader.setController(controllerRegistrazione);
			Parent root = loader.load();
			controllerRegistrazione.setPrimaryStage(primaryStage);
            Scene nuovaScena = new Scene(root);
            primaryStage.setScene(nuovaScena);
            primaryStage.show();
		
		} catch (Exception e) {
			e.printStackTrace();
			e.getCause();
		}
		*/
		ViewLoader.caricaView(ViewInfo.REGISTRAZIONE_VIEW);
	}
}