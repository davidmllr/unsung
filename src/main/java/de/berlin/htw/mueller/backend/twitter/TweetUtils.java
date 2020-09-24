package de.berlin.htw.mueller.backend.twitter;

public class TweetUtils {
    public static String clean(String tweet) {
        return tweet
                .replaceAll("@[A-Za-z0–9]+", "")
                .replaceAll("#", "")
                .replaceAll("RT[\\s]+", "")
                .replaceAll("https?://\\S+", "");
    }
}
