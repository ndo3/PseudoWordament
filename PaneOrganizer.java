package Wordament;

import javafx.scene.layout.BorderPane;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class PaneOrganizer {

	private static BorderPane _borderPane;
	private static Pane _wordamentPane;
	private static Pane _sideBarPane;
	private static WordamentGame _game;
	private static SideBar _sidebar;

	// This is the PaneOrganizer, which basically puts the Pane 
	public PaneOrganizer() throws IOException, FileNotFoundException {
		_borderPane = new BorderPane();
		_borderPane.setStyle(Constants.GAME_PANE_COLOR);
		_borderPane.setPrefSize(Constants.FRAME_WIDTH,Constants.FRAME_HEIGHT);
		_wordamentPane = new Pane();
		_sideBarPane = new Pane();
		_game = new WordamentGame(_wordamentPane);
		_sidebar = new SideBar(_sideBarPane);
		_borderPane.setLeft(_game.getRoot());
		_borderPane.setRight(_sidebar.getRoot());
	}
	// This method is to communicate with the App about the BorderPane
	public BorderPane getRoot() {
		return _borderPane;
	}
	
	// This method is to reset the game - communicating with the SideBar
	public static void resetGame() throws IOException, FileNotFoundException {
		_borderPane.getChildren().removeAll(_game.getRoot(), _sidebar.getRoot());
		_wordamentPane = new Pane();
		_sideBarPane = new Pane();
		_game = new WordamentGame(_wordamentPane);
		_sidebar = new SideBar(_sideBarPane);
		_borderPane.setLeft(_game.getRoot());
		_borderPane.setRight(_sidebar.getRoot());	
	}
}
