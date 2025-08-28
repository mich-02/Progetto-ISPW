package com.foodie.applicazione;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class ConfigurazioneViewController {
	@FXML
	private RadioButton demoModeRadioButton;
	@FXML
	private RadioButton memoryModeRadioButton;
	
	@FXML
	private void initialize() {
		ToggleGroup modeToggleGroup = new ToggleGroup();
		demoModeRadioButton.setToggleGroup(modeToggleGroup);
		memoryModeRadioButton.setToggleGroup(modeToggleGroup);
		demoModeRadioButton.setSelected(true);
	}
	
	@FXML
	private void handleConfermaButtonAction(ActionEvent event) {
		Persistenza persistenza = demoModeRadioButton.isSelected() ? Persistenza.MEMORIA
				: Persistenza.DATABASE;
		ConfiguratorePersistenza.configuraPersistenza(persistenza);
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}
}
