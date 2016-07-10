package dawg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link Dawg} implements {@link IDawg} interface. {@link Dawg} stores the
 * characters of a word in its edges and a node in the graph contains a map of
 * edges to child nodes and a final state. A final node marks the end of a word.
 * Storing words this way saves lots of memory space, because words may contain
 * duplicate characters.
 * 
 * For example, construction and constructive have common prefix "constructi".
 * construction and production have common suffix "uction". Those common parts
 * may be duplicate.
 * 
 */
public class Dawg implements IDawg {
	private long dawgNodeIdGenerator = 0;

	private final DawgNode root;

	private final Map<DawgNode, DawgNode> uniqueNodes;

	private String lastAddedWord;

	private final List<ParentEdgeChildNode> nodesWithDuplicates;

	public Dawg() {
		root = new DawgNode(dawgNodeIdGenerator++);
		uniqueNodes = new HashMap<DawgNode, DawgNode>();
		lastAddedWord = "";
		nodesWithDuplicates = new ArrayList<ParentEdgeChildNode>();

		nodesWithDuplicates.add(new ParentEdgeChildNode(root));
	}

	/**
	 * Add a word to the graph. Words must be added in alphabetical order or a
	 * WordsNotAddedInAlphabeticOrderException is thrown.
	 * 
	 * @param word
	 *            the word
	 */
	public void add(String word) {
		if (lastAddedWord.compareTo(word) > 0) {
			throw new WordsNotAddedInAlphabeticOrderException();
		}

		// find length of common prefix in the word, which can be used as index
		// of next different character in the word.
		// No new node needs to be created for characters in the common prefix
		int commonPrefixLength = getCommonPrefixLength(word);

		removeCommonSuffixFromEndOfWordToCommonPrefix(commonPrefixLength);

		// create new nodes for characters from common prefix to end of word
		addNewNodesFromCommonPrefix(word, commonPrefixLength);

		// replace last added word with the current word
		lastAddedWord = word;
	}

	/**
	 * Calculate the length of common prefix between the specified word and the
	 * last added word. For example, construction and constructive both have
	 * common prefix "constructi". The length of common prefix is 10.
	 * 
	 * @param word
	 *            the word
	 * @return the length of common prefix
	 */
	private int getCommonPrefixLength(String word) {
		int minimumStringLength = Math.min(word.length(), lastAddedWord.length());
		int commonPrefixLength = 0;
		while (commonPrefixLength < minimumStringLength
				&& word.charAt(commonPrefixLength) == lastAddedWord.charAt(commonPrefixLength)) {
			commonPrefixLength++;
		}
		return commonPrefixLength;
	}

	/**
	 * Find possible common suffix between unchecked node list and checked node
	 * list. Replace the common nodes with those in the checked node list. Add
	 * the remaining to the checked node list, since they are checked and
	 * unique.
	 * 
	 * @param commonPrefixLength
	 *            the common prefix length
	 */
	private void removeCommonSuffixFromEndOfWordToCommonPrefix(int commonPrefixLength) {
		// start from the end of the unchecked list, since it's easy to identify
		// duplicate from end of word.
		for (int i = nodesWithDuplicates.size() - 1; i > commonPrefixLength; i--) {
			ParentEdgeChildNode node = nodesWithDuplicates.remove(i);

			// if there's a matching node in checked list with the same final
			// state, edges, and child node, the current node is a duplicate
			if (uniqueNodes.containsKey(node.child)) {
				// replace the duplicate reference with the matching one in the
				// checked list
				node.parent.addChild(node.edge, uniqueNodes.get(node.child));
			} else {
				// add node to checked list
				uniqueNodes.put(node.child, node.child);
			}
		}
	}

	/**
	 * Create new nodes for characters from common prefix and add them to
	 * unchecked list.
	 * 
	 * @param word
	 *            the word
	 * @param commonPrefixLength
	 *            the common prefix length
	 */
	private void addNewNodesFromCommonPrefix(String word, int commonPrefixLength) {
		// root is in the unchecked list, so commonPrefixLength points to the
		// last node of common prefix
		DawgNode node = nodesWithDuplicates.get(commonPrefixLength).child;
		for (int i = commonPrefixLength; i < word.length(); i++) {
			char ch = word.charAt(i);
			DawgNode newNode = new DawgNode(dawgNodeIdGenerator++);
			node.addChild(ch, newNode);

			nodesWithDuplicates.add(new ParentEdgeChildNode(node, newNode, ch));
			node = newNode;
		}
		node.setFinal();
	}

	public void construct() {
		// remove duplicate nodes for the entire unchecked list except root node
		// and add the rest to the checked list
		removeCommonSuffixFromEndOfWordToCommonPrefix(0);
	}

	public IDawgNode getRoot() {
		return root;
	}

	public int getSize() {
		// include the root node
		return uniqueNodes.size() + 1;
	}

	/**
	 * {@link ParentEdgeChildNode} stores the reference to the current node
	 * (child), parent node, and edge connecting them.
	 * 
	 * It's used to store all unchecked nodes in order to easily replace the
	 * duplicates.
	 * 
	 */
	private static class ParentEdgeChildNode {
		private final DawgNode parent;
		private final DawgNode child;
		private final char edge;

		public ParentEdgeChildNode(DawgNode parent, DawgNode child, char edge) {
			this.parent = parent;
			this.child = child;
			this.edge = edge;
		}

		public ParentEdgeChildNode(DawgNode child) {
			this(null, child, ' ');
		}
	}

	public static class WordsNotAddedInAlphabeticOrderException extends RuntimeException {
	}
}
