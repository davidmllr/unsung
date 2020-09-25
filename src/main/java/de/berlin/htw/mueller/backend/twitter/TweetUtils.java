package de.berlin.htw.mueller.backend.twitter;

/**
 * This class holds functions to be used on tweets.
 */
public class TweetUtils {
    /**
     * Removes mentions, hashtags, URLs and special characters from tweets.
     * @param tweet is a given tweet as String.
     * @return a tweet without mentions, hashtags, URLS or special characters.
     */
    public static String clean(String tweet) {
        return tweet
                .replaceAll("@[A-Za-z0–9]+", "")
                .replaceAll("#", "")
                .replaceAll("RT[\\s]+", "")
                .replaceAll("https?://\\S+", "");
    }
}
