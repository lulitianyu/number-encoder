package encoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import visitors.DawgVisitor;
import dawg.Dawg;

public class NumberEncoderTest {
	private NumberEncoder encoder;
	private final String[] dictionaries = new String[] { "an", "blau", "Bo\"",
			"Boot", "bo\"s", "da", "Fee", "fern", "Fest", "fort", "je",
			"jemand", "mir", "Mix", "Mixer", "Name", "neu", "o\"d", "Ort",
			"so", "Tor", "Torf", "Wasser" };

	@Before
	public void setUp() {
		Map<Integer, char[]> numberCharacterMapping = new HashMap<Integer, char[]>();

		numberCharacterMapping.put(0, new char[] { 'e' });
		numberCharacterMapping.put(1, new char[] { 'j', 'n', 'q' });
		numberCharacterMapping.put(2, new char[] { 'r', 'w', 'x' });
		numberCharacterMapping.put(3, new char[] { 'd', 's', 'y' });
		numberCharacterMapping.put(4, new char[] { 'f', 't' });
		numberCharacterMapping.put(5, new char[] { 'a', 'm' });
		numberCharacterMapping.put(6, new char[] { 'c', 'i', 'v' });
		numberCharacterMapping.put(7, new char[] { 'b', 'k', 'u' });
		numberCharacterMapping.put(8, new char[] { 'l', 'o', 'p' });
		numberCharacterMapping.put(9, new char[] { 'g', 'h', 'z' });

		Dawg dawg = new Dawg();
		encoder = new NumberEncoder(numberCharacterMapping, dawg,
				new DawgVisitor(dawg));
	}

	@Test
	public void givenNumbers_ShouldEncode() {
		String[] numbers = { "112", "5624-82", "4824", "0721/608-4067",
				"10/783--5", "1078-913-5", "381482", "04824" };

		for (String word : dictionaries) {
			encoder.addWord(word);
		}
		encoder.prepare();

		List<String[]> words = new LinkedList<String[]>();
		words.add(new String[] {});
		words.add(new String[] { "Mix Tor", "mir Tor" });
		words.add(new String[] { "fort", "Tor 4", "Torf" });
		words.add(new String[] {});
		words.add(new String[] { "je Bo\" da", "je bo\"s 5", "neu o\"d 5" });
		words.add(new String[] {});
		words.add(new String[] { "so 1 Tor" });
		words.add(new String[] { "0 fort", "0 Tor 4", "0 Torf" });

		for (int i = 0; i < numbers.length; i++) {
			Arrays.sort(words.get(i));
			Assert.assertArrayEquals(words.get(i), encoder.encode(numbers[i]));
		}
	}
}
