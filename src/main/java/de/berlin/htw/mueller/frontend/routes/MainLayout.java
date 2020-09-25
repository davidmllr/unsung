package de.berlin.htw.mueller.frontend.routes;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;


/**
 * Representation for the layout of the app.
 * It handles PWA attributes and constructs the footer.
 */
@Push
@Route
@PWA(name = "Unsung",
        shortName = "Unsung",
        description = "Turn your Twitter moods into songs.")
@Theme(value = Material.class)
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=no")
@CssImport(value = "styles/shared-styles.css")
public class MainLayout extends FlexLayout implements PageConfigurator, BeforeEnterObserver, RouterLayout {

    private final Footer footer;
    private final Button backButton;

    /**
     * Basic constructor for the main layout.
     */
    public MainLayout() {

        setWidthFull();
        setHeightFull();

        this.footer = new Footer();
        add(footer);

        this.backButton = new Button(VaadinIcon.ARROW_BACKWARD.create(), e -> {
            History history = UI.getCurrent().getPage().getHistory();
            history.go(-1);
        });
        Button privacyButton = new Button(VaadinIcon.INFO_CIRCLE.create(), e -> {
            Span span = new Span("Here should be an explanation and a privacy policy for the app.");
            new Dialog(span).open();
        });
        Button imprintButton = new Button(VaadinIcon.ALIGN_RIGHT.create(), e -> {
            Span span = new Span("Here should be the imprint of the app's creator (me).");
            new Dialog(span).open();
        });
        footer.add(backButton, privacyButton, imprintButton);
    }

    /**
     * Disables the standard loading indicator for the application.
     * @param settings are page settings.
     */
    @Override
    public void configurePage(InitialPageSettings settings) {
        LoadingIndicatorConfiguration conf = settings.getLoadingIndicatorConfiguration();
        conf.setApplyDefaultTheme(false);
    }

    /**
     * Ensures that the back button in the footer only works if a user is in the SongView.
     * @param event is an event that can be used to determine the navigation target of the user.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(event.getNavigationTarget() == SongView.class)
            backButton.setEnabled(true);
        else backButton.setEnabled(false);
    }
}
