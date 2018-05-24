package Wordament;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;

import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.*;

/*(4) SideBar: Basically, the job of this class is to create a User Interface to allow the players
to communicate with the game about the technicalities - pause, enable computer player, open the cheat
sheet, display the list of words already played, quit, reset game, etc.
 */
public class SideBar {

	private Pane _pane;
	private static Label[] _label;
	private static Button[] _button;
	private int _secondsRemaining;
	private static ListView<String> _listViewOfAchievedWords;
	private static GridPane _functionGrid;
	private Timeline _timeline;
	private boolean _computerPlayerEnabled;
	private static int _playerScore;
	private static int _computerScore;
	private boolean _gameIsPlaying;
	private static boolean _computerIsPlaying;
	private static boolean _playerIsPlaying;

	// Constructor for the SideBar class
	public SideBar(Pane pane) {
		// These methods in general is to generate the Sidebar
		_pane = pane;
		_pane.setPrefSize(Constants.SIDEBAR_WIDTH, Constants.FRAME_HEIGHT);
		_button = new Button[5]; // This is to instantiate the buttons used in the SideBar
		_label = new Label[5]; // to instantiate the required labels that will be used in the sideBar
		this.createFunctionPane();
		// These two methods below instantiate the countdown timer in the sidebar
		_secondsRemaining = Constants.STARTING_SECONDS;
		this.setUpTimeline();
		// These instance variables is to keep track of what is happening in the game
		_computerPlayerEnabled = false;
		_computerScore = 0;
		_gameIsPlaying = true;
		_computerIsPlaying = false;
		_playerIsPlaying = true;
	}
	
	// Method to communicate with the PaneOrganizer
	public Pane getRoot() {
		return _pane;
	}
	
	// This method is to create the GUI
	public void createFunctionPane() {
		// Instantiating and adding some spice to the control pane on the right of the program
		// Creating the buttons
		_button[0] = new Button("Quit");
		_button[1] = new Button("Reset");
		_button[2] = new Button("Pause");
		_button[3] = new Button ("Enable Computer Player");
		_button[4] = new Button("Cheat Sheet");
		// Creating the labels
		_label[0] = new Label("Score: 0");
		_label[1] = new Label("Computer Score: N/A");
		_label[2] = new Label("Seconds remaining: " + _secondsRemaining);
		_label[3] = new Label("Drag & drop mouse to select meaningful words!!");
		_label[4] = new Label("Good luck!");
		// Creating the function Grid that would add everything to it to make it beautiful
		_functionGrid = new GridPane();
		_functionGrid.setVgap(Constants.V_GAP_GRID_PANE);
		_functionGrid.setMinWidth(Constants.SIDEBAR_WIDTH);
		_functionGrid.setMaxWidth(Constants.SIDEBAR_WIDTH);
		// The for loop to add stuff to the grid and set style and whatnot in the shortest way
		for (int i = 0; i < 5; i++) {
			_button[i].setStyle(Constants.SIDEBAR_BUTTON_COLOR);
			_functionGrid.add(_button[i], 0, 5 + i);
			_label[i].setTextFill(Color.WHITE);
			_functionGrid.add(_label[i], 0, i);
			_functionGrid.setHalignment(_button[i], HPos.CENTER);
			_functionGrid.setHalignment(_label[i], HPos.CENTER);
		}
		// The methods to set the buttons on action whenever they are clicked
		_button[0].setOnAction(new QuitHandler());
		_button[1].setOnAction(new ResetHandler());
		_button[2].setOnAction(new PauseHandler());
		_button[3].setOnAction(new SwitchComputerMode());
		_button[4].setOnAction(new OpenCheatSheet());
		// The method to create the list of the words selected by the player (and also potentially the machine)
		SideBar.updateListOfAchievedWords();
		// Creating a Vertical Box and adding everything to the game
		VBox panel = new VBox();
		// This is to set fixed dimensions for the sidebar so that it wouldn't change the size in case anything happens
		panel.setMinHeight(Constants.FRAME_HEIGHT);
		panel.setMaxHeight(Constants.FRAME_HEIGHT);
		panel.setMinWidth(Constants.SIDEBAR_WIDTH);
		panel.setMaxWidth(Constants.SIDEBAR_WIDTH);
		panel.getChildren().addAll(_functionGrid);
		panel.setStyle(Constants.SIDEBAR_COLOR);
		panel.setAlignment(Pos.CENTER);
		// Adding everything to the pane
		_pane.getChildren().addAll(panel); // the thing that adds everything
	}
	
	
	///////////////////////////////////////////// BEGINNING OF COUNT DOWN METHOD //////////////////////////////////////////////
	
	///// METHOD TO CREATE TIMELINE THAT ALLOWS THE SECONDS TO COUNT DOWN
	private void setUpTimeline() {
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.TIMELINE_SINGULAR_DURATION), new CountDownHandler());
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Constants.STARTING_SECONDS);
		_timeline.play();
	}
	
	///// METHOD TO CREATE THE TIME HANDLER THAT ALLOWS THE SECONDS TO COUNT DOWN
	private class CountDownHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// This basically counts down
			_secondsRemaining = _secondsRemaining - 1;
			_label[2].setText("Seconds remaining: " + _secondsRemaining);
			// When there is no time left, which means the game times out already
			if (_secondsRemaining == 0) {
				// The method above basically communicates with the Game that game is already over, and graphically
				// makes the game over on the Sidebar
				WordamentGame.pauseOrUnpause();
				_label[4].setText("Game Over!");
				_button[2].setDisable(true);
				_button[3].setDisable(true);
				_timeline.stop();
			}
		}
	}
	//////////////////////////////////////////////// END OF COUNT DOWN METHOD /////////////////////////////////////////////////

	//////////////////////////////////////// BEGINNING OF BUTTON RESPONSES METHODS ////////////////////////////////////////////
	
	// The handler to make sure that the game stops
	private class QuitHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			System.exit(0);
		}
	}
	
	////// METHOD TO MAKE THE GAME RESET
	private class ResetHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try {
				PaneOrganizer.resetGame();
				_secondsRemaining = 0; // This is so that we can completely stop the time from the old thing from running
				_timeline.stop(); // This is to prevent two timelines from playing when we create new game
			} catch (IOException e) {
				// This is to catch exceptions
				e.printStackTrace();
			}
		}
	}

	////// METHOD TO OPEN ANOTHER WINDOW FOR CHEAT SHEET - DONE - DON'T TOUCH
	private class OpenCheatSheet implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Stage stage = new Stage();
			stage.setTitle("Cheat Sheet - oooh naughty");
			stage.show();
			CheatSheetPaneOrganizer cheatSheet = new CheatSheetPaneOrganizer();
			Scene scene = new Scene(cheatSheet.getRoot());
			stage.setScene(scene);
		}
	}
	
	////// METHOD TO PAUSE THE GAME
	private class PauseHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// If the game is playing, then disable everything logically and graphically
			if (_gameIsPlaying == true) {
				_timeline.stop();
				_label[4].setText("Game Paused");
				_button[2].setText("Unpause");
				WordamentGame.pauseOrUnpause();
				_gameIsPlaying = false;
			} else {
				// If the game is paused, then enable everything logically and graphically again
				if (_gameIsPlaying == false) {
					_timeline.play();
					_button[2].setText("Pause");
					_label[4].setText("Game Unpaused");
					WordamentGame.pauseOrUnpause();
					_gameIsPlaying = true;
				}
			}
		}
	}
	
	////// METHOD TO ENABLE THE COMPUTER OR DISABLE THE COMPUTER PLAYER
	private class SwitchComputerMode implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// If the computer is enabled, then when the button is clicked, it will logically and graphically disable
			// the computer player
			if (_computerPlayerEnabled == false) {
				_computerPlayerEnabled = true;
				_button[3].setText("Disable Computer Player");
				_label[1].setText("Computer Score: " + _computerScore);
				_button[4].setDisable(true);
				WordamentGame.turnOnOrOffComputer();
			} else {
				// If the computer is not enabled, then when the button is clicked, it will logically and graphically enable
				// the computer player
				if (_computerPlayerEnabled == true) {
					_computerPlayerEnabled = false;
					_button[3].setText("Enable Computer Player");
					_label[1].setText("Computer Score: N/A");
					_button[4].setDisable(false);
					WordamentGame.turnOnOrOffComputer();
				}
			}
		}
	}	
	///////////////////////////////////////// END OF BUTTON RESPONSES METHOD //////////////////////////////////////////
	
	//////////////////////////////////////BEGIN OF COMPUTER MODE HELPER METHODS////////////////////////////////////////
	
	///// METHOD TO TELL THE BOARD ITSELF TO UPDATE THE SCORE
	public static void updateComputerScore(int score) {
		// Basically takes in the computer's score from the game, then will communicate with the game about the score, and then
		// graphically update the score in the GUI
		_computerScore = score;
		_label[1].setText("Computer Score: " + _computerScore);
	}
	
	///// METHOD TO MANAGE THE INFORMATION IN GENERAL - LOGIC WISE AND GRAPHIC WISE - WHENEVER THE GAME SWITCHES
	// TO COMPUTER'S TURN OR TO HUMAN'S TURN, SIDEBAR WILL SWITCH AS WELL
	public static void updatePlayerAndComputerInfo() {
		if (_computerIsPlaying == true && _playerIsPlaying == false) {
			_computerIsPlaying = false;
			_playerIsPlaying = true;
			_button[2].setDisable(false);
			_button[3].setDisable(false);
		} else {
			if (_computerIsPlaying == false && _playerIsPlaying == true) {
				_computerIsPlaying = true;
				_playerIsPlaying = false;
				_button[3].setDisable(true);
				_button[2].setDisable(true);
			}
		}
		SideBar.changePlayerNotificationStatus();
	}
	
	// This is to graphically update the situation of the game
	public static void changePlayerNotificationStatus() {
		if (_computerIsPlaying == true && _playerIsPlaying == false) {
			_label[4].setText("Waiting for computer to move");
		} else {
			if (_computerIsPlaying == false && _playerIsPlaying == true) {
				_label[4].setText("It's your turn!");
			}
		}
		
	}
	
	////////////////////////////////////// END OF COMPUTER MODE HELPER METHODS ////////////////////////////////////////
	
	///////////////////////////////////// BEGIN OF OTHER / RESULT HELPER METHODS //////////////////////////////////////
	
	///// METHOD TO TELL THE BOARD ITSELF TO UPDATE THE SCORE
	public static void updateScore(int score) {
		// Basically takes in the player's score from the game, then will communicate with the game about the score, and then
		// graphically update the score in the GUI
		_playerScore = score;
		_label[0].setText("Score: " + _playerScore);
	}
	
	//// METHOD TO DISPLAY THE ACHIEVED WORDS
	public static void updateListOfAchievedWords() {
		ObservableList<String> listOfAchievedWords = FXCollections.observableArrayList(WordamentGame.getAchievedWords());
		_listViewOfAchievedWords = new ListView<String>(listOfAchievedWords);
		_listViewOfAchievedWords.setOrientation(Orientation.VERTICAL);
		// Method to make sure the new List View has also the same dimensions we desire as well
		_listViewOfAchievedWords.setMinWidth(Constants.LISTVIEW_WIDTH);
		_listViewOfAchievedWords.setMaxWidth(Constants.LISTVIEW_WIDTH);
		_listViewOfAchievedWords.setMinHeight(Constants.LISTVIEW_HEIGHT);
		_listViewOfAchievedWords.setMaxHeight(Constants.LISTVIEW_HEIGHT);
		// Basically replace the old one with the new one by adding it to the same position
		_functionGrid.add(_listViewOfAchievedWords, 0, Constants.LISTVIEW_POSITION);
		// Set the alignment to center again
		_functionGrid.setHalignment(_listViewOfAchievedWords, HPos.CENTER);
	}
	
	////////////////////////////////////// END OF OTHER / RESULT HELPER METHODS ///////////////////////////////////////
}
