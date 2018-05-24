package Wordament;

import java.util.*;
import java.util.Scanner;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/* (8) Dictionary: Basically, it is the library (brain?) of the game. It imports words from a text file and create the
program's own list of word, and it creates basically helps the WordFactory to create the first word and
the following words in order to complete the board. It also helps to check if the word input in the game
is true or not.
 */
public class Dictionary {
	// Declaring necessary instance variables for usage in different places
	private static HashSet<String> _dictionary;
	private File _dictionaryDatabase;
	
	// Make sure to throw IOException just in case
	public Dictionary() throws IOException {
		_dictionary = new HashSet<String>(); // The use of HashSet to make the run time much more efficient
		_dictionaryDatabase = new File("/Users/macbook/Desktop/CS0150/Wordament/DictionaryDatabase.txt");
		Dictionary.this.addDataToDictionary(); // Metod defined below
	}
	// Method to scan the database imported outside to the dictionary
	private void addDataToDictionary() throws IOException, FileNotFoundException {
		Scanner databaseScanner = new Scanner(new FileReader(_dictionaryDatabase));
		while (databaseScanner.hasNext() == true) {
			// I choose to do all the back end stuff of my program in Lower Case
			_dictionary.add(databaseScanner.nextLine().toLowerCase());
		}
		databaseScanner.close();
	}
	
	// Method to check contain word
	public boolean checkContainWord(String word) {
		if (_dictionary.contains(word) == true) {
			return true;
		} else {
			return false;
		}
	}
	/// This method is to create the first word to start the board with
	public static String createFirstWord() {
		// Basically create a new string
		String firstWord = new String("");
		// the While loop to make sure that the words we create are larger than 8 words and smaller than 16 words
		while (firstWord.length() < Constants.MIN_FIRST_WORD_LENGTH || firstWord.length() > Constants.MAX_FIRST_WORD_LENGTH) {
			// This is to randomize a number in the dictionary that is going to be the location of word we look for
			int wordNumber = (int)(Math.random()*(_dictionary.size()));
			// This is the establishment to start counting towards the selected words - when it reaches the word
			// of that location it will go back
			int i = 0;
			for (String word : _dictionary) {
				if (i == wordNumber) {
					firstWord = word;
				}
				i++;
			}
		}

		return firstWord;
	}
	
	// The method is similar to that above, but I decided to make a separate method anyways, because the first word
	// dont have a length constraint and its probably longer to factor out codes
	public static String createWordGivenLength(int length) {
		String secondaryWord = new String("");
		while (secondaryWord.length() != length) {
			int wordNumber = (int)(Math.random()*(_dictionary.size()));
			int i = 0;
			for (String word : _dictionary) {
				if (i == wordNumber) {
					secondaryWord = word;
				}
				i++;
			}
		}
		return secondaryWord;
	}
	
}
