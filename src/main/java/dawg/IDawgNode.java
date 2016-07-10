package dawg;

/**
 * {@link IDawgNode} provides an interface of a node in a Directed Acyclic Word
 * Graph.
 * 
 */
public interface IDawgNode {

	/**
	 * Check if the current node contains the specified edge.
	 * 
	 * @param edge
	 *            the edge
	 * @return true if the current node contains the specified edge to a child
	 *         node.
	 */
	boolean containsEdge(char edge);

	/**
	 * Retrieve a child node with the specified edge.
	 * 
	 * @param edge
	 *            the edge
	 * @return a child node the specified edge leads to. Null if there's no such
	 *         edge.
	 */
	IDawgNode getChild(char edge);

	/**
	 * A final node marks the end of a word.
	 * 
	 * @return true if this node is a final node.
	 */
	boolean isFinal();

}