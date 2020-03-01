package MediaPlayer;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("pp.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Media player");
		primaryStage.setScene(scene);
		primaryStage.show();
		Controller con = loader.getController();
		con.setStage(primaryStage);

	}

	public static void main(String[] args) {

		launch(args);

	}

}