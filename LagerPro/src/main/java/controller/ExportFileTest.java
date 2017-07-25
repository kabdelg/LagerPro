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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.MainWindowController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ExportFileTest {
	
	private static ObservableList <StockDetails> data;
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	public static void exportNow() {
		try {
	//		//TODO
	//		//Ort genauer festlegen => auf den Server!!!
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			//Ort genauer festlegen => auf den Server
			//Ort Andy : /Users/andy/Desktop/Exports/
			//Ort Phil : C:/Users/Philipp/DesktopExports/
			//Ort Karim: C:\Users\Karim\Desktop\Exports
			String outputFolder = System.getProperty("user.home") + "/Desktop/Exports/";
//			File file = new File (outputFolder + "Lagerbestand-"+ dateFormat.format(date) + ".txt");
			File file = new File ("/Users/andy/Desktop/Exports/" + "Lagerbestand-"+ dateFormat.format(date) + ".csv");

			
			file.createNewFile();
			
			if (!file.equals(null)) {
			
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				DateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
				DateFormat timeF = new SimpleDateFormat("HH:mm:ss");
				
//				bw.write("Lagerbestand am " + dateF.format(date) + " um " + timeF.format(date) + " Uhr:");
				
				exportWrite(bw);
				
				bw.close();
				fw.close();
			} else {
				log.error("Export-Datei konnte nicht erstellt werden");
			}
		} catch (Exception e) {
			log.error("Export failed", e);
		}
	}
	
	public static void exportWrite(BufferedWriter bw) {
		try {
			ArrayList<String> values = new ArrayList<>();
//			values = getTableViewValues(MW_TableView);
			values = loadAndExport();
			
			bw.write("Artikelnummer;Produktname;Anzahl;Charge;Gewicht;Lagerplatz;Zu Prüfen ab\n");
			
			for (int i=0; i<values.size(); i++) {
//				if (i%7==0) bw.write("\n__________\n\n");
				bw.write(values.get(i));
			}
			
		} catch (Exception e) {
			log.error("Fehler bei MainWindowController.exportWrite()", e);
		}
	}
	
	
	
	//read from database
	public static ArrayList<String> loadAndExport() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");
			
			
			ArrayList<String> values = new ArrayList<>();
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
			
			for (int i=0; i<data.size(); i++) {
				values.add(data.get(i).getArtikelnummer().getValue().toString() + ";"
						+ data.get(i).getProduktname().getValue().toString() + ";"
						+ data.get(i).getAnzahl().getValue().toString() + ";"
						+ data.get(i).getCharge().getValue().toString() + ";"
						+ data.get(i).getGewicht().getValue().toString() + ";"
						+ data.get(i).getLagerplatz().getValue().toString() + ";"
						+ data.get(i).getZuPruefenAb().getValue().toString() + "\n");
			}
//			System.out.println(data.get(0).getArtikelnummer().getValue() + " || " + data.get(0).getProduktname().getValue());
//			System.out.println(rs.getInt("Artikelnummer"));
			
			return values;
			
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		return null;
	}
	
	
	
	public static void startExport() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				exportNow();
			}
		};
		//speichert CSV-Datei nach 5 Sekunden, danach immer alle 2 Stunden, falls Programm nicht gecshlossen wird
		timer.scheduleAtFixedRate(task, 5000, 7200000);
	}
	
	
	public static void exportHourly() {
		
	}
	
}
