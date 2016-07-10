package dawg;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link DawgNode} implements the {@link IDawgNode} interface.
 * 
 */
public class DawgNode implements IDawgNode {
	/**
	 * Unique node ID.
	 */
	private final long id;

	/**
	 * Child nodes reachable from the current node through an edge. The edge is
	 * the Key of the Map and the Child is the Value.
	 */
	private final Map<Character, DawgNode> children;

	/**
	 * A node is final if it's the last node of a word.
	 */
	private boolean isFinal;

	public DawgNode(long id) {
		this.id = id;
		children = new HashMap<Character, DawgNode>();
		isFinal = false;
	}

	public boolean containsEdge(char edge) {
		return children.containsKey(edge);
	}

	void addChild(char edge, DawgNode child) {
		children.put(edge, child);
	}

	public IDawgNode getChild(char edge) {
		return children.get(edge);
	}

	public boolean isFinal() {
		return isFinal;
	}

	/**
	 * Mark the node final and end of a word.
	 */
	void setFinal() {
		isFinal = true;
	}

	/**
	 * Construct a string to be used for hashing purpose. The string is
	 * constructed based on the final state and all edges and children's ID.
	 * 
	 * @return a hash string
	 */
	private String hashString() {
		String hash = String.valueOf(isFinal);
		for (Entry<Character, DawgNode> e : children.entrySet()) {
			hash += e.getKey();
			hash += e.getValue().id;
		}
		return hash;
	}

	@Override
	public int hashCode() {
		return hashString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		DawgNode other = (DawgNode) obj;

		// Two nodes are equal if their IDs are the same
		if (id == other.id)
			return true;

		// If their IDs are different but with the same final state, edges and
		// children, they are considered the same node.
		return hashString().equals(other.hashString());
	}

	@Override
	public String toString() {
		return "DawgNode [id=" + id + ", edges=" + children.keySet()
				+ ", isFinal="
				+ isFinal + "]";
	}

}
