package de.berlin.htw.mueller.frontend.routes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

/**
 * Represents an empty route which is forwared to the StartView.
 */
@Route(value = "", layout = MainLayout.class)
public class DefaultView extends VerticalLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.forwardTo(StartView.class);
    }
}
