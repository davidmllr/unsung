package de.berlin.htw.mueller.backend.spotify;

/**
 * This class holds explanations for the track features used within the application.
 * They are taken from Spotify's official documentation, which can be found here:
 * https://developer.spotify.com/documentation/web-api/reference/tracks/get-audio-features/
 */
public class Explanations {
    public static String VALENCE = "A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. " +
            "Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric), while seed_tracks with low valence sound more negative (e.g. sad, depressed, angry).";
    public static String DANCEABILITY = "Danceability describes how suitable a track is for dancing based on a combination of musical elements including tempo," +
            " rhythm stability, beat strength, and overall regularity." +
            " A value of 0.0 is least danceable and 1.0 is most danceable.";
    public static String ENERGY = "Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity." +
            " Typically, energetic seed_tracks feel fast, loud, and noisy." +
            " For example, death metal has high energy, while a Bach prelude scores low on the scale." +
            " Perceptual features contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate, and general entropy.";
}
