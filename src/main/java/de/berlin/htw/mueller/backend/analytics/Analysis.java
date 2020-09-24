package de.berlin.htw.mueller.backend.analytics;

import com.azure.ai.textanalytics.models.CategorizedEntityCollection;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.KeyPhrasesCollection;
import twitter4j.Status;

import java.io.Serializable;

public class Analysis implements Serializable {
    private Status status;
    private DocumentSentiment sentiment;
    private KeyPhrasesCollection keyPhrases;
    private CategorizedEntityCollection categorizedEntities;

    public Analysis(Status status,
                    DocumentSentiment sentiment,
                    KeyPhrasesCollection keyPhrases,
                    CategorizedEntityCollection categorizedEntities) {
        this.status = status;
        this.sentiment = sentiment;
        this.keyPhrases = keyPhrases;
        this.categorizedEntities = categorizedEntities;
    }

    public Status getStatus() {
        return status;
    }

    public DocumentSentiment getSentiment() {
        return sentiment;
    }

    public KeyPhrasesCollection getKeyPhrases() {
        return keyPhrases;
    }

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
