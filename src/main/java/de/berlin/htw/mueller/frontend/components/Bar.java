package de.berlin.htw.mueller.frontend.components;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class Bar extends Dialog {

    private final Div div;
    private final Label label;
    private final ProgressBar progressBar;

    /**
     *
     * @param min
     * @param max
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
     *
     */
    public Bar() {
        this(0, 100);
    }

    /**
     *
     * @param min
     */
    public void setMin(double min) {
        progressBar.setMin(min);
    }

    /**
     *
     * @param max
     */
    public void setMax(double max) {
        progressBar.setMax(max);
    }

    /**
     *
     * @param text
     */
    public void setLabel(String text) {
        label.setText(text);
    }

    /**
     *
     * @param text
     * @param value
     */
    public void set(String text, double value) {
        label.setText(text);
        progressBar.setValue(value);
    }

    /**
     *
     */
    public void increment() {
        double value = progressBar.getValue();
        progressBar.setValue(value+1);
    }

    /**
     *
     * @param value
     */
    public void setProgress(double value) {
        progressBar.setValue(value);
    }

    /**
     *
     * @return
     */
    public double getProgress() {
        return progressBar.getValue();
    }
}
