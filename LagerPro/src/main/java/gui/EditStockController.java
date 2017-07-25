package gui;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.BaseDetails;
import controller.GetStock;
import controller.InputChecks;
import controller.StockDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EditStockController implements Initializable{

	@FXML
	private Button ES_confirm;
	@FXML
	private Button ES_cancel;
	@FXML
	private TextField ES_art_id;
	@FXML
	private TextField ES_art_name;
	@FXML
	private TextField ES_number;
	@FXML
	private TextField ES_charge;
	@FXML
	private TextField ES_weight;
	@FXML
	private TextField ES_stock_loc;
	@FXML
	private TextField ES_exp_date;
	@FXML
	private Label ES_info_label;
	@FXML
	private TableView<StockDetails> MW_TableView;
	
	private ObservableList <BaseDetails> baseData;
	
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ES_art_id.setText(MainWindowController.set_art_id);
		ES_art_name.setText(MainWindowController.set_art_name);
		ES_number.setText(MainWindowController.set_number);
		ES_charge.setText(MainWindowController.set_charge);
		ES_weight.setText(MainWindowController.set_weight);
		ES_stock_loc.setText(MainWindowController.set_stock_loc);
		ES_exp_date.setText(MainWindowController.set_exp_date);
//		GetStock.loadEdit();
		
	}
	
	@FXML
	protected void ES_art_id_onEnter() {
		
		
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
				
				if (ES_art_id.getText().equals(String.valueOf(rs.getInt("Artikelnummer")))) {

					
					try {
						
						ES_art_name.setText(rs.getString("Produktname"));
						ES_weight.setText(String.valueOf(rs.getInt("Gewicht")));
						
					} catch (Exception e) {
						log.error("fail", e);
					}
					ES_info_label.setText("");
					return;
				} else {
					ES_info_label.setText("Ungültige Artikelnummer");
					ES_art_name.setText("");
					ES_weight.setText("");
					ES_exp_date.setText("");
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
	protected void ES_confirmButtonPressed() {
		try {
			if (!InputChecks.checkArtId(ES_art_id.getText())){
				ES_info_label.setText("Ungültige Artikelnummer");
			}
			else if (!InputChecks.checkNumber(ES_number.getText())){
				ES_info_label.setText("Ungültige Anzahl");
			}
			else if (!InputChecks.checkCharge(ES_charge.getText())){
				ES_info_label.setText("Ungültige Charge");
			}
			else if (!InputChecks.checkWeight(ES_weight.getText())){
				ES_info_label.setText("Ungültiges Gewicht");
			}
			else if (!InputChecks.checkStockLoc(ES_stock_loc.getText())){
				ES_info_label.setText("Ungültiger Lagerplatz");
			}else{
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
			//Statement myStmt = myConn.createStatement();
			
			
			
			
			PreparedStatement ps = myConn.prepareStatement("UPDATE `stock` "
					+ "SET `Artikelnummer`=?,`Produktname`=?,`Anzahl`=?,"
					+ "`Charge`=?,`Gewicht`=?,`Lagerplatz`=?,`ZuPruefenAb`=? "
					+ "WHERE `Artikelnummer`=? AND `Produktname`=? AND `Anzahl`=? AND "
					+ "`Charge`=? AND `Gewicht`=? AND `Lagerplatz`=? AND `ZuPruefenAb`=?");
			
			ps.setString(1, ES_art_id.getText());
			ps.setString(2, ES_art_name.getText());
			ps.setInt(3, Integer.parseInt(ES_number.getText()));
			ps.setString(4, ES_charge.getText());
			ps.setInt(5, Integer.parseInt(ES_weight.getText()));
			ps.setString(6, ES_stock_loc.getText());
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy"); 
			Date parsed = df.parse(ES_exp_date.getText());
			Date parsedMW = df.parse(MainWindowController.set_exp_date);
			java.sql.Date dateFormat = new java.sql.Date(parsed.getTime());
			java.sql.Date dateFormatMW = new java.sql.Date(parsedMW.getTime());
			ps.setDate(7, (java.sql.Date) dateFormat);
			
			ps.setString(8, MainWindowController.set_art_id);
			ps.setString(9, MainWindowController.set_art_name);
			ps.setInt(10, Integer.parseInt(MainWindowController.set_number));
			ps.setString(11, MainWindowController.set_charge);
			ps.setInt(12, Integer.parseInt(MainWindowController.set_weight));
			ps.setString(13, MainWindowController.set_stock_loc);
			ps.setDate(14, dateFormatMW);
			
			//ps.setDate(14, java.sql.Date.valueOf(MainWindowController.set_exp_date));

			
			
			ps.executeUpdate();
						
			
			Stage EditStage = (Stage) ES_confirm.getScene().getWindow();
			EditStage.getOnCloseRequest().handle(new WindowEvent(EditStage, WindowEvent.WINDOW_CLOSE_REQUEST));
			EditStage.close();
			}
			
		} catch (Exception e) {
			System.err.println(e);
			log.error("Änderungen konnten nicht bestätigt werden");
			
		}
	}
	
	@FXML
	protected void ES_cancelButtonPressed() {
		try {
			Stage EditStage = (Stage) ES_cancel.getScene().getWindow();
			EditStage.getOnCloseRequest().handle(new WindowEvent(EditStage, WindowEvent.WINDOW_CLOSE_REQUEST));
			EditStage.close();
		} catch (Exception e) {
			log.error("Änderungen konnten nicht abgebrochen werden");
		}
	}


	
	
}
