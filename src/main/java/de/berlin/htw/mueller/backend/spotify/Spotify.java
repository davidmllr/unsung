package de.berlin.htw.mueller.backend.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;
import de.berlin.htw.mueller.backend.analytics.Processor;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to interact with the Spotify API.
 * It handles authentication, authorization and basic requests for the application.
 * It's scope is limited to a session to make sure all users are authenticated separately.
 */
@Component
@Scope(value="session")
public class Spotify {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    private SpotifyApi spotifyApi;

    private final Logger logger = LoggerFactory.getLogger(Spotify.class);

    /**
     * Initializes the Spotify API as an object.
     */
    @PostConstruct
    public void init() {
        final URI uri = SpotifyHttpManager.makeUri(redirectUri);
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(uri)
                .build();
    }

    /**
     *
     * @return the Spotify API as an object.
     */
    public SpotifyApi getApi() {
        return spotifyApi;
    }

    /**
     * Creates a new authorization request for a user.
     * @return the URI for the authorization request.
     */
    public URI authorize() {
        final AuthorizationCodeUriRequest request = spotifyApi.authorizationCodeUri()
                .scope("user-top-read")
                .show_dialog(true)
                .build();
        return authorize(request);
    }

    /**
     * Executes a given authorization request.
     * @param request is a given authorization request.
     * @return the URI for the authorization request.
     */
    private URI authorize(AuthorizationCodeUriRequest request) {
        return request.execute();
    }

    /**
     * Returns a song recommendation for a given user by using their top songs.
     * @param result is the result returned by the analysis and interpretation of tweets.
     * @param isCheerUp is an indicator if the user wants to be cheered up by the recommendation.
     * @return the id of the recommended song.
     * @throws ParseException
     * @throws SpotifyWebApiException
     * @throws IOException
     */
    public String getRecommendation(Processor.Result result, boolean isCheerUp)
            throws ParseException, SpotifyWebApiException, IOException {
        List<String> seeds = getSeedsForUser();
        String str = String.join(",", seeds);
        final GetRecommendationsRequest getRecommendationsRequest = spotifyApi.getRecommendations()
                .limit(50)
                .seed_tracks(str)
                .target_valence(isCheerUp ? 1.0f : result.getPositiveRatio())
                .target_danceability(isCheerUp ? 1.0f : result.getNegativeRatio())
                .target_energy(result.getNeutralRatio())
            .build();
        final Recommendations recommendations = getRecommendationsRequest.execute();
        TrackSimplified track = Arrays.stream(recommendations.getTracks())
                .findAny()
                .orElse(null);
        return track == null ? "2goLsvvODILDzeeiT4dAoR" : track.getId();
    }

    /**
     * Returns a song recommendation based on a given genre.
     * @param result is the result returned by the analysis and interpretation of tweets.
     * @param isCheerUp is an indicator if the user wants to be cheered up by the recommendation.
     * @param genre is the String representation of a given genre.
     * @return the id of the recommended song.
     * @throws ParseException
     * @throws SpotifyWebApiException
     * @throws IOException
     */
    public String getRecommendationForGenre(Processor.Result result, boolean isCheerUp, String genre)
            throws ParseException, SpotifyWebApiException, IOException {
        final GetRecommendationsRequest getRecommendationsRequest = spotifyApi.getRecommendations()
                .limit(50)
                .seed_genres(genre)
                .target_valence(isCheerUp ? 1.0f : result.getPositiveRatio())
                .target_danceability(isCheerUp ? 1.0f : result.getNegativeRatio())
                .target_energy(result.getNeutralRatio())
                .build();
        final Recommendations recommendations = getRecommendationsRequest.execute();
        TrackSimplified track = Arrays.stream(recommendations.getTracks())
                .findAny()
                .orElse(null);
        return track == null ? "2goLsvvODILDzeeiT4dAoR" : track.getId();
    }

    /**
     * Calculates seeds for the recommendation process by looking at the top songs of the authenticated user.
     * @return an array of song IDs that are within the authenticated user's top songs.
     * @throws ParseException
     * @throws SpotifyWebApiException
     * @throws IOException
     */
    private List<String> getSeedsForUser() throws ParseException, SpotifyWebApiException, IOException {
        final GetUsersTopTracksRequest request = spotifyApi.getUsersTopTracks()
                .build();
        final Paging<Track> tracks = request.execute();
        return Arrays.stream(tracks.getItems())
                .unordered()
                .limit(5)
                .map(Track::getId)
                .collect(Collectors.toList());
    }

    /**
     * Requests audio features for a given track.
     * @param id of a given track.
     * @return list of audio features returned by the API.
     * @throws ParseException
     * @throws SpotifyWebApiException
     * @throws IOException
     */
    public AudioFeatures getAudioFeaturesForTrack(String id) throws ParseException, SpotifyWebApiException, IOException {
        final GetAudioFeaturesForTrackRequest request = spotifyApi.getAudioFeaturesForTrack(id)
                .build();
        return request.execute();

    }
}
