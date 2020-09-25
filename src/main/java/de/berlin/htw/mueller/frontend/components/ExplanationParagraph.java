package de.berlin.htw.mueller.frontend.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;

/**
 * Represents a paragraph which is used for explanation of audio features.
 */
public class ExplanationParagraph extends Div {
    /**
     * Basic constructor for a paragraph.
     * @param header is the name of the audio feature.
     * @param text is the description of the audio feature.
     */
    public ExplanationParagraph(String header, String text) {
        setClassName("explanation-paragraph");
        Label label = new Label(header);
        Span span = new Span(text);
        add(label, span);
    }
}
