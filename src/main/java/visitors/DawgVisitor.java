package visitors;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import dawg.IDawg;
import dawg.IDawgNode;

public class DawgVisitor implements Visitor {
	private final IDawg dawg;

	public DawgVisitor(IDawg dawg) {
		this.dawg = dawg;
	}

	public List<String> visit(List<Entry<Integer, char[]>> characterQueue) {
		List<String> words = new LinkedList<String>();
		searchForNextWordFromRoot(words, characterQueue, "");
		return words;
	}

    private boolean searchForNextWordFromNode(List<String> words, List<Entry<Integer, char[]>> queue, IDawgNode node, String word) {
        if (queue.isEmpty()) {
			// when queue is empty and the node is final or root, all characters are matched to words.
			if (node.isFinal() || node.equals(dawg.getRoot())) {
				words.add(word.trim());
				return true;
			}
			return false;
		}

		boolean found = false;

		// when queue is not empty and the node is final, a word is found to
		// match previous parts of the character queue.
		//
		// start from the root node and find a matching word for the rest of the
		// character queue.
		if (node.isFinal())
			found = searchForNextWordFromRoot(words, queue, word + " ");

        // loop through the characters to see if the node contains such an edge.
		for (char ch : queue.get(0).getValue()) {
			if (node.containsEdge(ch)) {
				// if a child node to which the character leads is found, visit
				// the child node with the next characters
				found = searchForNextWordFromNode(words, queue.subList(1, queue.size()), node.getChild(ch), word + ch) || found;
			}
		}

		return found;
	}

    private boolean searchForNextWordFromRoot(List<String> words, List<Entry<Integer, char[]>> queue, String word) {
        boolean found = searchForNextWordFromNode(words, queue, dawg.getRoot(), word);

        // if no match found, store an integer alternative for the characters
		// and start from root node and next characters in the queue
		if (!found)
			found = searchForNextWordFromNode(words, queue.subList(1, queue.size()), dawg.getRoot(), word + queue.get(0).getKey() + " ");
		return found;
	}
}
