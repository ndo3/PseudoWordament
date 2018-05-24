package Wordament;

import java.util.*;
/* THIS IS THE CLASS ComputerPlayer, which has the methods that will be used when a ComputerPlayer is initiated in
 * the WordamentGame.
 */
public class ComputerPlayer {

	private ArrayList<String> _listOfComputerWords;
	
	public ComputerPlayer() {
		// This is so that whenever the Computer is initiated, the computer will already know the list of the available
		// words, and know which words are the longest and smartest to play
		_listOfComputerWords = new ArrayList<String>();
		ComputerPlayer.this.createComputerListOfWords(WordFactory.getListOfAvailableWords());
	}
	
	/* Basically, the gist of this method is that it takes a HashSet of available words and make an array list out of it,
	 * so that it would use the most efficient data structure in order to be able to sort the available words by length the
	 * most effectively - I can comment on this in the README file later
	 */
	public void createComputerListOfWords(HashSet<String> listOfAvailableWords) {
		// ArrayList - completed the ArrayList of available words
		for (String word : listOfAvailableWords) {
			_listOfComputerWords.add(word);
		}
		// Sorting it by length - shortest first longest last
		Collections.sort(_listOfComputerWords, new sortByLength());
	}
			// The helper class to help sort the array list
			// Creates a private comparator class to compare it by length
			private class sortByLength implements Comparator<String> {
				@Override
				public int compare(String stringA, String stringB) {
					int difference = stringA.length() - stringB.length();
					return difference;
				}
			}
	
	// This is the method that would be used 
	public String getNextWord() {
		// So that we get the longest word - this is because while we sorted the longest ones are at the back
		String newWord = _listOfComputerWords.remove(_listOfComputerWords.size() - 1);
		return newWord;
	}
}
