package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBaseDataController {

	@FXML
	private Button ABD_confirm;
	@FXML
	private Button ABD_cancel;
	@FXML
	private TextField ABD_art_id;
	@FXML
	private TextField ABD_art_name;
	@FXML
	private TextField ABD_weight;
	@FXML
	private TextField ABD_exp_time;
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	@FXML
	protected void ABD_confirmButtonPressed() {
		try {
			
			//input checks
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
			
			PreparedStatement ps = myConn.prepareStatement("INSERT INTO baseData(Artikelnummer, Produktname, Gewicht, Einlagerungszeit) "
					+ "VALUES (?, ?, ?, ?);");
			
			ps.setString(1, ABD_art_id.getText());
			ps.setString(2, ABD_art_name.getText());
			ps.setInt(3, Integer.parseInt(ABD_weight.getText()));
			ps.setInt(4, Integer.parseInt(ABD_exp_time.getText()));
			
			ps.executeUpdate();
			
			Stage ABDStage = (Stage) ABD_confirm.getScene().getWindow();
			ABDStage.close();
			
		} catch (Exception e) {
			log.error("Neue Stammdaten konnten nicht hinzugefügt werden");
			
		}
	}
	
	@FXML
	protected void ABD_cancelButtonPressed() {
		try {
			Stage ABDStage = (Stage) ABD_cancel.getScene().getWindow();
			ABDStage.close();
		} catch (Exception e) {
			log.error("Neue Stammdaten hinzufügen konnte nicht abgebrochen werden");
		}
	}
	
}
