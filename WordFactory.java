package Wordament;

import java.io.IOException;
import javafx.scene.control.Button;
import java.lang.Character;
import java.util.*;

/*(5) WordFactory: If the WordamentGame is the skin and bone and muscles, this is the brain of the game - it
basically deals with everything logical about the program, from creating the board of 16 characters (this is
a large large chunk of the program), to handling the logic behind rotating the board (will comment later), to
checking the results from the input from user, to checking the entire board for which words are available in
general. Used depth first search, and /randomized/ depth first search.
 */
public class WordFactory {

	private WordButton[][] _array;
	private WordButton[][] _board; // Tis the official one
	private WordButton[][] _visitedButton;
	// The difference between _queueX, _queueY and _queueSecondaryX and _queueSecondaryY is that
	// _queueX and _queueY is used in creating the first word, and _queueSecondaryX and _queueSecondaryY
	// is used in creating the remaining words
	private LinkedList<Integer> _queueX;
	private LinkedList<Integer> _queueY;
	private LinkedList<Integer> _queueSecondaryX;
	private LinkedList<Integer> _queueSecondaryY;
	private LinkedList<Integer> _listOfRemainingX;
	private LinkedList<Integer> _listOfRemainingY;
	private Dictionary _dictionary;
	private String[][] _listCharacters;
	private Boolean[][] _visited;
	private Boolean[][] _visitedButtonBoolean;
	private static HashSet<String> _availableWords;
	private String _checkString;
	private int _wordCreated;
	private String _firstWord;

	private int _totalIncremented;
	private int _wordLength;
	
	public WordFactory() throws IOException {
		_dictionary = new Dictionary();
		_array = new WordButton[Constants.BOARD_ARRAY_SIZE][Constants.BOARD_ARRAY_SIZE];
		_listCharacters = new String[Constants.BOARD_ARRAY_SIZE][Constants.BOARD_ARRAY_SIZE];
		_visited = new Boolean[Constants.BOARD_ARRAY_SIZE][Constants.BOARD_ARRAY_SIZE];
		_availableWords = new HashSet<String>();
		_checkString = "";
		_wordCreated = 0;
		_firstWord = Dictionary.createFirstWord();
	}
		
		/////////////////////////////////////////////// MASTER BOARD CREATOR //////////////////////////////////////////////////
	
	///// METHOD TO WRITE: CREATE BOARD - BREADTH FIRST SEARCH
		public WordButton[][] createBoard() {
			_board = new WordButton[Constants.BOARD_ARRAY_SIZE][Constants.BOARD_ARRAY_SIZE];
			//Method to create the location for the first word to start
			int startingX = (int)(Math.random()*Constants.BOARD_ARRAY_SIZE);
			int startingY = (int)(Math.random()*Constants.BOARD_ARRAY_SIZE);
			// These two methods update the _queueX and _queueY that gives the positions to the squares
			WordFactory.this.prepareTocheckCreatePath(_board);
			WordFactory.this.checkCreatePath(startingX, startingY, _firstWord.length());
			WordFactory.this.createSquares(_queueX, _queueY, _board, _firstWord);
			WordFactory.this.checkForEmptySquares(_board);
			WordFactory.this.doStuffWithBlankSpaces(_board, _listOfRemainingX.removeFirst(), _listOfRemainingY.removeFirst());
			while (_listOfRemainingX.size() != 0 && _listOfRemainingY.size() != 0) {
				WordFactory.this.checkForEmptySquares(_board);
				WordFactory.this.doStuffWithBlankSpaces(_board, _listOfRemainingX.removeFirst(), _listOfRemainingY.removeFirst());
			}

			for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x ++) {
				for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y ++) {
					_listCharacters[x][y] = _board[x][y].getRoot().getText();
				}
			}
			WordFactory.this.setWordButtonArrayToPosition(_board);
			return _board;
		}
				//// Method to prepare stuff before create Path
				private void prepareTocheckCreatePath(WordButton[][] existingArray) {
					_wordCreated = 0;
					_totalIncremented = 0;
					_visitedButton = new WordButton[Constants.BOARD_ARRAY_SIZE][Constants.BOARD_ARRAY_SIZE];
					// These two queues are going to be the queues 
					_queueX = new LinkedList<Integer>();
					_queueY = new LinkedList<Integer>();
					_visitedButtonBoolean = new Boolean[Constants.BOARD_ARRAY_SIZE][Constants.BOARD_ARRAY_SIZE];
						// Method to make sure the _visitedButtonBoolean gets the true value from the board - extensibility
						for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x ++) {
							for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y ++) {
								if (existingArray[x][y] == null) {
									_visitedButtonBoolean[x][y] = false;
								} else {
									_visitedButtonBoolean[x][y] = true;
								}
							}
						}
					
					_wordLength = _firstWord.length();
					for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x ++) {
						for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y ++) {
							_visitedButtonBoolean[x][y] = false;
						}
					}
				}
				
				//// Method to create Path - randomized depth first search basically
				private void checkCreatePath(int startingX, int startingY, int wordLength) {
					// This is to basically recursively add a new one to the queue IF it is in the table
					if (startingX >= 0 && startingX <= 3 && startingY >= 0 && startingY <= 3) {
						LinkedList<Integer> conservingQueueX = new LinkedList<Integer>();
						LinkedList<Integer> conservingQueueY = new LinkedList<Integer>();
						// This is to conserve the old queue while doing stuff with the new queue to try out stuff
						conservingQueueX = _queueX;
						conservingQueueY = _queueY;
						// This is to add the new location to the queue
						_queueX.addFirst(startingX);
						_queueY.addFirst(startingY);
						// This is to mark the place as visited
						_visitedButtonBoolean[startingX][startingY] = true;
						// This is to keep track of the number of words created so that it does not over create
						_wordCreated = _wordCreated + 1;
					// This is so that whenever the word creation reaches its limit its not going to continue
					// the method so that its not getting repetitive
					if (wordLength == 1 || _wordCreated == _wordLength) {
						
					} else {
						// The assignment of -1 is so that it will automatically generate - extensibility
						int a = -1;
						int b = -1;
						// The use of surroundingCount is so that if all the words around it are occupied the method
						// later is going to break the loop out and restart the whole thing all over
						int surroundingCount = 0;
						if (wordLength > 1) {
							// So that while it randomizes to a position out of the board or occupied it randomizes to another one
							while ((a < 0 || b < 0 || a > 3 || b > 3 || _visitedButtonBoolean[a][b] == true) && surroundingCount < 9) {//&& _wordCreated < _wordLength - 1) {
								// Randomizes a new one
								a = (int)(Math.random()*3 + startingX - 1);
								b = (int)(Math.random()*3 + startingY - 1);
								// update the surrounding count so whenever it reaches a point it has to restart
								surroundingCount = surroundingCount + 1;
									// The method to restart the whole thing if it tries too much
									if (surroundingCount > 12) {
										WordFactory.this.createBoard();
									}
							}
							// This method is to recursively continue
							if (_wordCreated < _wordLength) {
							WordFactory.this.checkCreatePath(a, b, wordLength - 1);
							}
						}
					}
						// So as to preserve everything
						_queueX = conservingQueueX;
						_queueY = conservingQueueY;
					}

				}

				// This method is to create the squares created given a list of locations to create the squares, and the word
				private void createSquares(LinkedList<Integer> xLocations, LinkedList<Integer> yLocations, WordButton[][] board, String string) {
					String[] stringArray = string.split("", 0);
					int i = 0;
						while ((xLocations.size() != 0) && (yLocations.size() != 0)) {
							board[xLocations.removeLast()][yLocations.removeLast()] = new WordButton(stringArray[i].toUpperCase());
							i = i + 1;
						}

					
				}
				
				// This method is to create a linked list of the positions that still needs to be filled

				private void checkForEmptySquares(WordButton[][] board) {
					_listOfRemainingX = new LinkedList<Integer>();
					_listOfRemainingY = new LinkedList<Integer>();
					int fullCount = 0;
					for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x++) {
						for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y++) {
							if (board[x][y] == null) {
								_listOfRemainingX.addFirst(x);
								_listOfRemainingY.addFirst(y);
								_visitedButtonBoolean[x][y] = false;
							} else {
								_visitedButtonBoolean[x][y] = true;
							}
						}
					}

				}
				
				// This method is to check the corners of the board and do stuff with it after creating the
				// first word - Again, I basically create a completely new method because things needed to be
				// separate and I wanted clarity
				private void doStuffWithBlankSpaces(WordButton[][] board, int startingX, int startingY) {
					// Basically creation of the secondary queues in order to extensibly create new words for multiple times
					// So the program will get the queue of where to create the words, and then with the word, create it
					_queueSecondaryX = new LinkedList<Integer>();
					_queueSecondaryY = new LinkedList<Integer>();
					// This is instantiate an instance variable to count how many words have been created in the method
					// checkSecondaryNumberAndPath later - if checkSecondaryNumberAndPath creates a new character, it will
					// increment wordCreated by one
					_wordCreated = 0;
					Integer newWordLength = WordFactory.this.checkSecondaryNumberAndPath(startingX, startingY);
					if (newWordLength <= 2 && newWordLength != 0) { // This is because its better to random words that only have 1 or 2 characters...
						while (_queueSecondaryX.size() != 0 && _queueSecondaryY.size() != 0) {
							int i = (int)(Math.random()*25); // Randomizing the word that is going to be created
							board[_queueSecondaryX.removeFirst()][_queueSecondaryY.removeLast()] = new WordButton(Constants.ALPHABET[i]);
						}
					} else {
						// If we can create words that consist of 3 characters or more, then its going to search the library
						// for that word 
						WordFactory.this.createSquares(_queueSecondaryX, _queueSecondaryY, _board, Dictionary.createWordGivenLength(newWordLength));
					}
					// This method is to always, constantly let the program know the locations that are still available
					WordFactory.this.checkForEmptySquares(_board);
				}
				
				// This method is similar to the first method, but used to GET THE NUMBERS OF THE WORDS THAT
				// can be created using one specific spot
				// I could try to factor out code, but I think that for the sake of understandability and clarity
				// of code for myself its better to just 
				private int checkSecondaryNumberAndPath(int startingX, int startingY) {
					// This is to basically recursively add a new one to the queue IF it is in the table
					if (startingX >= 0 && startingX <= 3 && startingY >= 0 && startingY <= 3 && _visitedButtonBoolean[startingX][startingY] == false) {
						LinkedList<Integer> conservingQueueX = new LinkedList<Integer>();
						LinkedList<Integer> conservingQueueY = new LinkedList<Integer>();
						// This is to conserve the old queue while doing stuff with the new queue to try out stuff
						conservingQueueX = _queueSecondaryX;
						conservingQueueY = _queueSecondaryY;
						// This is to add the new location to the queue
						_queueSecondaryX.addFirst(startingX);
						_queueSecondaryY.addFirst(startingY);
						// This is to mark the place as visited
							_visitedButtonBoolean[startingX][startingY] = true;
						
						// This is to keep track of the number of words created so that it does not over create
						_wordCreated = _wordCreated + 1;
						
						// The assignment of -1 is so that it will automatically generate - extensibility
						int a = -1;
						int b = -1;
						// The use of surroundingCount is so that if all the words around it are occupied the method
						// later is going to break the loop out and restart the whole thing all over
						int surroundingCount = 0;	
							// So that while it randomizes to a position out of the board or occupied it randomizes to another one
							while ((a < 0 || b < 0 || a > 3 || b > 3 || _visitedButtonBoolean[a][b] == true) && surroundingCount < 9) {//&& _wordCreated < _wordLength - 1) {
								// Randomizes a new one
								a = (int)(Math.random()*3 + startingX - 1);
								b = (int)(Math.random()*3 + startingY - 1);
								// update the surrounding count so whenever it reaches a point it has to restart
								surroundingCount = surroundingCount + 1;
									// The method to restart the whole thing if it tries too much
									if (surroundingCount > 20) {
										return _wordCreated;
									}
							}
							// This method is to recursively continue
							WordFactory.this.checkSecondaryNumberAndPath(a, b);
							_queueSecondaryX = conservingQueueX;
							_queueSecondaryY = conservingQueueY;
							_visitedButtonBoolean[startingX][startingY] = false;
						}
					return _wordCreated;
				}
				
				
		
	////// HELPER METHOD: METHOD TO SET THE WORD BUTTONS INTO ITS POSITION - DO NOT TOUCH!!!
		public void setWordButtonArrayToPosition(WordButton[][] wordButtonArray) {
			int i = 0;
			for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x++) {
				for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y++) {
					wordButtonArray[x][y].setLocation(Constants.WORD_BOARD_COORDINATES[i][0], Constants.WORD_BOARD_COORDINATES[i][1]);

					i++;				
				}
			}
		}
		
		////////////////////////////////////////////// END OF MASTER BOARD CREATOR ///////////////////////////////////////////
		
		
		
		
		/////////////////////////////////////////////// MASTER ROTATE METHOD ////////////////////////////////////////////////

		
	///// METHOD TO CREATE ROTATE BUTTON AND SEND IT TO WORDAMENT GAME SO IT CAN SHOW UP ON WORDAMENT GAME - RETURN A WordButton - DONE - DONT TOUCH
	public WordButton createRotateButton() {
		WordButton rotateButton = new WordButton("Rotate");
		rotateButton.getRoot().setMinSize(Constants.ROTATE_SQUARE_SIZE, Constants.ROTATE_SQUARE_SIZE);
		rotateButton.getRoot().setMaxSize(Constants.ROTATE_SQUARE_SIZE, Constants.ROTATE_SQUARE_SIZE);
		rotateButton.setLocation(Constants.ROTATE_SQUARE_COORDINATES[0][0], Constants.ROTATE_SQUARE_COORDINATES[0][1]);
		return rotateButton;
	}
	
	/////// METHOD TO ROTATE THE BOARD - RETURN AN ARRAY TO COMMUNICATE WITH WordamentGame - DONE! - DON'T TOUCH
		public WordButton[][] rotateBoard(WordButton[][] array) {
			WordButton[][] fakeRotateArray = new WordButton[Constants.BOARD_ARRAY_SIZE][Constants.BOARD_ARRAY_SIZE]; // Creating a fakeButtonArray to rotate without deleting any of the current elements
			int layer = 0; //// The comment on the use of layer will be explained in README
			while (layer < 2) { // A 4x4 array only has two layers, one outside and one inside
				int a = layer;
				int b = 3 - layer;
				for (int i = a; i < b; i++) {
					// I will explain this math later in the README
					fakeRotateArray[a+b-i][a] = array[a][i];
					fakeRotateArray[a][i] = array[i][b];
					fakeRotateArray[i][b] = array[b][a+b-i];
					fakeRotateArray[b][a+b-i] = array[a+b-i][a];
				}
				layer = layer + 1; // Allow to move on to the next layer
			}
			return fakeRotateArray;
		}
		
		///////////////////////////////////////////////// END OF MASTER ROTATE METHOD ////////////////////////////////////////////////
		
		
		
	
		/////////////////////////////////////////////////// MASTER WORD CHECKER /////////////////////////////////////////////////////

	// MASTER METHOD (helper 1 + 2): METHOD FOR DIFFERENT PLACES TO CALL TO CHECK WHAT IS THE AVAILBLE WORDS
	public void checkAvailableWords() {		
		WordFactory.this.prepareToCreateListOfAvailableWords(_visited, _checkString); //call this to get ready for creating list of available words
		for (int x = 0; x < Constants.BOARD_ARRAY_SIZE; x++) {
			for (int y = 0; y < Constants.BOARD_ARRAY_SIZE; y++) {
				WordFactory.this.createListOfAvailableWords(_listCharacters, _visited, x, y, _checkString);
			}
		}
	}
	
					////// HELPER METHOD (1): METHOD TO PREPARE NECESSARY BOOLEAN ARRAYS AND STRING FOR CREATING LIST OF AVAILABLE WORDS
					// (it has to begin with an empty string and visit status of everyone is false)
					private void prepareToCreateListOfAvailableWords(Boolean[][] visitStatus, String beginningString) {
						for (int a = 0; a < Constants.BOARD_ARRAY_SIZE; a++) {
							for (int b = 0; b < Constants.BOARD_ARRAY_SIZE; b++) {
								visitStatus[a][b] = false;
							}
						}
						beginningString = "";
					}
	
					////// HELPER METHOD (2): THE METHOD TO CHECK WHAT THE AVAILABLE WORDS ARE GIVEN AN ARRAY OF CHARACTERS - DO NOT TOUCH!!!
					private void createListOfAvailableWords(String[][] listCharacters, Boolean[][] visitedOrNot, int a, int b, String initialString) {
						// This is to conserve the initial string to be able to check for different adjacent places, like AKJ and AKD and AKE
						String conservingString = initialString;
						initialString = initialString + listCharacters[a][b];		
						// This is to check if the word is in the dictionary or not, and if yes - print it out and add it to a hash set
						if (_dictionary.checkContainWord(initialString.toLowerCase()) == true && initialString.length() > 2) {
							_availableWords.add(initialString.toLowerCase()); // this is the problem
						}
						visitedOrNot[a][b] = true; // to make sure that it does not add itself
						// The recursive loop in order to continue the word check to the other adjacent characters
						for (int x = a-1; x<a+2; x++) {
							for (int y=b-1; y<b+2; y++) {		
								if (x >= 0 && y >= 0 && x < Constants.BOARD_ARRAY_SIZE && y < Constants.BOARD_ARRAY_SIZE && visitedOrNot[x][y] == false) { // so that only adds the adjacent ones
									WordFactory.this.createListOfAvailableWords(listCharacters, visitedOrNot, x, y, initialString);
								}	
							}
						}
						initialString = conservingString;
						visitedOrNot[a][b] = false;
					}
	
		/////////////////////////////////////////////// END OF MASTER WORD CHECKER //////////////////////////////////////////////////

					
					
					
		////////////////////////////////////////////////// MASTER RESULT CHECKER ////////////////////////////////////////////////////
	
	// Method to get the list (already created) for the sake of displaying in the cheat sheet
	public static HashSet<String> getListOfAvailableWords() {
		return _availableWords;
	}
	
	// METHOD TO CHECK IF WORD INPUT FROM USER IS IN ALL THE AVAILABLE WORD LIST OR NOT
	public boolean checkIfWordIsCorrect(String inputString) {
		return _availableWords.contains(inputString.toLowerCase()); // try to change this to available word
	}
	
	
	///// METHOD TO DEFINE WHAT SCORE A WORD GETS - FINALIZED - DON'T TOUCH
	public int checkScoreOfWord(String word) {
		int wordLength = word.length();
		int score = 0;
		if (wordLength <= Constants.WORD_LENGTH_LEVEL_ONE) {
			score = Constants.SCORE_LEVEL_ONE;
		}
		if (wordLength > Constants.WORD_LENGTH_LEVEL_ONE && wordLength < Constants.WORD_LENGTH_LEVEL_TWO) {
			score = Constants.SCORE_LEVEL_TWO;
		}
		if (wordLength >= Constants.WORD_LENGTH_LEVEL_TWO) {
			score = Constants.SCORE_LEVEL_THREE;
		}
		return score;
	}
	
	///////////////////////////////////////////// END OF MASTER RESULT CHECKER //////////////////////////////////////////////////
}
