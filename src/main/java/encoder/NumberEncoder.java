package encoder;

import dawg.IDawg;
import visitors.Visitor;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

/**
 * {@link NumberEncoder} encodes a number into words from a dictionary.
 */
public class NumberEncoder {
    private final Map<Integer, char[]> digitToCharacterMapping;

    /**
     * The Directed Acyclic Word Graph to store all the words.
     */
    private final IDawg dawg;

    /**
     * The dictionary maps the formatted words to the original words.
     */
    private final Map<String, String> dictionary;

    /**
     * Visitor to find all matching words for a characters queue.
     */
    private final Visitor visitor;

    public NumberEncoder(Map<Integer, char[]> digitToCharacterMapping, IDawg dawg, Visitor visitor) {
        this.digitToCharacterMapping = digitToCharacterMapping;
        this.dawg = dawg;
        this.visitor = visitor;
        this.dictionary = new HashMap<String, String>();
    }

    /**
     * Add a word to the DAWG and dictionary.
     *
     * @param word the word
     */
    public void addWord(String word) {
        // remove all invalid characters and change cases to lower
        String formattedWord = word.replaceAll("(\"|-)", "").toLowerCase();
        dictionary.put(formattedWord, word);
        dawg.add(formattedWord);
    }

    /**
     * Prepare the DAWG. This method must be called after all words are added
     * and before any encoding.
     */
    public void prepare() {
        dawg.construct();
    }

    /**
     * Encode a number string to matching words.
     *
     * @param numberTobeEncoded the number
     * @return a list of words.
     */
    public String[] encode(String numberTobeEncoded) {
        List<Entry<Integer, char[]>> characterQueue = mapNumberToCharacterQueue(numberTobeEncoded);
        List<String> formattedWords = searchForWordsComposedWithCharacters(characterQueue);
        List<String> originalWords = translateWordsToOriginal(formattedWords);

        return sortWords(originalWords);
    }

    private List<Entry<Integer, char[]>> mapNumberToCharacterQueue(String numberToBeEncoded) {
        int[] digits = toDigitSequence(numberToBeEncoded.replaceAll("(/|-)", ""));

        List<Entry<Integer, char[]>> characterQueue = new ArrayList<Entry<Integer, char[]>>(digits.length);
        for (int digit : digits) {
            characterQueue.add(new SimpleEntry<Integer, char[]>(digit, digitToCharacterMapping.get(digit)));
        }
        return characterQueue;
    }

    private int[] toDigitSequence(String numbers) {
        int[] digits = new int[numbers.length()];

        for (int i = 0; i < numbers.length(); i++) {
            digits[i] = Character.getNumericValue(numbers.charAt(i));
        }
        return digits;
    }

    private List<String> searchForWordsComposedWithCharacters(List<Entry<Integer, char[]>> characterQueue) {
        return visitor.visit(characterQueue);
    }

    private List<String> translateWordsToOriginal(List<String> formattedWords) {
        List<String> originalWords = new ArrayList<String>(formattedWords.size());

        for (String word : formattedWords) {
            originalWords.add(originalWord(word));
        }
        return originalWords;
    }

    private String originalWord(String word) {
        String originalWord = "";
        for (String s : word.split(" ")) {
            originalWord += " ";

            if (dictionary.containsKey(s))
                originalWord += dictionary.get(s);
            else
                originalWord += s;
        }
        return originalWord.trim();
    }

    private String[] sortWords(List<String> originalWords) {
        String[] array = originalWords.toArray(new String[originalWords.size()]);
        Arrays.sort(array);
        return array;
    }
}
