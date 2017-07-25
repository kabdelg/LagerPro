package controller;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StockDetails {

	private final StringProperty Artikelnummer;
	private final StringProperty Produktname;
	private final IntegerProperty Anzahl;
	private final StringProperty Charge;
	private final IntegerProperty Gewicht;
	private final StringProperty Lagerplatz;
	private final StringProperty ZuPruefenAb;
	
	public StockDetails(String Artikelnummer, String Produktname, Integer Anzahl, String Charge, Integer Gewicht, String Lagerplatz, String ZuPruefenAb) {
		this.Artikelnummer = new SimpleStringProperty(Artikelnummer);
		this.Produktname = new SimpleStringProperty(Produktname);
		this.Anzahl = new SimpleIntegerProperty(Anzahl);
		this.Charge = new SimpleStringProperty(Charge);
		this.Gewicht = new SimpleIntegerProperty(Gewicht);
		this.Lagerplatz = new SimpleStringProperty(Lagerplatz);
		this.ZuPruefenAb = new SimpleStringProperty(ZuPruefenAb);
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

	public IntegerProperty getAnzahl() {
		return Anzahl;
	}
	
	public void setAnzahl(int value){
		Anzahl.set(value);
	}

	public StringProperty getCharge() {
		return Charge;
	}
	
	public void setCharge(String value){
		Charge.set(value);
	}

	public IntegerProperty getGewicht() {
		return Gewicht;
	}
	
	public void setGewicht(int value){
		Gewicht.set(value);
	}

	public StringProperty getLagerplatz() {
		return Lagerplatz;
	}
	
	public void setLagerplatz(String value){
		Lagerplatz.set(value);
	}

	public StringProperty getZuPruefenAb() {
		return ZuPruefenAb;
	}
	
	public void setZuPruefenAb(String value){
		ZuPruefenAb.set(value);
	}
	
	public StringProperty ArtikelnummerProperty(){
		return Artikelnummer;
	}
	
	public StringProperty ProduktnameProperty(){
		return Produktname;
	}
	
	public IntegerProperty AnzahlProperty(){
		return Anzahl;
	}
	
	public StringProperty ChargeProperty(){
		return Charge;
	}
	
	public IntegerProperty GewichtProperty(){
		return Gewicht;
	}
	
	public StringProperty LagerplatzProperty(){
		return Lagerplatz;
	}
	
	public StringProperty ZuPruefenAbProperty(){
		return ZuPruefenAb;
	}

	public StockDetails Stream() {
		// TODO Auto-generated method stub
		return null;
	}

	public StockDetails map(StockDetails object) {
		// TODO Auto-generated method stub
		return null;
	}

}
