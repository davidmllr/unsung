package de.berlin.htw.mueller.backend.analytics;

import com.azure.ai.textanalytics.models.CategorizedEntityCollection;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.KeyPhrasesCollection;
import twitter4j.Status;

import java.io.Serializable;

/**
 * POJO that is used for storing results of an analysis by the Azure Text Sentiment API.
 */
public class Analysis implements Serializable {
    private Status status;
    private DocumentSentiment sentiment;
    private KeyPhrasesCollection keyPhrases;
    private CategorizedEntityCollection categorizedEntities;

    /**
     * Basic constructor for the class.
     * @param status holds a tweet.
     * @param sentiment is the calculated sentiment.
     * @param keyPhrases contains calculated key phrases.
     * @param categorizedEntities contains calculated categorized entities.
     */
    public Analysis(Status status,
                    DocumentSentiment sentiment,
                    KeyPhrasesCollection keyPhrases,
                    CategorizedEntityCollection categorizedEntities) {
        this.status = status;
        this.sentiment = sentiment;
        this.keyPhrases = keyPhrases;
        this.categorizedEntities = categorizedEntities;
    }

    /**
     *
     * @return the status of the analysis.
     */
    public Status getStatus() {
        return status;
    }

    /**
     *
     * @return the sentiment of the analysis.
     */
    public DocumentSentiment getSentiment() {
        return sentiment;
    }

    /**
     *
     * @return the key phrases of the analysis.
     */
    public KeyPhrasesCollection getKeyPhrases() {
        return keyPhrases;
    }

    /**
     *
     * @return the categorized entities of the analysis.
     */
    public CategorizedEntityCollection getCategorizedEntities() {
        return categorizedEntities;
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "status=" + status +
                ", sentiment=" + sentiment +
                ", keyPhrases=" + keyPhrases +
                ", categorizedEntities=" + categorizedEntities +
                '}';
    }
}
