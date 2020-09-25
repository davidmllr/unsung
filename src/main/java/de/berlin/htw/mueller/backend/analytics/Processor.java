package de.berlin.htw.mueller.backend.analytics;

import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.TextSentiment;
import de.berlin.htw.mueller.backend.azure.TextAnalyzer;
import de.berlin.htw.mueller.backend.twitter.TweetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Processor is a Spring Component that acts as a simple service for using the Azure Text Analytics API.
 */
@Component
public class Processor {

    private final TextAnalyzer textAnalyzer;
    private final Logger logger = LoggerFactory.getLogger(Processor.class);

    /**
     * Basic constructor.
     * @param textAnalyzer is the class that is directly connected to Azure.
     */
    @Autowired
    public Processor(TextAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    /**
     * Performs analyses for the given tweets.
     * @param states represent a list of tweets.
     * @return a list of analyses.
     */
    public List<Analysis> analyze(List<Status> states) {
        return states.stream()
                .map(status -> {
                    String text = TweetUtils.clean(status.getText());
                    DocumentSentiment sentiment = textAnalyzer.getSentiment(text);
                    if(sentiment == null) return null;
                    return new Analysis(
                            status,
                            sentiment,
                            null,
                            null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Interprets a given list of analyses by adding up the respective sentiments.
     * @param analyses are a list of given analyses that have been processed by analyze() before.
     * @return a result for the interpretation.
     */
    public Result interpret(List<Analysis> analyses) {
        float positive = 0, negative = 0, neutral = 0, mixed = 0;

        for(Analysis analysis : analyses) {
            DocumentSentiment documentSentiment = analysis.getSentiment();
            if(documentSentiment == null) continue;

            TextSentiment sentiment = documentSentiment.getSentiment();
            if(Objects.equals(sentiment, TextSentiment.POSITIVE)) positive++;
            else if(Objects.equals(sentiment, TextSentiment.NEGATIVE)) negative++;
            else if(Objects.equals(sentiment, TextSentiment.NEUTRAL)) neutral++;
            else if(Objects.equals(sentiment, TextSentiment.MIXED)) positive++;

        }

        logger.info("Positive: {} / Negative: {} / Neutral: {} / Mixed: {}",
                positive, negative, neutral, mixed);

        return new Result(positive, negative, neutral, mixed);
    }

    /**
     * A simple POJO that holds the result for an interpretation of analyses.
     */
    public class Result {
        private float positive;
        private float negative;
        private float neutral;
        private float mixed;

        /**
         * A basic constructor for a Result.
         * @param positive is the number of positive sentiments.
         * @param negative is the number of negative sentiments.
         * @param neutral is the number of neutral sentiments.
         * @param mixed is the number of mixed sentiments.
         */
        public Result(float positive, float negative, float neutral, float mixed) {
            this.positive = positive;
            this.negative = negative;
            this.neutral = neutral;
            this.mixed = mixed;
        }

        /**
         *
         * @return the total number of sentiments.
         */
        public float total() {
            return positive + negative + neutral + mixed;
        }

        /**
         *
         * @return the number of positive sentiments divided by the number of total sentiments.
         */
        public float getPositiveRatio() {
            return positive/total();
        }

        /**
         *
         * @return the number of negative sentiments divided by the number of total sentiments.
         */
        public float getNegativeRatio() {
            return negative/total();
        }

        /**
         *
         * @return the number of neutral sentiments divided by the number of total sentiments.
         */
        public float getNeutralRatio() {
            return neutral/total();
        }

        /**
         *
         * @return the number of mixed sentiments divided by the number of total sentiments.
         */
        public float getMixedRatio() {
            return mixed/total();
        }

        /**
         *
         * @return the number of positive sentiments.
         */
        public float getPositive() {
            return positive;
        }

        /**
         *
         * @return the number of negative sentiments.
         */
        public float getNegative() {
            return negative;
        }

        /**
         *
         * @return the number of neutral sentiments.
         */
        public float getNeutral() {
            return neutral;
        }

        /**
         *
         * @return the number of mixed sentiments.
         */
        public float getMixed() {
            return mixed;
        }
    }
}
