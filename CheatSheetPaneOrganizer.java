package Wordament;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.*;

/* Basically the PaneOrganizer of the new window whenever people want to see the cheat sheet of the list of
 * available words. Communicates with the WordFactory class in order to get the list of playable words and
 * display it.
 */
public class CheatSheetPaneOrganizer {
	
	private BorderPane _cheatSheet;
	
	//// Constructor of the Cheat Sheet
	public CheatSheetPaneOrganizer() {
		_cheatSheet = new BorderPane();
		_cheatSheet.setPrefSize(300, 300);
		CheatSheetPaneOrganizer.this.displayListOfWords();
	}
	
	//// Method for the SideBar to communicate with the CheatSheet
	public BorderPane getRoot() {
		return _cheatSheet;
	}
	
	//// METHOD TO DISPLAY LIST OF WORDS
	private void displayListOfWords() {
		// Basically takes in a set of words to display it onto the SideBar
		ObservableList<String> listOfWords = FXCollections.observableArrayList(WordFactory.getListOfAvailableWords());
		ListView<String> listViewOfWords = new ListView<String>(listOfWords);
		listViewOfWords.setPrefWidth(300);
		listViewOfWords.setPrefHeight(300);
		listViewOfWords.setStyle(Constants.CHEAT_SHEET_COLOR);
		listViewOfWords.setOrientation(Orientation.VERTICAL);
		_cheatSheet.setLeft(listViewOfWords);
	}
	
}
