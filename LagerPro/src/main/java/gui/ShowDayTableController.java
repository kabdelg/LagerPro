package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.StockDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ShowDayTableController {
	
	@FXML
	private Button SD_back;
	
	@FXML
	private Button SD_chooseDay;
	
	@FXML
	private TableView<StockDetails> SD_TableView;
	@FXML
	private TableColumn<StockDetails, String> SD_ArtikelIDCol;
	@FXML
	private TableColumn<StockDetails, String> SD_ProduktNameCol;
	@FXML
	private TableColumn<StockDetails, Number> SD_NumberCol;
	@FXML
	private TableColumn<StockDetails, String> SD_ChargeCol;
	@FXML
	private TableColumn<StockDetails, Number> SD_GewichtCol;
	@FXML
	private TableColumn<StockDetails, String> SD_LagerplatzCol;
	@FXML
	private TableColumn<StockDetails, String> SD_ZuPruefenAbCol;
	
	private ObservableList <StockDetails> data;
	
	private static Logger log = LogManager.getLogger(ShowDayTableController.class);
	
	@FXML
	private void SD_chooseButtonPressed() {
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Tag auswählen");
		
		try {
			fc.setInitialDirectory(new File("/Users/andy/Desktop/Exports/"));
		} catch (Exception e){
			log.error("InitialDirectory konnte nicht gefunden werden");
		}
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("CSV Files", "*.csv"));
		
		File selectedFile = fc.showOpenDialog((Stage) SD_chooseDay.getScene().getWindow());
		
		
//		String readFile = "";
		if (selectedFile!=null) {
//			System.out.println("not null");
			loadDataDay(selectedFile);
//			try {
//				Scanner s = new Scanner(selectedFile).useDelimiter("\n__________\n\n");
//				while (s.hasNext()) {
////					if (s.hasNextInt()) {
////						readFile = readFile + s.nextInt();
////					} else {
//						readFile = readFile + s.next();
////					}
//				}
//				s.close();
//				System.out.println(readFile);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String readFile = readFile(selectedFile).toString();
//			System.out.println(readFile);
		} else {
			System.out.println("selectedFile is null");
		}
//		loadDataDay(selectedFile);
		
	}
	
	@FXML
	private void SD_backButtonPressed() {
		try {
			Stage ShowDayStage = (Stage) SD_back.getScene().getWindow();
			ShowDayStage.close();
		} catch (Exception e) {
			log.error("Zurück-Funktion fehlgeschlagen");
		}
	}
	
	@FXML
	private void loadDataDay(File selectedFile) {
		try {
			String hola = "\"'%d.%m.%Y'\"";
			data = FXCollections.observableArrayList();
			List<String> arrList = new ArrayList<String>();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(selectedFile));
				String line = null;
				while ((line = br.readLine()) != null) {
					String[] lineSplit = line.split(";");
					for (String input : lineSplit) {
						arrList.add(input);
					}
					
				}
			} finally {
				br.close();
			}
			
			for (int i=7; i<arrList.size(); i=i+7) {
				data.add(new StockDetails(
						arrList.get(i).toString(), 
						arrList.get(i+1).toString(), 
						Integer.parseInt(arrList.get(i+2)), 
						arrList.get(i+3).toString(), 
						Integer.parseInt(arrList.get(i+4)), 
						arrList.get(i+5).toString(), 
						arrList.get(i+6)));
			}
			
			SD_ArtikelIDCol.setCellValueFactory(new PropertyValueFactory<>("Artikelnummer"));
			SD_ProduktNameCol.setCellValueFactory(new PropertyValueFactory<>("Produktname"));
			SD_NumberCol.setCellValueFactory(new PropertyValueFactory<>("Anzahl"));
			SD_ChargeCol.setCellValueFactory(new PropertyValueFactory<>("Charge"));
			SD_GewichtCol.setCellValueFactory(new PropertyValueFactory<>("Gewicht"));
			SD_LagerplatzCol.setCellValueFactory(new PropertyValueFactory<>("Lagerplatz"));
			SD_ZuPruefenAbCol.setCellValueFactory(new PropertyValueFactory<>("ZuPruefenAb"));
			SD_TableView.setId("StockTable");
		
			SD_TableView.setItems(data);
			
		} catch (Exception e) {
			log.error("loadDataDay failed", e);
			System.err.println("Error: " + e);
		}
	}


}
