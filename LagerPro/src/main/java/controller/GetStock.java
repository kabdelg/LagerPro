package controller;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GetStock {
	
	@FXML
	private static TableView<StockDetails> MW_TableView;
	@FXML
	private static TableColumn<StockDetails, String> MW_ArtikelIDCol;
	@FXML
	private TableColumn<StockDetails, String> MW_ProduktNameCol;
	@FXML
	private TableColumn<StockDetails, Integer> MW_NumberCol;
	@FXML
	private TableColumn<StockDetails, String> MW_ChargeCol;
	@FXML
	private TableColumn<StockDetails, Integer> MW_GewichtCol;
	@FXML
	private TableColumn<StockDetails, String> MW_LagerplatzCol;
	@FXML
	private TableColumn<StockDetails, String> MW_ZuPruefenAbCol;
	
	@FXML
	private static TextField ES_art_id;
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
	
	public static String art_id;

	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	public static void setEdit(StockDetails item) {
//		StockDetails item = MW_TableView.getItems().get(row);
		art_id = (String) MW_ArtikelIDCol.getCellObservableValue(item).getValue().toString();
	}
	
	public static void loadEdit() {
		
//		TablePosition pos = MW_TableView.getSelectionModel().getSelectedCells().get(0);
//		int row = pos.getRow();
//		StockDetails item = MW_TableView.getItems().get(row);
//		art_id = (String) MW_ArtikelIDCol.getCellObservableValue(item).getValue().toString();
		ES_art_id.setText(GetStock.art_id);
		
		
	}
	
}
