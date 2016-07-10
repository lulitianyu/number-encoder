package dawg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DawgNodeTest {
	private DawgNode node;
	private DawgNode nodeToTestEquality;
	private DawgNode newNode;
	private long numberOfNodes;

	@Before
	public void setUp() {
		numberOfNodes = 0;
		node = new DawgNode(numberOfNodes++);
		nodeToTestEquality = new DawgNode(numberOfNodes++);
		newNode = new DawgNode(numberOfNodes++);
	}

	@Test
	public void afterAddConnection_ShouldContainEdge() {
		char edge = 'e';
		node.addChild(edge, newNode);
		assertTrue(node.containsEdge(edge));
	}

	@Test
	public void afterSetFinal_ShouldBeFinal() {
		node.setFinal();
		assertTrue(node.isFinal());
	}

	@Test
	public void givenBothNodesFinalAndNoEdge_ShouldEqual() {
		node.setFinal();
		nodeToTestEquality.setFinal();
		assertEquals(node, nodeToTestEquality);
	}

	@Test
	public void givenOneNodeFinalTheOtherNot_ShouldNotEqual() {
		node.setFinal();
		assertNotEquals(node, nodeToTestEquality);
	}

	@Test
	public void givenBothNodesWithSameEdge_ShouldEqual() {
		node.addChild('e', newNode);
		nodeToTestEquality.addChild('e', newNode);
		assertEquals(node, nodeToTestEquality);
	}

	@Test
	public void givenNodesWithDifferentEdge_ShouldNotEqual() {
		node.addChild('e', newNode);
		nodeToTestEquality.addChild('f', newNode);
		assertNotEquals(node, nodeToTestEquality);
	}

	@Test
	public void givenNodesWithNextNodeOfDifferentFinalState_ShouldNotEqual() {
		newNode.setFinal();
		node.addChild('e', newNode);
		nodeToTestEquality.addChild('e', new DawgNode(numberOfNodes++));
		assertNotEquals(node, nodeToTestEquality);
	}

	@Test
	public void givenNodesWithNextNodeOfDifferentEdge_ShouldNotEqual() {
		newNode.addChild('e', new DawgNode(numberOfNodes++));
		node.addChild('e', newNode);
		nodeToTestEquality.addChild('e', new DawgNode(numberOfNodes++));
		assertNotEquals(node, nodeToTestEquality);
	}

	@Test
	public void givenOneNodeWithEdgeOnly_ShouldNotEqual() {
		node.addChild('e', newNode);
		assertNotEquals(node, nodeToTestEquality);
	}

	@Test
	public void afterAddConnection_ShouldGetConnectionReturnSameNode() {
		node.addChild('e', newNode);
		assertEquals(newNode, node.getChild('e'));
	}
}
