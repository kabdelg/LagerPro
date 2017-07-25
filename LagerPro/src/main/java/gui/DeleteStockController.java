package gui;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DeleteStockController implements Initializable {

	@FXML
	private Button DS_confirm;
	@FXML
	private Button DS_cancel;
	@FXML
	private Text DS_text;
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DS_text.setText("Möchten sie den Lagereintrag " + MainWindowController.set_art_name + " auf Lagerplatz " + MainWindowController.set_stock_loc + " wirklich löschen ?");
	}
	
	@FXML
	protected void DS_confirmButtonPressed() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
					
			PreparedStatement ps = myConn.prepareStatement("DELETE from stock where Artikelnummer = ? AND Produktname = ? AND Anzahl = ? AND Charge = ? AND Gewicht = ? AND Lagerplatz = ? AND ZuPruefenAb = ?");
			ps.setString(1, MainWindowController.set_art_id);
			ps.setString(2, MainWindowController.set_art_name);
			ps.setInt(3, Integer.parseInt(MainWindowController.set_number));
			ps.setString(4, MainWindowController.set_charge);
			ps.setInt(5, Integer.parseInt(MainWindowController.set_weight));
			ps.setString(6, MainWindowController.set_stock_loc);
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy"); 
			java.util.Date parsedMW = df.parse(MainWindowController.set_exp_date);
			java.sql.Date dateFormatMW = new java.sql.Date(parsedMW.getTime());
			ps.setDate(7, dateFormatMW);
			ps.executeUpdate();
			
			Stage DeleteStage = (Stage) DS_confirm.getScene().getWindow();
			DeleteStage.getOnCloseRequest().handle(new WindowEvent(DeleteStage, WindowEvent.WINDOW_CLOSE_REQUEST));
			DeleteStage.close();
		} catch (Exception e) {
			log.error("Eintrag konnte nicht gelöscht werden");
		}
	}
	
	@FXML
	protected void DS_cancelButtonPressed() {
		try {
			Stage DeleteStage = (Stage) DS_cancel.getScene().getWindow();
			DeleteStage.getOnCloseRequest().handle(new WindowEvent(DeleteStage, WindowEvent.WINDOW_CLOSE_REQUEST));
			DeleteStage.close();
		} catch (Exception e) {
			log.error("Eintrag löschen konnte nicht abgebrochen werden");
		}
	}
	
}
