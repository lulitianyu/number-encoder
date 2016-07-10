package dawg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DawgTest {
	private IDawg dawg;

	@Before
	public void setUp() {
		dawg = new Dawg();
	}

	private void afterAddWord_ShouldContainWord(String word) {
		dawg.add(word);
		IDawgNode node = dawg.getRoot();

		for (char ch : word.toCharArray()) {
			assertTrue(node.containsEdge(ch));
			node = node.getChild(ch);
		}
		assertTrue(node.isFinal());
	}

	private void afterFinalize_ShouldHaveSize(int count) {
		dawg.construct();
		assertEquals(count, dawg.getSize());
	}

	@Test
	public void afterAddWord_ShouldContainWordAndHaveNodeCount() {
		String word = "hello";
		afterAddWord_ShouldContainWord(word);
	}

	@Test
	public void afterAddTwoDifferentWords_ShouldContainWordAndHaveNodeCount() {
		String hello = "hello";
		String world = "world";
		afterAddWord_ShouldContainWord(hello);
		afterAddWord_ShouldContainWord(world);
		afterFinalize_ShouldHaveSize(hello.length() + world.length());
	}

	@Test
	public void afterAddTwoWordsWithOnlyCommonPrefix_ShouldContainWordAndHaveNodeCount() {
		String hello = "hello";
		String help = "help";
		int commonPrefix = 3;

		afterAddWord_ShouldContainWord(hello);
		afterAddWord_ShouldContainWord(help);
		afterFinalize_ShouldHaveSize(hello.length() + help.length()
				- commonPrefix);
	}

	@Test
	public void afterAddTwoWordsWithCommonPrefixAndSuffix_ShouldContainWordAndHaveNodeCount() {
		String hello = "hello";
		String helo = "helo";
		int commonPrefix = 3;
		int commonSuffix = 1;

		afterAddWord_ShouldContainWord(hello);
		afterAddWord_ShouldContainWord(helo);
		afterFinalize_ShouldHaveSize(hello.length() + helo.length() + 1
				- commonPrefix - commonSuffix);
	}

	@Test
	public void afterAddThreeWordsWithCommonPrefixAndSuffix_ShouldContainWordAndHaveNodeCount() {
		String cat = "cat";
		String cats = "cats";
		String catfish = "catfish";
		int commonPrefix = 3;
		int commonSuffix = 1;

		afterAddWord_ShouldContainWord(cat);
		afterAddWord_ShouldContainWord(catfish);
		afterAddWord_ShouldContainWord(cats);
		afterFinalize_ShouldHaveSize(cat.length() + cats.length()
				+ catfish.length() + 1 - commonPrefix * 2 - commonSuffix);
	}

	@Test
	public void afterAddSixWordsWithCommonPrefixAndSuffix_ShouldContainWordAndHaveNodeCount() {
		String produce = "produce";
		String productive = "productive";
		String production = "production";
		String seduce = "seduce";
		String seduction = "seduction";
		String seductive = "seductive";

		afterAddWord_ShouldContainWord(produce);
		afterAddWord_ShouldContainWord(production);
		afterAddWord_ShouldContainWord(productive);
		afterAddWord_ShouldContainWord(seduce);
		afterAddWord_ShouldContainWord(seduction);
		afterAddWord_ShouldContainWord(seductive);
		afterFinalize_ShouldHaveSize(13);
	}

	@Test
	public void afterAddFiveWordsWithCommonPrefixAndSuffix_ShouldContainWordAndHaveNodeCount() {
		String produce = "produce";
		String productive = "productive";
		String production = "production";
		String seduce = "seduce";
		String seductive = "seductive";

		afterAddWord_ShouldContainWord(produce);
		afterAddWord_ShouldContainWord(production);
		afterAddWord_ShouldContainWord(productive);
		afterAddWord_ShouldContainWord(seduce);
		afterAddWord_ShouldContainWord(seductive);
		afterFinalize_ShouldHaveSize(19);
	}

	@Test(expected = Dawg.WordsNotAddedInAlphabeticOrderException.class)
	public void givenWordsNotInOrder_ShouldThrow() {
		String product = "product";
		String produce = "produce";

		dawg.add(product);
		dawg.add(produce);
	}
}
