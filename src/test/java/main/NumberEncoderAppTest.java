package main;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NumberEncoderAppTest {
	private static final String NEW_LINE = System.getProperty("line.separator");

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private File phonebook;
	private File dictionary;
	private final String[] numbers = { "112", "5624-82", "4824",
			"0721/608-4067", "10/783--5", "1078-913-5", "381482", "04824" };

	@Before
	public void setUp() throws IOException {

		phonebook = folder.newFile("phonebook.txt");
		writeFile(phonebook, numbers);

		String[] dictionaries = new String[] { "an", "blau", "Bo\"", "Boot",
				"bo\"s", "da", "Fee", "fern", "Fest", "fort", "je", "jemand",
				"mir", "Mix", "Mixer", "Name", "neu", "o\"d", "Ort", "so",
				"Tor", "Torf", "Wasser" };

		dictionary = folder.newFile("dictionary.txt");
		writeFile(dictionary, dictionaries);
	}

	private void writeFile(File file, String[] lines) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		for (String line : lines) {
			out.write(line + NEW_LINE);
		}
		out.close();
		assertTrue(file.exists());
	}

	@Test
	public void givenPhonebookAndDictionary_ShouldDisplay() throws IOException {

		ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutputStream));

		List<String[]> sentences = new LinkedList<String[]>();
		sentences.add(new String[] {});
		sentences.add(new String[] { "Mix Tor", "mir Tor" });
		sentences.add(new String[] { "fort", "Tor 4", "Torf" });
		sentences.add(new String[] {});
		sentences.add(new String[] { "je Bo\" da", "je bo\"s 5", "neu o\"d 5" });
		sentences.add(new String[] {});
		sentences.add(new String[] { "so 1 Tor" });
		sentences.add(new String[] { "0 fort", "0 Tor 4", "0 Torf" });

		String output = "";
		for (int i = 0; i < numbers.length; i++) {
			String[] sentence = sentences.get(i);
			if (sentence.length > 0) {
				Arrays.sort(sentence);
				for (String word : sentence) output += numbers[i] + ": " + word + NEW_LINE;
			}
		}

        NumberEncoderApp.main(new String[]{phonebook.getPath(), dictionary.getPath()});

		assertEquals(output, testOutputStream.toString());

		testOutputStream.close();
	}
}
