package de.berlin.htw.mueller.frontend.helpers;

import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import de.berlin.htw.mueller.backend.analytics.Analysis;
import de.berlin.htw.mueller.backend.analytics.Processor;

import java.util.List;

public class Interpretation {
    private String id;
    private AudioFeatures features;
    private Processor.Result result;
    private List<Analysis> analyses;
    private boolean isCheerUp;

    public Interpretation(String id, AudioFeatures features, Processor.Result result, List<Analysis> analyses, boolean isCheerUp) {
        this.id = id;
        this.features = features;
        this.result = result;
        this.analyses = analyses;
        this.isCheerUp = isCheerUp;
    }

    public String getId() {
        return id;
    }

    public AudioFeatures getFeatures() {
        return features;
    }

    public Processor.Result getResult() {
        return result;
    }

    public List<Analysis> getAnalyses() {
        return analyses;
    }

    public boolean isCheerUp() {
        return isCheerUp;
    }
}
