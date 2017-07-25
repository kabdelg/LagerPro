package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.MainWindowController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ExportFile {
	
	private static ObservableList <StockDetails> data;
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	public static void exportNow(TableView MW_TableView) {
		try {
	//		//TODO
	//		//Ort genauer festlegen => auf den Server!!!
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
			Date date = new Date();
			//Ort genauer festlegen => auf den Server
			//Ort Andy : /Users/andy/Desktop/LagerPro/LagerPro/
			//Ort Phil : C:/Users/Philipp/DesktopExports/
			File file = new File ("/Users/Philipp/Desktop/Exports/" + dateFormat.format(date) + ".txt");
			
			file.createNewFile();
			
			if (!file.equals(null)) {
			
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				DateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
				DateFormat timeF = new SimpleDateFormat("HH:mm:ss");
				
				bw.write("Lagerbestand am " + dateF.format(date) + " um " + timeF.format(date) + " Uhr:");
				
				ExportFile.exportWrite(bw, MW_TableView);
				
				bw.close();
				fw.close();
			} else {
				log.error("Export-Datei konnte nicht erstellt werden");
			}
		} catch (Exception e) {
			log.error("Export failed", e);
		}
	}
	
	public static void exportWrite(BufferedWriter bw, TableView MW_TableView) {
		try {
			ArrayList<String> values = new ArrayList<>();
			values = getTableViewValues(MW_TableView);
//			values = loadAndExport();
			
			for (int i=0; i<values.size(); i++) {
				if (i%7==0) bw.write("\n__________\n\n");
				bw.write(values.get(i) + " || ");
			}
			
		} catch (Exception e) {
			log.error("Fehler bei MainWindowController.exportWrite()", e);
		}
	}
	
	private static ArrayList<String> getTableViewValues(TableView MW_TableView) {
		ArrayList<String> values = new ArrayList<>();
		ObservableList<TableColumn> columns = MW_TableView.getColumns();
		
		for (Object row: MW_TableView.getItems()) {
			for (TableColumn column : columns) {
				values.add((String) column.getCellObservableValue(row).getValue().toString());
			}
		}		
		
		return values;
	}
	
	//read from database
	public static ArrayList<String> loadAndExport() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
			
			
			ArrayList<String> values = new ArrayList<>();
			data = FXCollections.observableArrayList();
			Statement myStmt = myConn.createStatement();
			ResultSet rs = myStmt.executeQuery("select * from stock");
			
			while (rs.next()){
//				System.out.println(rs.getInt("Artikelnummer"));
				data.add(new StockDetails(
						rs.getString("Artikelnummer"),
						
						rs.getString("Produktname"),
						rs.getInt("Anzahl"),
						rs.getString("Charge"),
						rs.getInt("Gewicht"),
						rs.getString("Lagerplatz"),
						rs.getString("ZuPruefenAb")));
			}
			
			for (int i=0; i<data.size(); i++) {
				values.add(data.get(i).getArtikelnummer().getValue().toString() + " || "
						+ data.get(i).getProduktname().getValue().toString() + " || "
						+ data.get(i).getAnzahl().getValue().toString() + " || "
						+ data.get(i).getCharge().getValue().toString() + " || "
						+ data.get(i).getGewicht().getValue().toString() + " || "
						+ data.get(i).getLagerplatz().getValue().toString() + " || "
						+ data.get(i).getZuPruefenAb().getValue().toString() 
						+ "\n________\n\n");
			}
//			System.out.println(data.get(0).getArtikelnummer().getValue() + " || " + data.get(0).getProduktname().getValue());
//			System.out.println(rs.getInt("Artikelnummer"));
			
			return values;
			
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		return null;
	}
	
}
