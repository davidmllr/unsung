package de.berlin.htw.mueller.backend.azure;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.CategorizedEntityCollection;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.KeyPhrasesCollection;
import com.azure.ai.textanalytics.models.TextAnalyticsException;
import com.azure.core.credential.AzureKeyCredential;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * This component acts as a direct connection to the Azure Text Analytics API.
 * It stores all the relevant information for authenticating.
 */
@Component
@Scope("singleton")
public class TextAnalyzer {

    private TextAnalyticsClient client;

    @Value("${azure.key}")
    private String key;

    @Value("${azure.endpoint}")
    private String endpoint;

    private final Logger logger = LoggerFactory.getLogger(TextAnalyzer.class);

    /**
     * Initializes the Java Client for the API by using the given key and endpoint.
     */
    @PostConstruct
    public void init() {
        this.client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }

    /**
     * Calculates a sentiment for a given document.
     * @param document is the String to be analyzed.
     * @return a sentiment for the given document.
     */
    @Nullable
    public DocumentSentiment getSentiment(String document) {
        if(StringUtils.isEmpty(document)) return null;
        try {
            return client.analyzeSentiment(document);
        } catch (TextAnalyticsException e) {
            logger.error("Error while analyzing document {}.", document, e);
            return null;
        }
    }

    /**
     * Calculates key phrases for a given document.
     * @param document is the String to be analyzed.
     * @return key phrases for the given document.
     */
    @Nullable
    public KeyPhrasesCollection getKeyPhrases(String document) {
        if(StringUtils.isEmpty(document)) return null;
        return client.extractKeyPhrases(document);
    }

    /**
     * Calculates categorized entities for a given document.
     * @param document is the String to be analyzed.
     * @return categorized entities for the given document.
     */
    @Nullable
    public CategorizedEntityCollection getEntities(String document) throws TextAnalyticsException {
        if (StringUtils.isEmpty(document)) return null;
        return client.recognizeEntities(document);
    }

}
