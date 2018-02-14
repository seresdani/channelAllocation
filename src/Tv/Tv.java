package Tv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class Tv extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
		
		Scene scene = new Scene(root);
		
		stage.setTitle("Csatorna kiosztás");
		stage.setWidth(800);
		stage.setHeight(600);
		stage.setScene(scene);
		stage.show();
		
	}
	
	public static void main (String[] args) {
		
		launch(args);
		
	}
	
	
}