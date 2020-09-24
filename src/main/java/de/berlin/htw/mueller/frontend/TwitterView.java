package de.berlin.htw.mueller.frontend;

import com.azure.ai.textanalytics.models.CategorizedEntityCollection;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.KeyPhrasesCollection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.berlin.htw.mueller.backend.azure.TextAnalyzer;
import de.berlin.htw.mueller.backend.twitter.TweetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.twitter.Tweet;
import twitter4j.*;
import twitter4j.auth.RequestToken;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "twitter", layout = MainLayout.class)
public class TwitterView extends VerticalLayout {

    private final Twitter twitter;
    private final TextAnalyzer textAnalyzer;

    private final boolean authorized;
    private final Logger logger = LoggerFactory.getLogger(TwitterView.class);

    @Autowired
    public TwitterView(TextAnalyzer textAnalyzer) throws TwitterException {

        this.textAnalyzer = textAnalyzer;
        this.twitter = getTwitter();
        this.authorized = isAuthorized();

        if (!authorized) {
            authorize();
            return;
        }

        showTimeline();
    }

    /**
     * @throws TwitterException
     */
    public void authorize() throws TwitterException {
        VaadinSession.getCurrent().setAttribute(Twitter.class, twitter);
        RequestToken requestToken = twitter.getOAuthRequestToken();
        System.out.println("Authorization URL: \n"
                + requestToken.getAuthorizationURL());

        VaadinSession.getCurrent().setAttribute("requestToken", requestToken);
        UI.getCurrent().getPage().setLocation(requestToken.getAuthenticationURL());
    }

    /**
     * @return
     */
    public void showTimeline() throws TwitterException {
        User user = twitter.verifyCredentials();
        List<Status> statuses = twitter.getFavorites();

        Grid<Status> grid = new Grid<>();
        grid.addComponentColumn(status -> new Tweet(String.valueOf(status.getId())));
        grid.addColumn(status -> {
            DocumentSentiment sentiment = textAnalyzer.getSentiment(TweetUtils.clean(status.getText()));
            return sentiment == null ? null : sentiment.getSentiment();
        }).setHeader("Sentiments").setResizable(true);
        grid.addColumn(status -> {
            KeyPhrasesCollection keys = textAnalyzer.getKeyPhrases(TweetUtils.clean(status.getText()));
            if(keys == null) return null;
            return keys.stream().collect(Collectors.joining(" / "));
        }).setHeader("Key Phrases").setResizable(true);
        grid.addColumn(status -> {
            CategorizedEntityCollection entities = textAnalyzer.getEntities(TweetUtils.clean(status.getText()));
            if(entities == null) return null;
            return entities.stream().map(entity -> entity.getText() + "[" + entity.getCategory() + "]")
                    .collect(Collectors.joining(" / "));
        }).setHeader("Entities").setResizable(true);

        grid.setItems(statuses);
        add(grid);
    }

    /**
     * @return
     */
    private Twitter getTwitter() {
        Twitter twitter = VaadinSession.getCurrent().getAttribute(Twitter.class);
        return twitter == null ? TwitterFactory.getSingleton() : twitter;
    }

    /**
     * @return
     */
    private Boolean isAuthorized() {
        Object authorized = VaadinSession.getCurrent().getAttribute("authorized");
        return authorized instanceof Boolean ? (Boolean) authorized : false;
    }
}
