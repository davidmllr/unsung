package de.berlin.htw.mueller._deprecated.translator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Translator {

    private static List<Translation> translations;
    private static final Logger logger = LoggerFactory.getLogger(Translator.class);

    public static void init() throws IOException {
        logger.info("Initializing translation dictionary...");
        final Path path = Paths.get("src/main/resources/models/translations/dict-de-en.txt");
        try (final Stream<String> lines = Files.lines(path)) {
            translations = lines.map(line -> line.split("\\t")).map(line -> {
                final String german = line[0];
                final String english = line[1];
                return new Translation(german, english);
            }).collect(Collectors.toList());
        }
        logger.info("Successfully initialized translation dictionary.");
    }

    public static String translateSentence(String sentence) {
        List<String> words = getWords(sentence);
        return words.parallelStream().map(word -> {
            String translated = translateGermanToEnglish(word);
            logger.info("Translated '{}' to '{}'.", word, translated);
            return translated;
        }).collect(Collectors.joining(" "));
    }

    /**
     *
     * @param str
     * @return
     */
    public static String translate(String str) {
        boolean isEnglish = translations.stream()
                .anyMatch(t -> StringUtils.containsIgnoreCase(t.getEnglish(), str));
        if(isEnglish) return translateEnglishToGerman(str);

        boolean isGerman = translations.stream()
                .anyMatch(t -> StringUtils.containsIgnoreCase(t.getGerman(), str));

        if(isGerman) return translateGermanToEnglish(str);
        return str;
    }

    /**
     *
     * @param str
     * @return
     */
    public static String translateEnglishToGerman(String str) {
        Translation translation = translations.stream()
                .filter(t -> StringUtils.equalsAnyIgnoreCase(t.getEnglish(), str))
                .findAny()
                .orElse(null);

        return translation == null ? str : translation.getGerman();
    }

    /**
     *
     * @param str
     * @return
     */
    public static String translateGermanToEnglish(String str) {
        Translation translation = translations.stream()
                .filter(t -> StringUtils.equalsAnyIgnoreCase(t.getGerman(), str))
                .findAny()
                .orElse(null);

        return translation == null ? str : translation.getEnglish();
    }

    /**
     *
     * @param text
     * @return
     */
    public static List<String> getWords(String text) {
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex));
            }
        }

        return words;
    }
}
