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

@Component
public class Processor {

    private final TextAnalyzer textAnalyzer;
    private final Logger logger = LoggerFactory.getLogger(Processor.class);

    /**
     *
     * @param textAnalyzer
     */
    @Autowired
    public Processor(TextAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    /**
     *
     * @param states
     * @return
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
     *
     * @param analyses
     * @return
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
     *
     */
    public class Result {
        private float positive;
        private float negative;
        private float neutral;
        private float mixed;

        /**
         *
         * @param positive
         * @param negative
         * @param neutral
         * @param mixed
         */
        public Result(float positive, float negative, float neutral, float mixed) {
            this.positive = positive;
            this.negative = negative;
            this.neutral = neutral;
            this.mixed = mixed;
        }

        /**
         *
         * @return
         */
        public float total() {
            return positive + negative + neutral + mixed;
        }

        /**
         *
         * @return
         */
        public float getPositiveRatio() {
            return positive/total();
        }

        /**
         *
         * @return
         */
        public float getNegativeRatio() {
            return negative/total();
        }

        /**
         *
         * @return
         */
        public float getNeutralRatio() {
            return neutral/total();
        }

        /**
         *
         * @return
         */
        public float getMixedRatio() {
            return mixed/total();
        }


        /**
         *
         * @return
         */
        public float getPositive() {
            return positive;
        }

        /**
         *
         * @return
         */
        public float getNegative() {
            return negative;
        }

        /**
         *
         * @return
         */
        public float getNeutral() {
            return neutral;
        }

        /**
         *
         * @return
         */
        public float getMixed() {
            return mixed;
        }
    }
}
