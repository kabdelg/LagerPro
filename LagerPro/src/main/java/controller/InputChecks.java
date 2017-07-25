package controller;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.StringUtils;

import gui.CreateStockController;
import gui.MainWindowController;

public class InputChecks {
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	public static boolean checkCreate(String CS_art_id, String CS_art_name, String CS_number,
			String CS_charge, String CS_weight, String CS_stock_loc, String CS_exp_date) {
		
		if (checkArtId(CS_art_id) &&
				checkNumber(CS_number) &&
				checkCharge(CS_charge) &&
				checkWeight(CS_weight) &&
				checkStockLoc(CS_stock_loc) &&
				checkExpDate(CS_exp_date)
		
		) return true;
		
		return false;
		
	}
	
	public static boolean checkArtId(String CS_art_id) {
		
		if (CS_art_id.equals("")) {
			log.error("Artikelnummer ist leer");
			return false;
		}
	
		if (CS_art_id.length()>6) {
			log.error("Artikelnummer besteht aus mehr als 6 Ziffern");
			return false;
		}
		
		return true;
		
	}
	
	
	public static boolean checkNumber(String CS_number) {
		
		if (CS_number.equals("")) {
			log.error("Anzahl ist leer");
			return false;
		}
		int number = Integer.parseInt(CS_number);
		if (number < 0){
			log.error("Anzahl kann nicht kleiner null sein");
			return false;
		}
		if(!StringUtils.isStrictlyNumeric(CS_number)) {
			log.error("Anzahl besteht nicht nur aus Zahlen");
			return false;
		}
		
		return true;
	}
	
	public static boolean checkCharge(String CS_charge) {
		
		if (CS_charge.equals("")) {
			log.error("Charge ist leer");
			return false;
		}
		
		if (CS_charge.length()>11)  {
			log.error("Charge besteht aus mehr als 11 Ziffern");
			return false;
		}
		
		return true;
	}
	
	public static boolean checkWeight(String CS_weight) {
		
		if (CS_weight.equals("")) {
			log.error("Gewicht ist leer");
			return false;
		}
		int weight = Integer.parseInt(CS_weight);
		if (weight < 0){
			log.error("Gewicht kann nicht kleiner null sein");
			return false;
		}
		//nur Zahlen oder mit "kg" ???
		
		
		return true;
	}
	
	public static boolean checkStockLoc(String CS_stock_loc) {
		
		if (CS_stock_loc.equals("")) {
			log.error("Lagerplatz ist leer");
			return false;
		}
		//erstes Zeichen A, B, C, D oder E
		
		//max. Länge = 4
		if (CS_stock_loc.length()>4) {
			log.error("Lagerplatz besteht aus mehr als 4 Stellen");
			return false;
		}
		//max Wert für zweites bis viertes Zeichen je nach Regal
		
		
		return true;
	}
	
	public static boolean checkExpDate(String cS_exp_date) {
		
		
		//wenn Datumformatierung steht => ExpDate > sysdate z.B. (LocalDate localDate = LocalDate.now();
		
		
		return true;
	}
	
	

}
