package Wordament;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Queue;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.event.ActionEvent;

import javafx.scene.input.MouseEvent;

import java.util.*;

/*
 * (3) WordamentGame: Basically, the job of this class is to control the game. 
- A large chunk of the methods
in this class is to control the program based on the mouse moves input from the user (dragged, released).
- It basically controls the back end processing of everything - whether the word input is valid or not
(adjacent, repeated, etc.), control the logical and graphical responses in the game, control the graphical
effects necessary in the program.
- It also controls the rotation of the board (it gets the logical rotation from the WordFactory, then
graphically display it, as well as allowing the user to tell the board to rotate.
- It also controls the alternation of the user's moves and the computer's move, should the computer mode
is on
- It also has some random helper methods.
 */

//////// THIS IS WHERE THE CLASS STARTS WHOOHOO!!!! :)
public class WordamentGame {

	private static Pane _pane;
	private WordFactory _wordFactory;
	private static WordButton[][] _array;
	private static WordButton _rotateButton;
	private String _currentString;
	private Boolean[][] _visited;
	private int _currentScore;
	private int _currentComputerScore;
	private static  MouseHandler _mouseDraggedHandler;
	private MouseReleasedHandler _mouseReleasedHandler;
	private LinkedList<String> _currentStringStack;
	private LinkedList<Integer> _currentStringStackX;
	private LinkedList<Integer> _currentStringStackY;
	private static LinkedList<String> _achievedWords;
	private static HashSet<String> _achievedWordsHashSet;
	private boolean _exitedAWord;
	private boolean _visitedAWord;
	private double _currentButtonX;
	private double _currentButtonHigherX;
	private double _currentButtonY;
	private double _currentButtonHigherY;
	
	private static boolean _gameIsPlaying;
	private static boolean _playerIsPlaying;
	private static boolean _computerIsPlaying;
	private static boolean _computerSwitchedOff;
	
	private double _countUp;
	private double _waitingForComputerCount;
	private ComputerPlayer _computer;

	
	public WordamentGame(Pane pane) throws IOException, FileNotFoundException {
		_pane = pane;
		_wordFactory = new WordFactory(); // This is to install a WordFactory in this so it can do stuff in the game
		_array = _wordFactory.createBoard(); // This is to communicate the logical board with the game
		WordamentGame.this.displayBoard(_array, _pane); // This is to get the board created in WordFactory to show up
		WordamentGame.this.createRotateButton(); // This is to create the rotate button so we can rotate the board
		_wordFactory.checkAvailableWords(); // This is so that there is a list of available words ready to use - don't touch
		
		_mouseDraggedHandler = new MouseHandler(); // instantiate a new Mouse Handler (for whenever the mouse is dragged)
		_mouseReleasedHandler = new MouseReleasedHandler(); // instantiate a new Mouse Released Handler (for whenever mouse is released)
		_pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDraggedHandler); // add event listener
		_pane.setFocusTraversable(true); // this is so that the focus is always on the game pane
		
		// This is to instantiate needed instance variables		
		_visited = new Boolean[4][4];
		
		// Set the visited boolean as all false to prepare for the checking of the available words later
		for (int x = 0; x < 4; x ++) {
			for (int y=0; y<4; y ++) {
				_visited[x][y] = false;
			}
		}
		
		// This is to get the current score for both the computer and the player
		_currentScore = 0;
		_currentComputerScore = 0;
		// This is the instantiation of the HashSet of achievedWords in order to display the achieved words later
		/////////// I WILL: Comment in the README 
		_achievedWords = new LinkedList<String>();
		_achievedWordsHashSet = new HashSet<String>();
		// This is the current string stack and the required which allows undo and which allows tracking the position
		// of the mouse in relation with buttons
		//// BASICALLY: In order to be able to Re-do - Will you consider this a Bell and Whistle?
		/////////// I WILL: Comment on the use of these three different stacks later on the README
		_currentStringStack = new LinkedList<String>();
		_currentStringStackX = new LinkedList<Integer>();
		_currentStringStackY = new LinkedList<Integer>();
		_exitedAWord = false;
		_visitedAWord = false;
		// The necessary instance variables to keep track of the computer mode
		_gameIsPlaying = true;
		_computerIsPlaying = false;
		_playerIsPlaying = true;
		_computerSwitchedOff = true;
		// Instantiate a new "Computer Player" the game
		_computer = new ComputerPlayer();
		
	}
	// Method to get the pane for display
	public Pane getRoot() {
		return _pane;
	}
	
	/////////////////////////////////////////////// MASTER MOUSE HANDLER METHODS ////////////////////////////////////////////////
	
	// This is the class MouseHandler()
	private class MouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent e) {
				// Every time the mouse moves, currentX and currentY will be updated based on the location of the mouse
				double currentX = e.getSceneX();
				double currentY = e.getSceneY();
				for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x ++) {
					for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y++) {
						// This is to get the bounds of the current button that we loop through, and so that we could compare the
						// current mouse location with the bounds of all the buttons to see if there is any buttons that is being
						// dragged on or not
						double buttonX = _array[x][y].getXLocation();
						double buttonHigherX = _array[x][y].getXLocation() + Constants.WORD_SQUARE_SIZE;
						double buttonY = _array[x][y].getYLocation();
						double buttonHigherY = _array[x][y].getYLocation() + Constants.WORD_SQUARE_SIZE;
						// If the mouse location is in one button
						if (currentX <= buttonHigherX && currentX >= buttonX && currentY >= buttonY && currentY <= buttonHigherY) {
							// It will update the position of the current button that is being visited, so it will have
							// the data to compare so the computer will know if the mouse leave a button or not - I will indicate
							// when this data is used later
							_currentButtonX = buttonX;
							_currentButtonHigherX = buttonHigherX;
							_currentButtonY = buttonY;
							_currentButtonHigherY = buttonHigherY;
							// This method below is to add the array locations of the arrays we visited, so that in case we need
							// to undo later, we can pop from the stack and unvisit the button, or to compare the new locations
							// with the previously visited location in the stack
							if (_currentStringStackX.size() == 0 && _currentStringStackY.size() == 0) {
								_currentStringStackX.addFirst(x);
								_currentStringStackY.addFirst(y);
							}
							// There we go - one application of the StackX and StackY - This is going to check if the newest visited
							// button location is adjacent to the previous one
							
							// If it is adjacent, then we go on to check for other stuff!
							if (WordamentGame.this.checkAdjacent(_currentStringStackX.getFirst(), _currentStringStackY.getFirst(), 
								x, y) == true) {
							// If the button visited is false, then it will be visited!!							
								if (_visited[x][y] == false) {
									if (_visitedAWord == false) {
										_array[x][y].getRoot().setStyle(Constants.SELECTED_BUTTON_COLOR);
										_currentStringStack.addFirst(_array[x][y].getRoot().getText());
										_exitedAWord = false;
										_visited[x][y] = true;
										_visitedAWord = true;
										_currentStringStackX.addFirst(x);
										_currentStringStackY.addFirst(y);
									}
									// Or, if it is already visited
								} else {
									// Then, if the word earlier exited a word, and the stack size != 0 (to prevent noElementException, and
									// meaning that there was a word earlier!)
									if (_visited[x][y] == true && _exitedAWord == true && _currentStringStack.size() != 0) {	
										// Because it visited so exited a word is false and visited a word is true 
										// (until it really exits a word)
										_exitedAWord = false;
										// just to clarify: _visited[x][y] != from  _visitedAWord because you can still visit a word when
										// that word is already visited / highlighted / chosen (_visited[x][y] = true)
										_visitedAWord = true;
										// If the word hovering is the word recently visited then
										if (_array[x][y].getRoot().getText() == _currentStringStack.getFirst()) {
											// Basically redo - setting the color back to normal, remove the redone out of the done stacks,
											// and mark as unvisited so we can visit them again later
											_array[x][y].getRoot().setStyle(Constants.NORMAL_BUTTON_COLOR);
											_currentStringStack.removeFirst();
											_visited[x][y] = false;
											_currentStringStackX.removeFirst();
											_currentStringStackY.removeFirst();
										}
									}
								}
							}	
						} else {
							// If the mouse X is out of bounds of the most recent button - THIS IS WHERE THE INSTANCE VARIABLES ABOVE ARE USED - AS PROMISED
							if (currentX > _currentButtonHigherX || currentX < _currentButtonX || currentY < _currentButtonY || currentY > _currentButtonHigherY) {
							// Then its going to mark exitedAWord as true and visitedAWord as false
							_exitedAWord = true;
							_visitedAWord = false;
							}
						}
					}
				}
				// This is so that whenever the mouse is released after it is dragged, it will start the methods of analyzing the
				// words
				_pane.addEventHandler(MouseEvent.MOUSE_RELEASED, _mouseReleasedHandler);
		}
	}
	
	//// METHOD TO TELL WHAT TO DO WHEN THE MOUSE IS RELEASED
	private class MouseReleasedHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent e) {	
			_currentString = "";
			// This is to convert the letters in the current String Stack to one string that checks everything
			while (_currentStringStack.size() != 0) {
				// This is so that we can update the current string in order to be able to check it
				_currentString = _currentString + _currentStringStack.removeLast();
			}
			
			if (_currentString != "") {
				// If the current string is legitimate (check by the check library method) AND the string is not selected before
				if (_wordFactory.checkIfWordIsCorrect(_currentString) == true && _achievedWordsHashSet.contains(_currentString.toLowerCase()) == false) {
					WordamentGame.this.wordIsCorrectResponse();
				} else {
					// If the string is legitimate, BUT the string is already selected earlier
					if (_wordFactory.checkIfWordIsCorrect(_currentString) == true && _achievedWordsHashSet.contains(_currentString.toLowerCase()) == true) {
						WordamentGame.this.wordIsRepeatedResponse();
					}
					// If the string is completely not legitimate
					if (_wordFactory.checkIfWordIsCorrect(_currentString) == false) {
						WordamentGame.this.wordIsFalseResponse();
					}
				}
				
				// This two things below here basically created a count to allow for the delay before graphical responses are shown
				_countUp = 0;
				WordamentGame.this.setUpReturnTimeline();
			}
			
			// This is to remove all the things in the currentX and Y stack and get prepared for another round
			while (_currentStringStackX.size() != 0 && _currentStringStackY.size() != 0) {
				_currentStringStackX.removeFirst();
				_currentStringStackY.removeFirst();
			}
			
			// This is the end of the player's move. If the computers mode is on, then its going to allow the computer to make
			// the move. (And game is playing is true too - we dont want the computer to make any move when game stopped.
			if (_computerSwitchedOff == false && _gameIsPlaying == true) {
				WordamentGame.this.makeAComputerMove();
			}
			// Remove event handler of the mouse released handler so we don't get a bunch of unnecessary event handler
			_pane.removeEventHandler(MouseEvent.MOUSE_RELEASED, _mouseReleasedHandler);
		}
	}
	
	///////////////////////////////////////// END OF MASTER MOUSE HANDLER METHODS //////////////////////////////////////////
	
	
	
	/////////////////////////////////////////////// MASTER RESPONSE METHODS ////////////////////////////////////////////////
	
	/// THIS ReturnTimeline THAT WE SET UP IS GOING TO BE FOR GRAPHICAL EFFECT TO THE RESPONSES TO THE WORD INPUT
	private void setUpReturnTimeline() {
		KeyFrame kf = new KeyFrame(Duration.seconds(0.06), new ReturnHandler()); // I will not put this number in Constants because its too insignificant
		Timeline timeline = new Timeline(kf);
		timeline.setCycleCount(5); // So this will allow 0.3 seconds before the graphical responses disappear
		timeline.play();
	}
	
	/// The private class to tell everything to go back to normal after 0.3 seconds
	private class ReturnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			_countUp = _countUp + 0.06;
			// When its 0.3 seconds (thats when _countUp is 0.3 as well)
			if (_countUp == 0.3) {
				// Set all the squares back to normal
				for (int x = 0; x < 4; x ++) {
					for (int y = 0; y < 4; y ++) {
						if (_visited[x][y] = true) {
							_array[x][y].getRoot().setStyle(Constants.NORMAL_BUTTON_COLOR);
							_visited[x][y] = false;
							_currentString = "";
						}
					}
				}
			}
			
		}
	}

	/// Method to respond when word is correct
	private void wordIsCorrectResponse() {
		// Basically logically update current score in this game, and tell the SideBar to update the score
		_currentScore = _currentScore + _wordFactory.checkScoreOfWord(_currentString);
		WordamentGame.this.tellSideBarToUpdateScore(_currentScore);
		// Basically add the word to the two lists of achieved words for two different purposes (README)
		_achievedWords.addFirst(_currentString);
		_achievedWordsHashSet.add(_currentString.toLowerCase());
		SideBar.updateListOfAchievedWords();
		// Going to set all the color to green
		while (_currentStringStackX.size() != 0 && _currentStringStackY.size() != 0) {
			_array[_currentStringStackX.removeFirst()][_currentStringStackY.removeFirst()].getRoot().setStyle(Constants.CORRECT_BUTTON_COLOR);
		}
	}
	
	/// Method to respond when word is false
	private void wordIsFalseResponse() {
		// Going to set all the color to red
		while (_currentStringStackX.size() != 0 && _currentStringStackY.size() != 0) {
			_array[_currentStringStackX.removeFirst()][_currentStringStackY.removeFirst()].getRoot().setStyle(Constants.INCORRECT_BUTTON_COLOR);
		}
	}
	
	/// Method to respond when word is repeated
	private void wordIsRepeatedResponse() {
		// Going to set all the color to yellow
		while (_currentStringStackX.size() != 0 && _currentStringStackY.size() != 0) {
			_array[_currentStringStackX.removeFirst()][_currentStringStackY.removeFirst()].getRoot().setStyle(Constants.REPEATED_BUTTON_COLOR);
		}
	}
	
	///////////////////////////////////////////// END OF MASTER RESPONSE METHODS ///////////////////////////////////////////
	
	////METHOD TO TELL THE SIDEBAR TO UPDATE SCORE
	private void tellSideBarToUpdateScore(int currentScore) {
		SideBar.updateScore(currentScore);
	}
	
	///// METHOD TO GET LINKEDLIST FOR DISPLAY
	public static LinkedList<String> getAchievedWords() {
		return _achievedWords;
	}
	
	///// METHOD TO STOP EVERYTHING FROM WORKING - PAUSE OR GAME OVER - done - do not touch
	public static void pauseOrUnpause() {
		if (_gameIsPlaying == true) {
			_rotateButton.getRoot().setDisable(true);
			_pane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDraggedHandler);
			_gameIsPlaying = false;
			for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x ++) {
				for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y ++) {
					_array[x][y].getRoot().setDisable(true);
				}
			}
		} else {
			_gameIsPlaying = true;
			_rotateButton.getRoot().setDisable(false);
			_pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDraggedHandler);
			for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x ++) {
				for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y ++) {
					_array[x][y].getRoot().setDisable(false);
				}
			}
		}
	}

	//// METHOD TO CHECK IF THE NEW WORD IS ADJACENT TO THE OLD WORD OR NOT
	private boolean checkAdjacent(int oldX, int oldY, int newX, int newY) {
		// I use this method so that when the current one is adjacent, it will increase so that the method would return
		// true, and if it does not decrease it will return false
		int isAdjacent = 0;
		// Looping through the surrounding squares to see if the current one is adjacent or not
		for (int x = oldX - 1; x <= oldX + 1; x ++) {
			for (int y = oldY - 1; y <= oldY + 1; y ++) {
				// Conditions to make sure that the loop doesnt go out of bound
				if (x >= 0 && x < Constants.BOARD_ARRAY_SIZE && y >= 0 && y < Constants.BOARD_ARRAY_SIZE) {
					if (x == newX && y == newY) {
						isAdjacent = isAdjacent + 1;
					}
				}
			}
		}
		
		// Returning the results
		if (isAdjacent > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// This method is to take the created array to put it to the display board -- Done, dont touch
	private void displayBoard(WordButton[][] wordButtonArray, Pane pane) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				pane.getChildren().addAll(wordButtonArray[x][y].getRoot());
			}
		}
	}
	
	/////////////////////////////////////////////// MASTER ROTATE METHOD ////////////////////////////////////////////////
	
	///// METHOD TO CREATE ROTATE BUTTON - FINALIZED - DO NOT TOUCH
	private void createRotateButton() {
		_rotateButton = _wordFactory.createRotateButton();
		_rotateButton.getRoot().setOnAction(new RotateHandler());
		_pane.getChildren().add(_rotateButton.getRoot());
	}
	
	///// METHOD TO MAKE THE ROTATE BUTTON CALL THE DISPLAY ROTATE BOARD METHOD - DO NOT TOUCH
	private class RotateHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			WordamentGame.this.displayRotateBoard(_array, _pane);
		}
	}
	
	///// METHOD TO MAKE THE BOARD ROTATE GRAPHICALLY (DELETE ALL ONE, ROTATE LOGICALLY, CREATE NEW ONE) - DO NOT TOUCH
	private void displayRotateBoard(WordButton[][] oldButtonArray, Pane pane) {
		// Method to create the old board off the pane
		for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x++) {
			for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y++) {
				pane.getChildren().remove(_array[x][y].getRoot());
			}
		}
		_array = _wordFactory.rotateBoard(_array); // Method to logically replace the current array with new array (rotated)
		_wordFactory.setWordButtonArrayToPosition(_array); // Method to put the new array into its location
		// Method to add newly created board to the pane again
		for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x++) {
			for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y++) {
				pane.getChildren().add(_array[x][y].getRoot());
			}
		}
	}
	
	/////////////////////////////////////////// END OF MASTER ROTATE METHOD ////////////////////////////////////////////
	
	
	
	///////////////////////////////////////////////// COMPUTER METHOD //////////////////////////////////////////////////
	
	// This is to turn on and off the computer mode
	public static void turnOnOrOffComputer() {
		if (_computerSwitchedOff == false) {
			_computerSwitchedOff = true;
		} else {
			if (_computerSwitchedOff == true) {
				_computerSwitchedOff = false;
			}
		}
	}
	
	// This basically is enabled when _computerSwitchOff is false, after a player makes a move
	public void makeAComputerMove() {
		// This is to disable the functionalities of the player (logically)
		SideBar.updatePlayerAndComputerInfo();
		// This is to update the current game situation
		_computerIsPlaying = true;
		_playerIsPlaying = false;
		// This is to remove the mouse handler so that user cannot play when the computer is playing
		_pane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDraggedHandler);
		// This is to graphically notify the player that its not their move
		SideBar.changePlayerNotificationStatus();
		// This is to let the computer make a move
		WordamentGame.this.createWaitForComputerMoveTimeline();
	}
		// This is to create the timeline that basically allowed for the wait of 2.5 seconds before the computer
		// makes a move
		private void createWaitForComputerMoveTimeline() {
			KeyFrame kf = new KeyFrame(Duration.seconds(0.5), new waitForComputerMoveHandler());
			Timeline timeline = new Timeline(kf);
			timeline.setCycleCount(5);
			_waitingForComputerCount = 0; // Again, the use of counts to let it count 2.5 seconds before it takes any actions
			timeline.play();
		}
		
		private class waitForComputerMoveHandler implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				_waitingForComputerCount = _waitingForComputerCount + 0.5;
				if (_waitingForComputerCount == 2.5) {
					// The computer will logically make a move. If I had more time for bells and whistles I would make the
					// computer moves be graphically shown, but I can't now...
					WordamentGame.this.computerLogicallyMakeAMove();
					// After the computer made a move, these two methods allow the use to play again
					_pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDraggedHandler);
					SideBar.updatePlayerAndComputerInfo();
				}
			}
		}
		
		/* Basically this method allows the current list of achieved words to update the list of achieved words already
		 * to graphically display for the player which word has been played, as well as so that player cannot play the word
		 * that was already played by the computer and vice versa
		 */
		private void computerLogicallyMakeAMove() {
			String theWord = _computer.getNextWord();
			// Making sure below that the computer does not play the words that was played by the user
			while (_achievedWordsHashSet.contains(theWord.toLowerCase()) == true) {
				theWord = _computer.getNextWord();
			}
			// Adding the achieved word to the two lists for back end processing and for displaying to the user
			_achievedWordsHashSet.add(theWord.toLowerCase());
			_achievedWords.addFirst(theWord);
			// Graphically updating the score and show the user what the computer word was
			_currentComputerScore = _currentComputerScore + _wordFactory.checkScoreOfWord(theWord);
			SideBar.updateComputerScore(_currentComputerScore);
			SideBar.updateListOfAchievedWords();
		}
	
		///////////////////////////////////////////// END OF COMPUTER METHOD //////////////////////////////////////////////
	
}
