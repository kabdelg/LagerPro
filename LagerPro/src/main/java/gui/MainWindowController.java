package gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.SyslogAppender;

import controller.Database;
import controller.ExportFile;
import controller.ExportFileTest;
import controller.GetStock;
import controller.StockDetails;

public class MainWindowController implements Initializable {

	@FXML
	private Button MW_create_stock;
	@FXML
	private Button MW_edit_stock;
	@FXML
	private Button MW_delete_stock;
	@FXML
	private Button MW_expired_products;
	@FXML
	private Button MW_show_day;
	@FXML
	private Button MW_abd;
	@FXML
	private TextField MW_search;
	@FXML
	private Label MW_info_label;
	
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
	private TableView<StockDetails> MW_TableView;
	@FXML
	private TableColumn<StockDetails, String> MW_ArtikelIDCol;
	@FXML
	private TableColumn<StockDetails, String> MW_ProduktNameCol;
	@FXML
	private TableColumn<StockDetails, Number> MW_NumberCol;
	@FXML
	private TableColumn<StockDetails, String> MW_ChargeCol;
	@FXML
	private TableColumn<StockDetails, Number> MW_GewichtCol;
	@FXML
	private TableColumn<StockDetails, String> MW_LagerplatzCol;
	@FXML
	private TableColumn<StockDetails, String> MW_ZuPruefenAbCol;
	
	
	private ObservableList <StockDetails> data;
	
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	public static MainWindowController mw;
	
	static String set_art_id;
	static String set_art_name;
	static String set_number;
	static String set_charge;
	static String set_weight;
	static String set_stock_loc;
	static String set_exp_date;
	
	@Override
	public void initialize(URL url, ResourceBundle rb){
		loadData();
		searchStock();
		ExportFileTest.startExport();
    }
	
	@FXML
	public void searchStock() {
		MW_ProduktNameCol.setCellValueFactory(cellData -> cellData.getValue().ProduktnameProperty());
		MW_ArtikelIDCol.setCellValueFactory(cellData -> cellData.getValue().ArtikelnummerProperty());
		MW_ChargeCol.setCellValueFactory(cellData -> cellData.getValue().ChargeProperty());
		MW_LagerplatzCol.setCellValueFactory(cellData -> cellData.getValue().LagerplatzProperty());
		
        FilteredList<StockDetails> filteredData = new FilteredList<>(data, p -> true);
        MW_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(StockDetails -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                
                String ArtikelIDString = StockDetails.getArtikelnummer().toString();
                String ChargeString = StockDetails.getCharge().toString();
                
         
                
                
                if (StockDetails.getProduktname().get().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (ArtikelIDString.contains(lowerCaseFilter)) {
                    return true;
                } else if (ChargeString.contains(lowerCaseFilter)) {
                    return true;
                } else if (StockDetails.getLagerplatz().get().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<StockDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(MW_TableView.comparatorProperty());
        MW_TableView.setItems(sortedData);
	}
			

	@FXML
	public void loadData (){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
			
			data = FXCollections.observableArrayList();
			Statement myStmt = myConn.createStatement();
			ResultSet rs = myStmt.executeQuery("SELECT *, DATE_FORMAT(ZuPruefenAb, \"%d.%m.%Y\") AS Date FROM stock;");
			
			while (rs.next()){
//				System.out.println(rs.getInt("Artikelnummer"));
				data.add(new StockDetails(
						rs.getString("Artikelnummer"),
						rs.getString("Produktname"),
						rs.getInt("Anzahl"),
						rs.getString("Charge"),
						rs.getInt("Gewicht"),
						rs.getString("Lagerplatz"),
						rs.getString("Date")));
			}
//			System.out.println(rs.getInt("Artikelnummer"));
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		
		

	
		MW_ArtikelIDCol.setCellValueFactory(new PropertyValueFactory<>("Artikelnummer"));
		MW_ProduktNameCol.setCellValueFactory(new PropertyValueFactory<>("Produktname"));
		MW_NumberCol.setCellValueFactory(new PropertyValueFactory<>("Anzahl"));
		MW_ChargeCol.setCellValueFactory(new PropertyValueFactory<>("Charge"));
		MW_GewichtCol.setCellValueFactory(new PropertyValueFactory<>("Gewicht"));
		MW_LagerplatzCol.setCellValueFactory(new PropertyValueFactory<>("Lagerplatz"));
		MW_ZuPruefenAbCol.setCellValueFactory(new PropertyValueFactory<>("ZuPruefenAb"));
		MW_TableView.setId("StockTable");
	
		MW_TableView.setItems(data);
		
	}

	@FXML
	protected void MW_createButtonPressed() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CreateStock.fxml"));
			
			Parent main = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Lagereintrag erstellen");
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					loadData();
					searchStock();
				}
			});
			stage.setScene(new Scene(main));
			stage.show();
			//MW_info_label.setText("");
			
			
		} catch (Exception e) {
			log.error("CreateStock.fxml nicht gefunden");
			MW_info_label.setText("Es konnte kein neuer Lagereintrag erstellt werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	@FXML
	protected void MW_editButtonPressed() {
		try {
			if (getSelectedStock() == null){
				MW_info_label.setText("Kein Lagereintrag ausgewählt !");
			} else {
				
				TablePosition pos = MW_TableView.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				StockDetails item = MW_TableView.getItems().get(row);
				set_art_id = (String) MW_ArtikelIDCol.getCellObservableValue(item).getValue().toString();
				set_art_name = (String) MW_ProduktNameCol.getCellObservableValue(item).getValue().toString();
				set_number = (String) MW_NumberCol.getCellObservableValue(item).getValue().toString();
				set_charge = (String) MW_ChargeCol.getCellObservableValue(item).getValue().toString();
				set_weight = (String) MW_GewichtCol.getCellObservableValue(item).getValue().toString();
				set_stock_loc = (String) MW_LagerplatzCol.getCellObservableValue(item).getValue().toString();
				set_exp_date = (String) MW_ZuPruefenAbCol.getCellObservableValue(item).getValue().toString();

//				GetStock.setEdit(item);
//				int row = pos.getRow();
//				StockDetails item = MW_TableView.getItems().get(row);
//				
				//ES_art_id.setText(art_id);
//				System.out.println(art_id);
				
				
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EditStock.fxml"));
				Parent main = (Parent) fxmlLoader.load();
				//EditStockController editContr = fxmlLoader.getController();
				//editContr.setArtId(art_id);
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				
				stage.setResizable(false);
				stage.setTitle("Lagereintrag ändern");
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent we) {
						loadData();
						searchStock();
					}
				});
				stage.setScene(new Scene(main));
				
				stage.show();
				//ES_art_id.setText(art_id);
//				MW_info_label.setText("");
				
			
			}
		} catch (Exception e) {
			log.error("EditStock.fxml nicht gefunden", e);
			MW_info_label.setText("Der Lagereintrag konnte nicht geändert werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	@FXML
	protected void MW_deleteButtonPressed() {
		try {
			if (getSelectedStock() == null){
				MW_info_label.setText("Kein Lagereintrag ausgewählt !");
			} else {
				
			TablePosition pos = MW_TableView.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			StockDetails item = MW_TableView.getItems().get(row);
			set_art_id = (String) MW_ArtikelIDCol.getCellObservableValue(item).getValue().toString();
			set_art_name = (String) MW_ProduktNameCol.getCellObservableValue(item).getValue().toString();
			set_number = (String) MW_NumberCol.getCellObservableValue(item).getValue().toString();
			set_charge = (String) MW_ChargeCol.getCellObservableValue(item).getValue().toString();
			set_weight = (String) MW_GewichtCol.getCellObservableValue(item).getValue().toString();
			set_stock_loc = (String) MW_LagerplatzCol.getCellObservableValue(item).getValue().toString();
			set_exp_date = (String) MW_ZuPruefenAbCol.getCellObservableValue(item).getValue().toString();			
				
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DeleteStock.fxml"));
			Parent main = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Lagereintrag löschen");
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					loadData();
					searchStock();
				}
			});
			stage.setScene(new Scene(main));
			stage.show();
			MW_info_label.setText("");
			}
			
		} catch (Exception e) {
			log.error("DeleteStock.fxml nicht gefunden");
			System.err.println(e);
			MW_info_label.setText("Der Lagereintrag konnte nicht gelöscht werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	@FXML
	protected void MW_expiredButtonPressed() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ExpiredProducts.fxml"));
			
			Parent main = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Überlagerte Ware");
			stage.setScene(new Scene(main));
			stage.show();
			//MW_info_label.setText("");
			
			
		} catch (Exception e) {
			System.err.println(e);
			log.error("ExpiredProducts.fxml nicht gefunden");
			//MW_info_label.setText("Es konnten keine neuen Stammdaten erstellt werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	@FXML
	protected void MW_showButtonPressed() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ShowDayTable.fxml"));
			
			Parent main = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			//stage.initOwner((Stage)MW_show_day.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Stichtag anzeigen");
			stage.setScene(new Scene(main));
			stage.show();
			//MW_info_label.setText("");
			
			
		} catch (Exception e) {
			log.error("ShowDayTable.fxml nicht gefunden");
			//MW_info_label.setText("Es konnten keine neuen Stammdaten erstellt werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	@FXML
	protected void MW_abdButtonPressed() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AdminLogin.fxml"));
			
			Parent main = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Administrator Login");
			stage.setScene(new Scene(main));
			stage.show();
			//MW_info_label.setText("");
			
			
		} catch (Exception e) {
			log.error("AdminLogin.fxml nicht gefunden");
			//MW_info_label.setText("Es konnten keine neuen Stammdaten erstellt werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	@FXML
	protected void MW_exportButtonPressed() {
		ExportFileTest.exportNow();
//		ExportFile.loadAndExport();
	}
	
//	@FXML
//	public void exitApplication(ActionEvent event) {
//		System.out.println("tralala");
//		ExportFile.exportNow(MW_TableView);
//		Platform.exit();
//	}
	
	StockDetails getSelectedStock() {
		return MW_TableView.getSelectionModel().getSelectedItem();
	}
	
//	public void exportNow() {
//		try {
//	//		//TODO
//	//		//Ort genauer festlegen => auf den Server!!!
//			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
//			Date date = new Date();
//			//Ort genauer festlegen => auf den Server
//			//Ort Andy : /Users/andy/Desktop/LagerPro/LagerPro/
//			//Ort Phil : 
//			File file = new File ("/Users/andy/Desktop/LagerPro/LagerPro/" + dateFormat.format(date) + ".txt");
//			
//			file.createNewFile();
//			
//			if (!file.equals(null)) {
//			
//				FileWriter fw = new FileWriter(file.getAbsoluteFile());
//				BufferedWriter bw = new BufferedWriter(fw);
//				
//				DateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
//				DateFormat timeF = new SimpleDateFormat("HH:mm:ss");
//				
//				bw.write("Lagerbestand am " + dateF.format(date) + " um " + timeF.format(date) + " Uhr:");
//				
//				ExportFile.exportWrite(bw, MW_TableView);
//				
//				bw.close();
//				fw.close();
//			} else {
//				log.error("Export-Datei konnte nicht erstellt werden");
//			}
//		} catch (Exception e) {
//			log.error("Export failed", e);
//		}
//	}
	
	
//	@Override
//	public void stop(){
//		ExportFile.exportNow(MW_TableView);
//	    System.out.println("Stage is closing");
//	    // Save file
//	}
	
	
//	public void exportWrite(BufferedWriter bw) {
//		try {
//			ArrayList<String> values = new ArrayList<>();
//			values = getTableViewValues(MW_TableView);
//			
//			for (int i=0; i<values.size(); i++) {
//				if (i%7==0) bw.write("\n__________\n\n");
//				bw.write(values.get(i) + " || ");
//			}
//			
//		} catch (Exception e) {
//			log.error("Fehler bei MainWindowController.exportWrite()", e);
//		}
//	}
	
//	private static ArrayList<String> getTableViewValues(TableView MW_TableView) {
//		ArrayList<String> values = new ArrayList<>();
//		ObservableList<TableColumn> columns = MW_TableView.getColumns();
//		
//		System.out.println(columns.size());
//		System.out.println(MW_TableView.getItems().size());
//		
//		
//		System.out.println("hola");
//		
//		for (Object row: MW_TableView.getItems()) {
//			for (TableColumn column : columns) {
//				values.add((String) column.getCellObservableValue(row).getValue().toString());
//			}
//		}		
//		
//		return values;
//	}

}
