package de.berlin.htw.mueller.frontend.components;

import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;

import java.util.Collections;
import java.util.List;

public class SentimentDetails extends Div {
    private final DocumentSentiment sentiment;

    public SentimentDetails(DocumentSentiment sentiment) {
        this.sentiment = sentiment;
        setClassName("tweet-sentiment-details");


        double score = getHighestConfidenceScore();
        Html html = new Html("<p>This tweet is <b>" + sentiment.getSentiment() + "</b> with a probability of <b>" + score + "</b></p>");
        add(html);
    }

    /**
     *
     * @return
     */
    private double getHighestConfidenceScore() {
        if(sentiment == null)
            return 0.0d;

        SentimentConfidenceScores scores = sentiment.getConfidenceScores();
        List<Double> list = List.of(
                scores.getPositive(), scores.getNegative(), scores.getNeutral());
        return Collections.max(list);
    }
}
