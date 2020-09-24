package de.berlin.htw.mueller.frontend.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;

import java.text.NumberFormat;
import java.util.Locale;

public class AnalysisDiv extends Div {
    private final AudioFeatures features;
    private final Dialog explanationDialog;

    /**
     *
     * @param features
     * @param explanationDialog
     */
    public AnalysisDiv(AudioFeatures features, Dialog explanationDialog) {

        this.features = features;
        this.explanationDialog = explanationDialog;

        setClassName("analysis-div");
        Div left = new Div();
        left.setClassName("left");

        Div valence = new Div(new Label("Valence"), new Span(percentage(features.getValence(), Locale.ENGLISH)));
        Div danceability = new Div(new Label("Danceability"), new Span(percentage(features.getDanceability(), Locale.ENGLISH)));
        Div energy = new Div(new Label("Energy"), new Span(percentage(features.getEnergy(), Locale.ENGLISH)));
        Button button = new Button(VaadinIcon.QUESTION_CIRCLE.create(), e -> explanationDialog.open());

        left.add(valence, danceability, energy);
        add(left, button);
    }


    /**
     *
     * @param value
     * @param locale
     * @return
     */
    public static String percentage(double value, Locale locale) {
        NumberFormat nf = NumberFormat.getPercentInstance(locale);
        return nf.format(value);
    }
}
