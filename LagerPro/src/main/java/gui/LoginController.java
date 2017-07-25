package gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import controller.Database;
import controller.ExportFile;
import controller.ExportFileTest;
import controller.Login;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController {

	@FXML
	Button LI_login;
	@FXML
	TextField LI_user;
	@FXML
	PasswordField LI_pw;
	@FXML
	Label LoginFail;
	@FXML
	TableView MW_TableView;

	private static Logger log = LogManager.getLogger(LoginController.class);


	@FXML
	protected void LoginButtonPressed() {
		try {
			if (Login.LoginCheck(LI_user.getText(), LI_pw.getText())) {
				if (Database.DatabaseCheck()){
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow2.fxml"));
					Parent mainWindowParent = (Parent) fxmlLoader.load();
					Stage mainWindowStage = new Stage();
					mainWindowStage.setScene(new Scene(mainWindowParent));
					mainWindowStage.setResizable(true);
					mainWindowStage.setTitle("LagerPro");
					
					mainWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
						
						@Override
						public void handle(WindowEvent event) {
							ExportFileTest.exportNow();
							
						}
					});
					
					mainWindowStage.show();
					Stage LoginStage = (Stage) LI_login.getScene().getWindow();
					LoginStage.close();
				} else {
					LoginFail.setText("Server nicht erreichbar!");
				}
			} else {
				LoginFail.setText("Benutzername oder Passwort falsch");
				log.error("Falscher Benutzername/Passwort");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Hauptfenster wurde nicht gefunden/geöffnet", e);
			LoginFail.setText("Login nicht möglich, bitte wenden sie sich an den Administrator!");
		}
	}
	@FXML
	protected void onEnter() {
		LoginButtonPressed();
	}
	
	
}