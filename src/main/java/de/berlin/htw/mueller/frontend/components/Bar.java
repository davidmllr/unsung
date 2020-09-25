package de.berlin.htw.mueller.frontend.components;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.progressbar.ProgressBar;

/**
 * Represents a small dialog with a progress bar and explanation of the current process.
 */
public class Bar extends Dialog {

    private final Div div;
    private final Label label;
    private final ProgressBar progressBar;

    /**
     * Basic constructor for the progress bar container.
     * @param min is the minimum value for the progress bar.
     * @param max is the maximum value for the progress bar.
     */
    public Bar(double min, double max) {
        super();

        this.label = new Label("Loading...");
        this.progressBar = new ProgressBar(min, max);

        this.div = new Div();
        div.getStyle().set("padding", "5px");
        div.add(label, progressBar);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setResizable(false);

        add(div);
        open();
    }

    /**
     *  Basic constructor without parameters.
     */
    public Bar() {
        this(0, 100);
    }

    /**
     * Sets a label text as well as current progress.
     * @param text is a label to be used.
     * @param value is the current progress.
     */
    public void set(String text, double value) {
        label.setText(text);
        progressBar.setValue(value);
    }
}
