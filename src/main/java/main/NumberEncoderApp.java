package main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import visitors.DawgVisitor;
import dawg.Dawg;
import encoder.NumberEncoder;
import formatter.NumberEncodeFormatter;

public class NumberEncoderApp {
    private static final Map<Integer, char[]> digitToCharacterMapping = new HashMap<Integer, char[]>() {
        {
            put(0, new char[]{'e'});
            put(1, new char[]{'j', 'n', 'q'});
            put(2, new char[]{'r', 'w', 'x'});
            put(3, new char[]{'d', 's', 'y'});
            put(4, new char[]{'f', 't'});
            put(5, new char[]{'a', 'm'});
            put(6, new char[]{'c', 'i', 'v'});
            put(7, new char[]{'b', 'k', 'u'});
            put(8, new char[]{'l', 'o', 'p'});
            put(9, new char[]{'g', 'h', 'z'});
        }
    };

    private final NumberEncoder encoder;

    public NumberEncoderApp() {
        Dawg dawg = new Dawg();
        encoder = new NumberEncoder(digitToCharacterMapping, dawg, new DawgVisitor(dawg));

    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2) {
            System.out.println("Usage: NumberEncoderApp <phonebook> <dictionary>");
            return;
        }
        NumberEncoderApp app = new NumberEncoderApp();

        app.initialize(args[1]);
        app.run(args[0]);
    }

    /**
     * Initialize encoder with a dictionary.
     *
     * @param dictionary the dictionary file path
     * @throws FileNotFoundException when no such file is found
     */
    private void initialize(String dictionary) throws FileNotFoundException {
        Scanner dictionaryScanner = new Scanner(new BufferedInputStream(
                new FileInputStream(dictionary)));
        try {
            while (dictionaryScanner.hasNextLine()) {
                encoder.addWord(dictionaryScanner.nextLine());
            }
            encoder.prepare();
        } finally {
            dictionaryScanner.close();
        }
    }

    /**
     * Run encoder through the phone book.
     *
     * @param phonebook the phone book file path.
     * @throws FileNotFoundException when no such file is found
     */
    private void run(String phonebook) throws FileNotFoundException {
        Scanner phonebookScanner = new Scanner(new BufferedInputStream(
                new FileInputStream(phonebook)));

        try {
            while (phonebookScanner.hasNextLine()) {
                String phoneNumber = phonebookScanner.nextLine();
                System.out.print(NumberEncodeFormatter.format(phoneNumber, encoder.encode(phoneNumber)));
            }
        } finally {
            phonebookScanner.close();
        }
    }

}
