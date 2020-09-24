package de.berlin.htw.mueller.frontend.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;

public class ExplanationParagraph extends Div {
    public ExplanationParagraph(String header, String text) {
        setClassName("explanation-paragraph");
        Label label = new Label(header);
        Span span = new Span(text);
        add(label, span);
    }
}
