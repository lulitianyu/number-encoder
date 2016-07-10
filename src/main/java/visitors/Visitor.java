package visitors;

import java.util.List;
import java.util.Map.Entry;

import dawg.IDawg;

/**
 * {@link Visitor} provides an interface to visit the {@link IDawg}.
 * 
 */
public interface Visitor {

	/**
	 * Visit the {@link IDawg} and find all words that match the character
	 * sequence.
	 * 
	 * @param characterQueue
	 *            the character sequence with many possible combinations for
	 *            words with integer alternative if no match is found
	 * @return the list of words matches the character sequence
	 */
	List<String> visit(List<Entry<Integer, char[]>> characterQueue);

}