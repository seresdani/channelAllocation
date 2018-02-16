package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Csatorna kiosztás");
        primaryStage.setWidth(825 );
        primaryStage.setHeight(650);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("tv.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
