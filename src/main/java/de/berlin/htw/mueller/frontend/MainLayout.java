package de.berlin.htw.mueller.frontend;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "Moodify",
        shortName = "Moodify",
        description = "Find songs based on your twitter moods.",
        enableInstallPrompt = false)
@Theme(value = Material.class)
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=no")
@CssImport(value = "styles/shared-styles.css")
public class MainLayout extends FlexLayout implements PageConfigurator, RouterLayout {

    private final Footer footer;

    public MainLayout() {

        setWidthFull();
        setHeightFull();

        this.footer = new Footer();
        add(footer);

        Button backButton = new Button(VaadinIcon.ARROW_BACKWARD.create());
        Button privacyButton = new Button(VaadinIcon.LOCK.create());
        Button imprintButton = new Button(VaadinIcon.ALIGN_RIGHT.create());
        footer.add(backButton, privacyButton, imprintButton);
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        LoadingIndicatorConfiguration conf = settings.getLoadingIndicatorConfiguration();
        conf.setApplyDefaultTheme(false);
    }
}
