package gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminLoginController {
	
	@FXML
	Button ALI_login;
	@FXML
	TextField ALI_user;
	@FXML
	PasswordField ALI_pw;
	@FXML
	Button ALI_back;
	@FXML
	Label AdminLoginFail;
	
	
	private static Logger log = LogManager.getLogger(LoginController.class);

	@FXML
	protected void ALI_loginButtonPressed() {
		try {
			if (Login.AdminLoginCheck(ALI_user.getText(), ALI_pw.getText())) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddBaseData.fxml"));
				Parent ABDParent = (Parent) fxmlLoader.load();
				Stage ABDStage = new Stage();
				ABDStage.initModality(Modality.APPLICATION_MODAL);
				ABDStage.setResizable(false);
				ABDStage.setTitle("Neue Stammdaten erstellen");
				ABDStage.setScene(new Scene(ABDParent));
				ABDStage.show();
				
				Stage AdminLoginStage = (Stage) ALI_login.getScene().getWindow();
				AdminLoginStage.close();
			} else {
				AdminLoginFail.setText("Benutzername oder Passwort falsch");
				log.error("Falscher Benutzername/Passwort");
			}
		} catch (Exception e) {
			log.error("AdminLogin fehlgeschlagen", e);
			
		}
	}
	
	@FXML
	protected void ALI_onEnter() {
		try {
			if (Login.AdminLoginCheck(ALI_user.getText(), ALI_pw.getText())) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddBaseData.fxml"));
				Parent ABDParent = (Parent) fxmlLoader.load();
				Stage ABDStage = new Stage();
				ABDStage.initModality(Modality.APPLICATION_MODAL);
				ABDStage.setScene(new Scene(ABDParent));
				ABDStage.setResizable(true);
				ABDStage.show();
				Stage AdminLoginStage = (Stage) ALI_login.getScene().getWindow();
				AdminLoginStage.close();
			} else {
				AdminLoginFail.setText("Benutzername oder Passwort falsch");
				log.error("Falscher Benutzername/Passwort");
			}
		} catch (Exception e) {
			log.error("AdminLogin fehlgeschlagen", e);
			
		}
	}
	
	@FXML
	protected void ALI_backButtonPressed() {
		try {
			Stage ALIStage = (Stage) ALI_back.getScene().getWindow();
			ALIStage.close();
		} catch (Exception e) {
			log.error("AdminLoginBack Fehler", e);
		}
	}

}
