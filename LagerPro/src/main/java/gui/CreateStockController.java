package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import controller.BaseDetails;
import controller.InputChecks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CreateStockController {

	@FXML
	private Button CS_create;
	@FXML
	private Button CS_cancel;
	@FXML
	private TextField CS_art_id;
	@FXML
	private TextField CS_art_name;
	@FXML
	private TextField CS_number;
	@FXML
	private TextField CS_charge;
	@FXML
	private TextField CS_weight;
	@FXML
	private TextField CS_stock_loc;
	@FXML
	public TextField CS_exp_date;
	@FXML
	private Label CS_info_label;
	
	String createStockSQL;
	
	private ObservableList <BaseDetails> baseData;
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	
	
	@FXML
	protected void CS_art_id_onEnter() {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
			Statement myStmt = myConn.createStatement();
			
			baseData = FXCollections.observableArrayList();
			Date date = new Date();

			ResultSet rs = myStmt.executeQuery("select * from baseData");
			while (rs.next()) {
				baseData.add(new BaseDetails(
						rs.getString("Artikelnummer"), 
						rs.getString("Produktname"), 
						rs.getInt("Gewicht"), 
						rs.getInt("Einlagerungszeit")));
				
				if (CS_art_id.getText().equals(String.valueOf(rs.getInt("Artikelnummer")))) {

					System.out.println("bin drin");
					try {
						CS_art_name.setText(rs.getString("Produktname"));
						CS_weight.setText(String.valueOf(rs.getInt("Gewicht")));
						
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
						LocalDate localDate = LocalDate.now();
						String localDateFormat = dtf.format(localDate.plusMonths(rs.getInt("Einlagerungszeit")));
						CS_exp_date.setText(localDateFormat);
					} catch (Exception e) {
						log.error("fail", e);
					}
					CS_info_label.setText("");
					return;
				} else {
					CS_info_label.setText("Ungültige Artikelnummer");
					CS_art_name.setText("");
					CS_weight.setText("");
					CS_exp_date.setText("");
				}
			
			}
			
			
//			String test = baseData.get(0).getArtikelnummer().toString();
//			
//			test = test.substring(test.length()-7, test.length()-1);
//			
//			System.out.println(baseData.get(0).getArtikelnummer().toString());
//			System.out.println(test);
//			
//			for (int i=0;i<baseData.size();i++) {
//				if (CS_art_id.getText().equals(test/*baseData.get(i).getArtikelnummer().toString()*/)) {
////				if (Integer.parseInt(CS_art_id.))
//					CS_art_name.setText(baseData.get(i).getProduktname().toString());
//					CS_weight.setText(baseData.get(i).getGewicht().toString());
//					//exp_time zwischenspeichern für Vorschlag exp_date
//				}
//			}
			
		} catch (Exception e) {
			log.error("Autocomplete fehlgeschlagen, evtl. Artikelnummer ungültig", e);
			
		}
		
	}
	
	@FXML
	protected void CS_createButtonPressed() {
		try {
			if (!InputChecks.checkArtId(CS_art_id.getText())){
				CS_info_label.setText("Ungültige Artikelnummer");
			}
			else if (!InputChecks.checkNumber(CS_number.getText())){
				CS_info_label.setText("Ungültige Anzahl");
			}
			else if (!InputChecks.checkCharge(CS_charge.getText())){
				CS_info_label.setText("Ungültige Charge");
			}
			else if (!InputChecks.checkWeight(CS_weight.getText())){
				CS_info_label.setText("Ungültiges Gewicht");
			}
			else if (!InputChecks.checkStockLoc(CS_stock_loc.getText())){
				CS_info_label.setText("Ungültiger Lagerplatz");
			}else{
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
			
			PreparedStatement ps = myConn.prepareStatement("INSERT INTO stock (Artikelnummer, Produktname, Anzahl, Charge, Gewicht, Lagerplatz, ZuPruefenAb) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);");
			ps.setString(1, CS_art_id.getText());
			ps.setString(2, CS_art_name.getText());
			ps.setInt(3, Integer.parseInt(CS_number.getText()));
			ps.setString(4, CS_charge.getText());
			ps.setInt(5, Integer.parseInt(CS_weight.getText()));
			ps.setString(6, CS_stock_loc.getText());
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy"); 
			Date parsed = df.parse(CS_exp_date.getText());
			java.sql.Date dateFormat = new java.sql.Date(parsed.getTime());
			ps.setDate(7, (java.sql.Date) dateFormat);
			
			ps.executeUpdate();
			
			Stage CreateStage = (Stage) CS_create.getScene().getWindow();
			CreateStage.getOnCloseRequest().handle(new WindowEvent(CreateStage, WindowEvent.WINDOW_CLOSE_REQUEST));
			CreateStage.close();
			
			
			}
		} catch (Exception e) {
			log.error("Eintrag konnte nicht erstellt werden");
			System.err.println("Error: " + e);
		}
	}
	
	@FXML
	protected void CS_cancelButtonPressed() {
		try {
			Stage CreateStage = (Stage) CS_cancel.getScene().getWindow();
			CreateStage.getOnCloseRequest().handle(new WindowEvent(CreateStage, WindowEvent.WINDOW_CLOSE_REQUEST));
			CreateStage.close();
		} catch (Exception e) {
			log.error("Eintrag erstellen konnte nicht abgebrochen werden");
		}
	}

	
}
