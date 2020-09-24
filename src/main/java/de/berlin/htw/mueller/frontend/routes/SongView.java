package de.berlin.htw.mueller.frontend.routes;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import de.berlin.htw.mueller.backend.analytics.Analysis;
import de.berlin.htw.mueller.backend.analytics.Processor;
import de.berlin.htw.mueller.backend.spotify.Explanations;
import de.berlin.htw.mueller.frontend.components.AnalysisDiv;
import de.berlin.htw.mueller.frontend.components.ExplanationParagraph;
import de.berlin.htw.mueller.frontend.components.SpotifyPlayer;
import de.berlin.htw.mueller.frontend.components.TweetPresenter;
import de.berlin.htw.mueller.frontend.helpers.Interpretation;

import java.text.MessageFormat;
import java.util.List;

@Route(value = "song", layout = MainLayout.class)
public class SongView extends VerticalLayout {

    private final String id;
    private final AudioFeatures features;
    private final Processor.Result result;
    private final List<Analysis> analyses;
    private final boolean isCheerUp;

    private final SpotifyPlayer player;
    private final Div layout;
    private final Dialog explanationDialog;

    public SongView() {

        setClassName("view");

        final Interpretation interpretation =
                VaadinSession.getCurrent().getAttribute(Interpretation.class);

        if(interpretation == null) {
            UI.getCurrent().access(() -> UI.getCurrent().navigate(StartView.class));
            this.id = null;
            this.features = null;
            this.result = null;
            this.analyses = null;
            this.isCheerUp = false;
            this.player = null;
            this.layout = null;
            this.explanationDialog = null;
            return;
        }

        this.id = interpretation.getId();
        this.features = interpretation.getFeatures();
        this.result = interpretation.getResult();
        this.analyses = interpretation.getAnalyses();
        this.isCheerUp = interpretation.isCheerUp();

        this.player = SpotifyPlayer.forId(id);
        this.layout = new Div();
        this.explanationDialog = createExplanationDialog();

        AnalysisDiv analysisDiv = new AnalysisDiv(features, explanationDialog);
        analysisDiv.setVisible(false);
        Button analysisButton = new Button(VaadinIcon.QUESTION_CIRCLE.create(), e -> {
            analysisDiv.setVisible(!analysisDiv.isVisible());
        });

        String str = MessageFormat.format("Out of your last twenty tweets and/or retweets, {0,number} tweets were positive, {1,number} were negative and {2,number} were neutral.",
                result.getPositive(), result.getNegative(), result.getNeutral());

        if (result.getMixed() > 0.0f)
            str += MessageFormat.format(" ({3,number} tweets sent mixed messages.)", result.getMixed());

        String other = MessageFormat.format("Based on this, we figured you might like a song that is at least {0,number,#.##%} valent, {1,number,#.##%} danceable and has at least {2,number,#.##%} energy.",
                result.getPositiveRatio(), result.getNegativeRatio(), result.getNeutralRatio(), result.getMixedRatio());
        String cheerStr = "Because we want to cheer you up, we were looking for a song which has a high valency, danceability and energy.";
        Button explButton = new Button(VaadinIcon.QUESTION_CIRCLE_O.create(), e -> explanationDialog.open());
        Div explanation = new Div(
                new Span(str),
                new HtmlComponent("br"),
                new HtmlComponent("br"),
                isCheerUp ? new Span(cheerStr) : new Span(other),
                explButton);
        explanation.setClassName("song-explanation");

        Tab explanationTab = new Tab("Explanation");
        Tab detailsTab = new Tab("Details");
        Tabs tabs = new Tabs(explanationTab, detailsTab);
        tabs.setSelectedTab(explanationTab);
        tabs.addSelectedChangeListener(e -> {
            if (e.getSelectedTab().equals(detailsTab)) {
                player.setHeight(80);
                layout.getStyle().remove("display");
                layout.setVisible(true);
                explanation.setVisible(false);
            } else {
                player.setHeight(380);
                layout.setVisible(false);
                explanation.setVisible(true);
            }
        });

        layout.setClassName("tweet-grid");
        analyses.forEach(analysis -> layout.add(new TweetPresenter(analysis)));
        layout.getStyle().set("display", "none");

        setWidthFull();
        setHeightFull();
        setAlignItems(Alignment.CENTER);
        getElement().getStyle().set("overflow", "hidden");

        add(analysisButton, analysisDiv, player, tabs, explanation, layout);
    }

    /**
     * @return
     */
    private Dialog createExplanationDialog() {
        VerticalLayout layout = new VerticalLayout(
                new ExplanationParagraph("Valence", Explanations.VALENCE),
                new ExplanationParagraph("Danceability", Explanations.DANCEABILITY),
                new ExplanationParagraph("Energy", Explanations.ENERGY)
        );
        layout.setHeight("50vh");
        return new Dialog(layout);
    }

}
