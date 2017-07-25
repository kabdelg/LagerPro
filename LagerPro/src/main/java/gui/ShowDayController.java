package gui;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ShowDayController {

	@FXML
	private Button SD_show;
	@FXML
	private Button SD_back;
	@FXML
	private TextField SD_date;
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	
	
	@FXML
	protected void SD_showButtonPressed() {
		try {
			
			//checks, dass Eingabe ein Datum ist
			
			//open .txt-File
			String date = SD_date.getText();
			String fileName = "Lagerbestand-" + date;
			System.out.println(fileName);
			
			FileChooser fc = new FileChooser();
			fc.setTitle("Tag auswählen");
			
			try {
				fc.setInitialDirectory(new File("/Users/andy/Desktop/Exports/"));
			} catch (Exception e){
				log.error("InitialDirectory konnte nicht gefunden werden");
			}
			fc.getExtensionFilters().addAll(
					new ExtensionFilter("Text Files", "*.txt"));
			
			File selectedFile = fc.showOpenDialog(null);
			
			if (selectedFile != null) {
//				selectedFile.show();
			} else {
				System.out.println("Selected file is not valid");
			}
			
			
		} catch (Exception e) {
			log.error("Lagerbestand kann nicht angezeigt werden");
			
		}
	}
	
	@FXML
	protected void SD_backButtonPressed() {
		try {
			Stage ShowDayStage = (Stage) SD_back.getScene().getWindow();
			ShowDayStage.close();
		} catch (Exception e) {
			log.error("Zurück-Funktion fehlgeschlagen");
		}
	}
	
}
