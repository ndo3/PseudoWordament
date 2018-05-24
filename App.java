package Wordament;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/* THIS IS WHERE THE ENTIRETY OF MY INDEPENDENT PROJECT GET STARTED! GET PUMPED! WOOHOO!
 */

public class App extends Application {
	private static Stage _stage;
	@Override
	public void start(Stage stage) throws IOException, FileNotFoundException {
		_stage = stage;
		_stage.setTitle("Wordament");
		_stage.show();
		PaneOrganizer paneOrganizer = new PaneOrganizer();
		Scene scene = new Scene (paneOrganizer.getRoot());
		_stage.setScene(scene);
	}

	
	 public static void main(String[] argv) {
		 launch(argv);
	 }
	 
	 public static void updateScene() throws IOException, FileNotFoundException {
		 PaneOrganizer paneOrganizer = new PaneOrganizer();
		 Scene scene = new Scene(paneOrganizer.getRoot());
		 _stage.setScene(scene);
	 }
}
