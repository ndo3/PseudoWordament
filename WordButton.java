package Wordament;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

/*(6) WordButton: Basically, it creates a type of Button that has more cool effects in it. These WordButtons
will be used in creating the board.
 */

public class WordButton {

	private Rectangle _wordSquare;
	private Button _wordButton;
	// Method to create the WordButtons with an existing size, color and given text
	public WordButton(String string) {
		_wordSquare = new Rectangle(Constants.WORD_SQUARE_SIZE, Constants.WORD_SQUARE_SIZE);
		_wordSquare.setFill(Color.DARKORANGE);
		_wordButton = new Button();
		_wordButton.setText(string);
		_wordButton.setShape(_wordSquare);
		_wordButton.setStyle("-fx-background-color: #FF8C00;");
		_wordButton.setMinSize(Constants.WORD_SQUARE_SIZE, Constants.WORD_SQUARE_SIZE);
		_wordButton.setMaxSize(Constants.WORD_SQUARE_SIZE, Constants.WORD_SQUARE_SIZE);
		
	}
	
	public Button getRoot() {
		return _wordButton;
	}
	// Method to extensibly set the location of the buttons
	public void setLocation(double x, double y) {
		_wordButton.setLayoutX(x);
		_wordButton.setLayoutY(y);
	}
	// Method to get the location of the buttons
	public double getXLocation() {
		return _wordButton.getLayoutX();
	}
	// Method to get the location of the buttons
	public double getYLocation() {
		return _wordButton.getLayoutY();
	}
	// Method to set the color of the buttons
	public void setColor(Color color) {
		_wordSquare.setFill(color);
	}
}
