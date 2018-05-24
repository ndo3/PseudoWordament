package Wordament;



public class Constants {
	/////////////// General Constants ///////////////
	public static final double FRAME_WIDTH = 860;
	public static final double GAME_WIDTH = 560;
	public static final double FRAME_HEIGHT = 560;
	public static final double WORD_SQUARE_SIZE = 80;	
	public static final int DICTIONARY_SIZE = 479000;
	public static final double ROTATE_SQUARE_SIZE = 40;
	
	/////////////// Constants in Dictionary Class ///////////////
	public static final int MAX_FIRST_WORD_LENGTH = 15;
	public static final int MIN_FIRST_WORD_LENGTH = 8;
	
	/////////////// Constants in SideBar class ///////////////
	public static final double V_GAP_GRID_PANE = 12;
	public static final double SIDEBAR_WIDTH = 300;
	public static final String SIDEBAR_BUTTON_COLOR = "-fx-background-color: #e0b774;";
	public static final String SIDEBAR_COLOR = "-fx-background-color: #336c9b;";
	public static final Integer STARTING_SECONDS = 90;
	public static final double LISTVIEW_HEIGHT = 150;
	public static final double LISTVIEW_WIDTH = 200;
	public static final Integer LISTVIEW_POSITION = 11;	
	public static final int TIMELINE_SINGULAR_DURATION = 1;
	
	/////////////// Constants in the WordFactory class ///////////////
	public static final Integer BOARD_ARRAY_SIZE = 4;
	public static final Integer NUMBER_OF_CHARACTERS = 16;
		// This is to differentiate the different levels of words found!
	public static final Integer WORD_LENGTH_LEVEL_ONE = 4;
	public static final Integer WORD_LENGTH_LEVEL_TWO = 8;
	public static final Integer SCORE_LEVEL_ONE = 20;
	public static final Integer SCORE_LEVEL_TWO = 35;
	public static final Integer SCORE_LEVEL_THREE = 69;
		// This is to determine the coordinates of the squares of the board
	public static final double[][] WORD_BOARD_COORDINATES = {{Constants.WORD_SQUARE_SIZE*1, Constants.WORD_SQUARE_SIZE*1}, {Constants.WORD_SQUARE_SIZE*1, Constants.WORD_SQUARE_SIZE*2 + 20}, {Constants.WORD_SQUARE_SIZE*1, Constants.WORD_SQUARE_SIZE*3 + 40}, {Constants.WORD_SQUARE_SIZE*1, Constants.WORD_SQUARE_SIZE*4 + 60}, 
			{Constants.WORD_SQUARE_SIZE*2 + 20, Constants.WORD_SQUARE_SIZE*1}, {Constants.WORD_SQUARE_SIZE*2 + 20, Constants.WORD_SQUARE_SIZE*2 + 20}, {Constants.WORD_SQUARE_SIZE*2 + 20, Constants.WORD_SQUARE_SIZE*3 + 40}, {Constants.WORD_SQUARE_SIZE*2 + 20, Constants.WORD_SQUARE_SIZE*4 + 60}, 
			{Constants.WORD_SQUARE_SIZE*3 + 40, Constants.WORD_SQUARE_SIZE*1}, {Constants.WORD_SQUARE_SIZE*3 + 40, Constants.WORD_SQUARE_SIZE*2 + 20}, {Constants.WORD_SQUARE_SIZE*3 + 40, Constants.WORD_SQUARE_SIZE*3 + 40}, {Constants.WORD_SQUARE_SIZE*3 + 40, Constants.WORD_SQUARE_SIZE*4 + 60}, 
			{Constants.WORD_SQUARE_SIZE*4 + 60, Constants.WORD_SQUARE_SIZE*1}, {Constants.WORD_SQUARE_SIZE*4 + 60, Constants.WORD_SQUARE_SIZE*2 + 20}, {Constants.WORD_SQUARE_SIZE*4 + 60, Constants.WORD_SQUARE_SIZE*3 + 40}, {Constants.WORD_SQUARE_SIZE*4 + 60, Constants.WORD_SQUARE_SIZE*4 + 60}};
	public static final double[][] ROTATE_SQUARE_COORDINATES = {{Constants.WORD_SQUARE_SIZE*4 + 60 + 80 + 20, Constants.WORD_SQUARE_SIZE*4 + 60 + 40}};
	// This is the list of alphabets that would be helpful when I create the board and need to fill in miscellaneous cells that
	// stand on their own in the create board method
	public static final String[] ALPHABET = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z"};
	
	// Constants in WordamentGame class
	public static final String NORMAL_BUTTON_COLOR = "-fx-background-color: #FF8C00;";
	public static final String SELECTED_BUTTON_COLOR = "-fx-background-color: #ffffff;";
	public static final String CORRECT_BUTTON_COLOR = "-fx-background-color: #90ed82;";
	public static final String INCORRECT_BUTTON_COLOR = "-fx-background-color: #ed8282;";
	public static final String REPEATED_BUTTON_COLOR = "-fx-background-color: #ffea82;";
	
	
	// Constants in PaneOrganizer class
	public static final String GAME_PANE_COLOR = "-fx-background-color: #4682B4;";
	
	// Constants in CheatSheetPaneOrganizer class
	public static final String CHEAT_SHEET_COLOR = "-fx-background-color: #ffffff;";
}
