package com.foodie.boundary;

import java.util.List;
import java.util.logging.Logger;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.AdattatoreFactory;
import com.foodie.controller.ControllerAdapter;
import com.foodie.controller.TrovaRicettaController;
import com.foodie.model.RicettaBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TrovaRicetteViewController {
	
//	private static TrovaRicetteViewController istanza;
	private AdattatoreFactory factory = AdattatoreFactory.ottieniIstanza();
	private TrovaRicettaController trovaRicettaController = new TrovaRicettaController();
//	private ControllerAdapter adattatoreTrovaRicettaController= factory.creaTrovaRicettaAdapter();
//	private CaricaView caricaView = CaricaView.ottieniIstanza();
	private static final String FORMATO = "Arial";
	private static final Logger logger = Logger.getLogger(TrovaRicetteViewController.class.getName());
	@FXML
	private VBox contenitoreRicette;
	@FXML
	private MenuItem facile;
	@FXML
	private MenuItem media;
	@FXML
	private MenuItem difficile;
	
	/*
	private TrovaRicetteViewController() {	
	}
	
	public static synchronized TrovaRicetteViewController ottieniIstanza() { //SINGLETON	
		if(istanza == null) {
			istanza = new TrovaRicetteViewController();
		}
		return istanza;
	}
	*/
	
	@FXML
	public void initialize() {
		trovaRicette();
	}
	
	public VBox getContenitoreRicette() {  //RESTITUISCO IL VBOX
		return contenitoreRicette;
	}
	
	@FXML
	private void tornaAlLogin(MouseEvent event) {  
		trovaRicettaController.svuotaDispensa();
		//caricaView.tornaAlLogin(primaryStage);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}
	
	@FXML
	private void caricaViewDispensa(ActionEvent event) { 
		//caricaView.caricaViewDispensa(primaryStage);
		ViewLoader.caricaView(ViewInfo.DISPENSA_UTENTE);
	}
	
	@FXML
	private void filtraRicette(ActionEvent event) {  //SE VOGLIO UNA DIFFICOLTA' SPECIFICA RIFACCIO LA RICERCA CON QUELLA DIFFICOLTA'
		contenitoreRicette.getChildren().clear();
		MenuItem item= (MenuItem) event.getSource();
		String difficolta=item.getText();
		switch(difficolta){
			case "Facile":	
					trovaRicette(1);
					break;
			case "Media":	
					trovaRicette(2);
					break;
			case "Difficile":	
					trovaRicette(3);
					break;
			default:
				logger.severe("difficoltà non riconosciuta");
				contenitoreRicette.getChildren().clear();
		}
	}
	
	//metodo spostato da DispensaUtenteViewController
	private void trovaRicette() {  //AGGIORNO DINAMICAMENTE L'FXML TROVANDO SUBITO LE RICETTE
		int count=0;
		VBox contenitoreRicette = getContenitoreRicette();
		for(int i=1;i<4;i++) {  //TROVO TUTTE LE DIFFICOLTA'
			if(creaRicetteTrovate(i)==-1) {
				count++;
			}
		}
		if(count==3) {  //SE NON TROVO 3 VOLTE RISULTATI
			Label label=new Label("NESSUN RISULTATO");
			label.setStyle("-fx-background-color: rgba(217, 217, 217, 0.75);-fx-border-color: black;");
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(110);
			label.setWrapText(true);
			label.setFont(Font.font(FORMATO,50));
			label.setAlignment(Pos.CENTER);
			contenitoreRicette.getChildren().add(label);
		}
	}
	
	private void trovaRicette(int difficoltaInt) { //CHIAMA METODO PER TROVARE LE RICETTE E CREARE LE VARIE PARTI GRAFICHE, filtrando risultato
		if(creaRicetteTrovate(difficoltaInt)==-1) {  //SE NESSUN RISULTATO
				Label label=new Label("NESSUN RISULTATO");
				label.setStyle("-fx-background-color: rgba(217, 217, 217, 0.75);-fx-border-color: black;");
				label.setMaxWidth(Double.MAX_VALUE);
				label.setMinHeight(110);
				label.setWrapText(true);
				label.setFont(Font.font(FORMATO,50));
				label.setAlignment(Pos.CENTER);
				contenitoreRicette.getChildren().add(label);
		}
	}
	
	public int creaRicetteTrovate(int difficoltaInt) {  //TROVA LE RICETTE E LE MOSTRA GRAFICAMENTE
		List<RicettaBean> ricetteTrovate= null;
		ricetteTrovate = trovaRicettaController.trovaRicetteUtente(difficoltaInt);
		if(!ricetteTrovate.isEmpty()) {
			for(RicettaBean r: ricetteTrovate) {  //PER OGNI RICETTA CREA PARTI GRAFICHE
				HBox contenitoreRicettaSingola = new HBox();
				contenitoreRicettaSingola.setAlignment(Pos.CENTER_LEFT);
				contenitoreRicettaSingola.setStyle("-fx-background-color: rgba(217, 217, 217, 0.75);-fx-border-color: black;");
				contenitoreRicettaSingola.setMinHeight(110);
				contenitoreRicettaSingola.setMaxWidth(Double.MAX_VALUE);
				Label labelNome = new Label(r.getNome());
				labelNome.setMinWidth(313);
				labelNome.setMinHeight(65);
				labelNome.setFont(Font.font(FORMATO,20));
				labelNome.setAlignment(Pos.CENTER);
				Label labelAutore = new Label(r.getAutore());
				labelAutore.setMinWidth(313);
				labelAutore.setMinHeight(65);
				labelAutore.setFont(Font.font(FORMATO,20));
				labelAutore.setAlignment(Pos.CENTER);
				String difficolta="";
				switch(difficoltaInt) {
				case 1:
						difficolta="facile";
						break;
				case 2:
						difficolta="intermedio";
						break;
				case 3:
						difficolta="difficile";
						break;
				default:
					logger.severe("difficoltà non riconosciuta");
					contenitoreRicette.getChildren().clear();
					return -1;
				}
				Label labelDifficolta = new Label(difficolta);
				labelDifficolta.setMinWidth(313);
				labelDifficolta.setMinHeight(65);
				labelDifficolta.setFont(Font.font(FORMATO,20));
				labelDifficolta.setAlignment(Pos.CENTER);
				contenitoreRicettaSingola.getChildren().addAll(labelNome,labelAutore,labelDifficolta);
				contenitoreRicette.getChildren().add(contenitoreRicettaSingola);
			}
			impostaHBox();  //RENDO RICETTE CLICCABILI
			return 1;  //SE TROVATA ALMENO 1
		}
		else {
			return -1; //SE NON TROVATE
		}
	}
	
	private void impostaHBox() {  //METODO CHE IMPOSTA CLICCABILI LE RICETTE TROVATE
		contenitoreRicette.getChildren().forEach(node -> {  //E' LA PARTE DI SETTAGGIO DELLE LABEL CLICCABILI
	        HBox contenitoreRicetta = (HBox) node;
	        contenitoreRicetta.setOnMouseClicked(event2 -> {
	            String nomeRicetta="";
	            String autoreRicetta="";
	            int indiceLabel=1;
	            for (Node labelNode : contenitoreRicetta.getChildren()) {	        
	                    Label label = (Label) labelNode;
	                    if(indiceLabel==1)
	                    	nomeRicetta=label.getText();
	                    else if(indiceLabel==2)
	                    	autoreRicetta=label.getText();
	                    else {
	                    	break;
	                    }
	                    indiceLabel++;
	            }
	            RicettaBean ricettaBean = new RicettaBean();
	            ricettaBean.setNome(nomeRicetta);
	            ricettaBean.setAutore(autoreRicetta);
	            caricaViewRicetta(ricettaBean);  //SE CLICCATA LA APRE
	        });
	    });
	}
	
	/*
	public void caricaViewRicetta(String nomeRicetta,String autoreRicetta) {  //CARICA VIEW CONTENUTO RICETTA
		RicettaBean ricettaSelezionata = adattatoreTrovaRicettaController.apriLaRicetta(nomeRicetta,autoreRicetta);
		FXMLLoader loader;
		try {
			loader = new FXMLLoader(getClass().getResource("ContenutoRicettaView.fxml"));
	        ContenutoRicettaViewController contenutoRicettaViewController = ContenutoRicettaViewController.ottieniIstanza();
	        loader.setController(contenutoRicettaViewController);
	        Parent root = loader.load();
	        caricaDatiRicetta(ricettaSelezionata,contenutoRicettaViewController); //METODO PER POPOLARE GRAFICAMENTE LA VIEW CON I DATI DELLA RICETTA
	        contenutoRicettaViewController.setPrimaryStage(primaryStage);
	        Scene nuovaScena = new Scene(root);
	        primaryStage.setScene(nuovaScena);
	        primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	private void caricaViewRicetta(RicettaBean ricettaBean) {  //CARICA VIEW CONTENUTO RICETTA
		RicettaBean ricettaSelezionata = trovaRicettaController.ottieniRicetta(ricettaBean);
		FXMLLoader loader =  ViewLoader.caricaView(ViewInfo.CONTENUTO_RIC_UT);
		ContenutoRicettaViewController contenutoRicettaViewController = loader.getController();
		contenutoRicettaViewController.initData(ricettaSelezionata);
	}
	

	
	/* //spostata in ContenutoRicettaViewController 
	private void caricaDatiRicetta(RicettaBean ricettaBean, ContenutoRicettaViewController contenutoRicettaViewController) {  //POPOLA GRAFICAMENTE IL CONTENUTO DELLA RICETTA NEL FXML
		Label labelNome=contenutoRicettaViewController.getNome();
		Label labelDescrizione = contenutoRicettaViewController.getDescrizione();
		VBox contenitoreIngredienti= contenutoRicettaViewController.getContenitoreIngredienti();
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
	*/
	
}