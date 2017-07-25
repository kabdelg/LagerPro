package controller;

	import org.apache.logging.log4j.LogManager;
	import org.apache.logging.log4j.Logger;
	import javafx.application.Application;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
	import javafx.stage.Stage;

	public class Controller extends Application {

		private static Logger log = LogManager.getLogger(Controller.class);

		
		public static void main(String[] args) {
			try {
				launch(args);
			} catch (Exception e) {
				log.error("Fehler beim Start des Programmes", e);
			}
		}

		public void start(Stage LoginStage) throws Exception {
			Pane login = (Pane) FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
			Scene loginScene = new Scene(login);
			LoginStage.setScene(loginScene);
			LoginStage.setResizable(false);
			LoginStage.setTitle("Login");
			LoginStage.getIcons().add(new Image("/icons/Icon_try.png"));
			LoginStage.show();
		}
		
//		@Override
//		public void stop(){
//			ExportFile.exportNow(MW_TableView);
//		    System.out.println("Stage is closing");
//		    // Save file
//		}
		
		

	}
