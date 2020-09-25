package de.berlin.htw.mueller.frontend.components;

import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.TextSentiment;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import de.berlin.htw.mueller.backend.analytics.Analysis;
import org.vaadin.addon.twitter.Tweet;
import twitter4j.Status;

import java.util.Objects;

/**
 * A component which presents a tweet to the user.
 * The tweet is covered by an overlay and two icons.
 * The left icon shows the sentiment while the right icon can be used to be forwarded to the original tweet.
 */
@Tag("tweet-presenter")
public class TweetPresenter extends Component implements HasComponents {

    private final Analysis analysis;

    public TweetPresenter(Analysis analysis) {
        super();
        this.analysis = analysis;

        Status status = analysis.getStatus();
        DocumentSentiment sentiment = analysis.getSentiment();

        Div tweetContainer = new Div();
        Div overlay = new Div();
        overlay.setClassName("tweet-overlay");
        tweetContainer.setClassName("tweet-container");
        Tweet tweet = new Tweet(String.valueOf(status.getId()));
        tweet.setHeight("425px");
        tweetContainer.add(tweet);

        SentimentDetails details = new SentimentDetails(sentiment);
        details.setVisible(false);
        add(details);

        Icon sentimentIcon = getIconForSentiment(sentiment);
        sentimentIcon.setClassName("tweet-clickable-icon");
        sentimentIcon.addClickListener(e -> {
            if(e.isFromClient()) {
                details.setVisible(true);
            }
        });
        details.addClickListener(e -> {
            if(e.isFromClient()) {
                details.setVisible(false);
            }
        });

        Icon eyeIcon = VaadinIcon.EYE.create();
        eyeIcon.setClassName("tweet-clickable-icon");
        eyeIcon.addClickListener(e -> UI.getCurrent().getPage().open("https://twitter.com/user/status/" + status.getId()));

        Div icons = new Div(sentimentIcon, eyeIcon);
        icons.setClassName("tweet-icon-group");

        add(overlay, tweetContainer, icons);

    }

    /**
     * Calculates an icon for a given sentiment.
     * @param documentSentiment is a given sentiment.
     * @return an icon.
     */
    private Icon getIconForSentiment(DocumentSentiment documentSentiment) {
        TextSentiment sentiment = documentSentiment.getSentiment();
        if(Objects.equals(sentiment, TextSentiment.POSITIVE)) return VaadinIcon.SMILEY_O.create();
        else if(Objects.equals(sentiment, TextSentiment.NEGATIVE)) return VaadinIcon.FROWN_O.create();
        else if(Objects.equals(sentiment, TextSentiment.NEUTRAL)) return VaadinIcon.MEH_O.create();
        else if(Objects.equals(sentiment, TextSentiment.MIXED)) return VaadinIcon.DOT_CIRCLE.create();
        return VaadinIcon.QUESTION_CIRCLE_O.create();
    }
}
