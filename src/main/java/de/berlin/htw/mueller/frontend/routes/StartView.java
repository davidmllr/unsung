package de.berlin.htw.mueller.frontend.routes;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import de.berlin.htw.mueller.backend.analytics.Analysis;
import de.berlin.htw.mueller.backend.analytics.Processor;
import de.berlin.htw.mueller.backend.spotify.RecommendType;
import de.berlin.htw.mueller.backend.spotify.Spotify;
import de.berlin.htw.mueller.frontend.components.Bar;
import de.berlin.htw.mueller.frontend.helpers.Interpretation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.flow.helper.AsyncManager;
import twitter4j.*;
import twitter4j.auth.RequestToken;

import java.net.URI;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * A view which shows basic interaction possibilities to the user.
 * A user can start the recommendation process and decides about two parameters:
 * a cheer-up-option as well as a personal or a genre based recommendation.
 */
@Route(value = "start", layout = MainLayout.class)
public class StartView extends VerticalLayout {

    private final Processor processor;
    private final Twitter twitter;
    private final Spotify spotify;

    @Value("${twitter.redirect-uri}")
    private String twitterRedirectUri;

    private final Logger logger = LoggerFactory.getLogger(StartView.class);

    /**
     * Basic constructor for the view.
     * @param processor is a reference to the Processor component.
     * @param spotify is a reference to the Spotify component.
     */
    @Autowired
    public StartView(Processor processor,
                     Spotify spotify) {
        this.processor = processor;
        this.twitter = getTwitter();
        this.spotify = spotify;

        Object obj = VaadinSession.getCurrent().getAttribute("authorize.action");
        if(obj != null && (Boolean) obj) authorizeTwitter();

        setWidthFull();
        setHeightFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        Select<RecommendType> recommendTypes = new Select<>();
        recommendTypes.setItems(EnumSet.allOf(RecommendType.class));
        recommendTypes.setTextRenderer(RecommendType::getDesc);
        recommendTypes.setValue(RecommendType.TRACKS);
        recommendTypes.setId("recommendation-types-select");

        Checkbox cheerBox = new Checkbox("Cheer Me Up!");
        cheerBox.setId("cheer-up-checkbox");

        Image logo = new Image("img/logo.png", "Application Logo");
        logo.setClassName("application-logo");

        Button button = new Button("Find A Song", e -> {
            if (!authorizeSpotify() || !authorizeTwitter())
                return;

            Bar bar = new Bar();

            AsyncManager.ExceptionHandler handler = (task, e1) -> logger.error("Error in task {}.", task, e1);
            AsyncManager.getInstance().setExceptionHandler(handler);
            AsyncManager.register(this, task -> {
                task.push(() -> bar.set("Verifying credentials...", 10d));
                User user = twitter.verifyCredentials();
                task.push(() -> bar.set("Analyzing timeline...", 30d));
                Paging paging = new Paging(1, 20);
                List<Analysis> analyses = processor.analyze(twitter.getUserTimeline(paging));
                task.push(() -> bar.set("Interpreting values...", 60d));
                Processor.Result result = processor.interpret(analyses);
                task.push(() -> bar.set("Finding song...", 80d));
                String id = Objects.equals(recommendTypes.getValue(), RecommendType.TRACKS) ?
                        spotify.getRecommendation(result, cheerBox.getValue()) :
                        spotify.getRecommendationForGenre(result, cheerBox.getValue(), "pop");
                task.push(() -> bar.set("Presenting song...", 100d));
                AudioFeatures features = spotify.getAudioFeaturesForTrack(id);
                task.push(bar::close);
                task.push(() -> buildSong(id, features, result, analyses, cheerBox.getValue()));
            });
        });
        button.setId("start-button");
        add(logo, button, recommendTypes, cheerBox);
    }

    /**
     * When an interpretation is done, this function is used to transfer it to the SongView.
     * @param id is the id for the calculated song.
     * @param features are the calculated audio features.
     * @param result is the result for the analyses.
     * @param analyses is a list of analyses.
     * @param isCheerUp is an indicator if the user wants to be cheered up by the recommendation.
     */
    private void buildSong(String id,
                           AudioFeatures features,
                           Processor.Result result,
                           List<Analysis> analyses,
                           boolean isCheerUp) {
        final Interpretation interpretation =
                new Interpretation(id, features, result, analyses, isCheerUp);
        VaadinSession.getCurrent().setAttribute(Interpretation.class, interpretation);
        UI.getCurrent().navigate(SongView.class);
    }

    /**
     * Starts the Spotify authorization process if it has not happened yet.
     */
    private boolean authorizeSpotify() {

        SpotifyApi spotifyApi = spotify.getApi();
        String accessToken = spotifyApi.getAccessToken();
        String refreshToken = spotifyApi.getRefreshToken();

        if(StringUtils.isNotEmpty(accessToken) && StringUtils.isNotEmpty(refreshToken)) {
            logger.info("Spotify is already authorized.");
            return true;
        }

        URI uri = spotify.authorize();
        if(uri != null) UI.getCurrent().getPage().setLocation(uri);
        return false;
    }

    /**
     * Starts the Twitter authorization process if it has not happened yet.
     */
    private boolean authorizeTwitter() {
        if(isTwitterAuthorized()) {
            logger.info("Twitter is already authorized.");
            return true;
        }

        VaadinSession.getCurrent().setAttribute(Twitter.class, twitter);
        try {
            RequestToken requestToken = twitter.getOAuthRequestToken(twitterRedirectUri);
            VaadinSession.getCurrent().setAttribute("twitter.requestToken", requestToken);
            UI.getCurrent().getPage().setLocation(requestToken.getAuthenticationURL());
        } catch (TwitterException e) {
            logger.error("Error while obtaining twitter request token.", e);
        }

        return false;
    }


    /**
     * @return an indicator if Twitter is already authorized.
     */
    private Boolean isTwitterAuthorized() {
        Object authorized = VaadinSession.getCurrent().getAttribute("twitter.authorized");
        return authorized instanceof Boolean ? (Boolean) authorized : false;
    }

    /**
     * @return a reference to Twitter.
     */
    private Twitter getTwitter() {
        Twitter twitter = VaadinSession.getCurrent().getAttribute(Twitter.class);
        return twitter == null ? new TwitterFactory().getInstance() : twitter;
    }
}
