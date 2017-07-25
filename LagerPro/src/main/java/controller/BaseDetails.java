package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BaseDetails {
	
	private final SimpleStringProperty Artikelnummer;
	private final StringProperty Produktname;
	private final IntegerProperty Gewicht;
	private final IntegerProperty Einlagerungszeit;
	
	public BaseDetails(String Artikelnummer, String Produktname, Integer Gewicht, Integer Einlagerungszeit) {
		this.Artikelnummer = new SimpleStringProperty(Artikelnummer);
		this.Produktname = new SimpleStringProperty(Produktname);
		this.Gewicht = new SimpleIntegerProperty(Gewicht);
		this.Einlagerungszeit = new SimpleIntegerProperty(Einlagerungszeit);
	}
	
	public StringProperty getArtikelnummer() {
		return Artikelnummer;
	}
	
	public void setArtikelnummer(String value){
		Artikelnummer.set(value);
	}
	
	public StringProperty getProduktname() {
		return Produktname;
	}
	
	public void setProduktname(String value){
		Produktname.set(value);
	}
	
	public IntegerProperty getGewicht() {
		return Gewicht;
	}
	
	public void setGewicht(int value){
		Gewicht.set(value);
	}
	
	public IntegerProperty getEinlagerungszeit() {
		return Einlagerungszeit;
	}
	
	public void setEinlagerungszeit(int value){
		Einlagerungszeit.set(value);
	}
	
	public StringProperty ArtikelnummerProperty(){
		return Artikelnummer;
	}
	
	public StringProperty ProduktnameProperty(){
		return Produktname;
	}
	
	public IntegerProperty GewichtProperty(){
		return Gewicht;
	}
	
	public IntegerProperty EinlagerungszeitProperty(){
		return Einlagerungszeit;
	}

}
