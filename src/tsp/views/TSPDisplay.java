package tsp.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TSPDisplay extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("TSPDisplay.fxml"));
		Scene scene = new Scene(root,200,200);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
