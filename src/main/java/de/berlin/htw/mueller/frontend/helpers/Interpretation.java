package de.berlin.htw.mueller.frontend.helpers;

import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import de.berlin.htw.mueller.backend.analytics.Analysis;
import de.berlin.htw.mueller.backend.analytics.Processor;

import java.util.List;

/**
 * Simple POJO that is used to transfer data between sessions.
 * It holds all the information that is used to present analysis results to the user.
 */
public class Interpretation {
    private String id;
    private AudioFeatures features;
    private Processor.Result result;
    private List<Analysis> analyses;
    private boolean isCheerUp;

    /**
     * Constructor that creates the interpretation.
     * @param id is the id of a given song.
     * @param features are the calculated audio features.
     * @param result is the result of the analyses.
     * @param analyses is a list of analyses.
     * @param isCheerUp is an indicator if the user wants to be cheered up by the recommendation.
     */
    public Interpretation(String id,
                          AudioFeatures features,
                          Processor.Result result,
                          List<Analysis> analyses,
                          boolean isCheerUp) {
        this.id = id;
        this.features = features;
        this.result = result;
        this.analyses = analyses;
        this.isCheerUp = isCheerUp;
    }

    /**
     *
     * @return the id of a given song.
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return the audio features for a given song.
     */
    public AudioFeatures getFeatures() {
        return features;
    }

    /**
     *
     * @return the result of the analyses.
     */
    public Processor.Result getResult() {
        return result;
    }

    /**
     *
     * @return the analyses.
     */
    public List<Analysis> getAnalyses() {
        return analyses;
    }

    /**
     *
     * @return an indicator if the user wants to be cheered up by the recommendation.
     */
    public boolean isCheerUp() {
        return isCheerUp;
    }

    @Override
    public String toString() {
        return "Interpretation{" +
                "id='" + id + '\'' +
                ", features=" + features +
                ", result=" + result +
                ", analyses=" + analyses +
                ", isCheerUp=" + isCheerUp +
                '}';
    }
}
