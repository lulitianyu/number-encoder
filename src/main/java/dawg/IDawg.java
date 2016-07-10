package dawg;

/**
 * {@link IDawg} provides an interface of Directed Acyclic Word Graph.
 * 
 */
public interface IDawg {

	/**
	 * Add a word to the graph.
	 * 
	 * @param word
	 *            the word
	 */
	void add(String word);

	/**
	 * Construct the graph. This method must be called after all words are
	 * added.
	 */
	void construct();

	/**
	 * Get the root node of the graph.
	 * 
	 * @return the root node.
	 */
	IDawgNode getRoot();

	/**
	 * 
	 * @return the number of nodes in the graph.
	 */
	int getSize();

}