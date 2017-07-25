package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.StockDetails;

public class ExpiredProductsController implements Initializable {
	
	@FXML
	private Button EP_edit_stock;
	@FXML
	private Button EP_delete_stock;
	@FXML
	private TextField EP_Search;
	@FXML
	private Label EP_info_label;
	@FXML
	private TableView<StockDetails> EP_TableView;
	@FXML
	private TableColumn<StockDetails, String> EP_ArtikelIDCol;
	@FXML
	private TableColumn<StockDetails, String> EP_ProduktNameCol;
	@FXML
	private TableColumn<StockDetails, Number> EP_NumberCol;
	@FXML
	private TableColumn<StockDetails, String> EP_ChargeCol;
	@FXML
	private TableColumn<StockDetails, Number> EP_GewichtCol;
	@FXML
	private TableColumn<StockDetails, String> EP_LagerplatzCol;
	@FXML
	private TableColumn<StockDetails, String> EP_ZuPruefenAbCol;
	
	@FXML
	private Button EP_back;
	
	private static Logger log = LogManager.getLogger(MainWindowController.class);
	
	private ObservableList <StockDetails> EP_data;

	@Override
	public void initialize(URL url, ResourceBundle rb){
		EP_loadData();
		EP_searchStock();
    }
	
	@FXML
	public void EP_loadData (){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wevotest", "root", "");

			EP_data = FXCollections.observableArrayList();
			Statement myStmt = myConn.createStatement();
			ResultSet rs = myStmt.executeQuery("SELECT *, DATE_FORMAT(ZuPruefenAb, \"%d.%m.%Y\") AS Date FROM Stock WHERE ZuPruefenAb <= CURRENT_DATE");
			
			while (rs.next()){
				EP_data.add(new StockDetails(
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

	
	EP_ArtikelIDCol.setCellValueFactory(new PropertyValueFactory<>("Artikelnummer"));
	EP_ProduktNameCol.setCellValueFactory(new PropertyValueFactory<>("Produktname"));
	EP_NumberCol.setCellValueFactory(new PropertyValueFactory<>("Anzahl"));
	EP_ChargeCol.setCellValueFactory(new PropertyValueFactory<>("Charge"));
	EP_GewichtCol.setCellValueFactory(new PropertyValueFactory<>("Gewicht"));
	EP_LagerplatzCol.setCellValueFactory(new PropertyValueFactory<>("Lagerplatz"));
	EP_ZuPruefenAbCol.setCellValueFactory(new PropertyValueFactory<>("ZuPruefenAb"));	
	EP_TableView.setId("EP_StockTable");
	
	EP_TableView.setItems(EP_data);
	
}
	
	public void EP_searchStock() {
		EP_ProduktNameCol.setCellValueFactory(cellData -> cellData.getValue().ProduktnameProperty());
		EP_ArtikelIDCol.setCellValueFactory(cellData -> cellData.getValue().ArtikelnummerProperty());
		EP_ChargeCol.setCellValueFactory(cellData -> cellData.getValue().ChargeProperty());
		EP_LagerplatzCol.setCellValueFactory(cellData -> cellData.getValue().LagerplatzProperty());
		
        FilteredList<StockDetails> EP_filteredData = new FilteredList<>(EP_data, p -> true);
        EP_Search.textProperty().addListener((observable, oldValue, newValue) -> {
            EP_filteredData.setPredicate(StockDetails -> {
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

        SortedList<StockDetails> EP_sortedData = new SortedList<>(EP_filteredData);
        EP_sortedData.comparatorProperty().bind(EP_TableView.comparatorProperty());
        EP_TableView.setItems(EP_sortedData);
	}
	
	@FXML
	protected void EP_editButtonPressed() {
		try {
			if (EP_getSelectedStock() == null){
				EP_info_label.setText("Kein Lagereintrag ausgewählt !");
			} else {
				
				TablePosition pos = EP_TableView.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				StockDetails item = EP_TableView.getItems().get(row);
				MainWindowController.set_art_id = (String) EP_ArtikelIDCol.getCellObservableValue(item).getValue().toString();
				MainWindowController.set_art_name = (String) EP_ProduktNameCol.getCellObservableValue(item).getValue().toString();
				MainWindowController.set_number = (String) EP_NumberCol.getCellObservableValue(item).getValue().toString();
				MainWindowController.set_charge = (String) EP_ChargeCol.getCellObservableValue(item).getValue().toString();
				MainWindowController.set_weight = (String) EP_GewichtCol.getCellObservableValue(item).getValue().toString();
				MainWindowController.set_stock_loc = (String) EP_LagerplatzCol.getCellObservableValue(item).getValue().toString();
				MainWindowController.set_exp_date = (String) EP_ZuPruefenAbCol.getCellObservableValue(item).getValue().toString();

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
						EP_loadData();
						EP_searchStock();
					}
				});
				stage.setScene(new Scene(main));
				
				stage.show();
				//ES_art_id.setText(art_id);
//				MW_info_label.setText("");
				
			
			}
		} catch (Exception e) {
			log.error("EditStock.fxml nicht gefunden", e);
			EP_info_label.setText("Der Lagereintrag konnte nicht geändert werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	@FXML
	protected void EP_deleteButtonPressed() {
		try {
			if (EP_getSelectedStock() == null){
				EP_info_label.setText("Kein Lagereintrag ausgewählt !");
			} else {
				
			TablePosition pos = EP_TableView.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			StockDetails item = EP_TableView.getItems().get(row);
			MainWindowController.set_art_id = (String) EP_ArtikelIDCol.getCellObservableValue(item).getValue().toString();
			MainWindowController.set_art_name = (String) EP_ProduktNameCol.getCellObservableValue(item).getValue().toString();
			MainWindowController.set_number = (String) EP_NumberCol.getCellObservableValue(item).getValue().toString();
			MainWindowController.set_charge = (String) EP_ChargeCol.getCellObservableValue(item).getValue().toString();
			MainWindowController.set_weight = (String) EP_GewichtCol.getCellObservableValue(item).getValue().toString();
			MainWindowController.set_stock_loc = (String) EP_LagerplatzCol.getCellObservableValue(item).getValue().toString();
			MainWindowController.set_exp_date = (String) EP_ZuPruefenAbCol.getCellObservableValue(item).getValue().toString();			
				
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DeleteStock.fxml"));
			Parent main = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.setTitle("Lagereintrag löschen");
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					EP_loadData();
					EP_searchStock();
				}
			});
			stage.setScene(new Scene(main));
			stage.show();
			EP_info_label.setText("");
			}
			
		} catch (Exception e) {
			log.error("DeleteStock.fxml nicht gefunden");
			System.err.println(e);
			EP_info_label.setText("Der Lagereintrag konnte nicht gelöscht werden, bitte wenden Sie sich an einen Administrator!");
		}
	}
	
	
	@FXML
	protected void EP_backButtonPressed() {
		try {
			Stage ExpiredProductsStage = (Stage) EP_back.getScene().getWindow();
			ExpiredProductsStage.close();
		} catch (Exception e) {
			log.error("Zurück-Funktion fehlgeschlagen");
		}
	}
	
	StockDetails EP_getSelectedStock() {
		return EP_TableView.getSelectionModel().getSelectedItem();
	}

}
