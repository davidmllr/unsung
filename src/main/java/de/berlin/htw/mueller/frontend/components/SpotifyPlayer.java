package de.berlin.htw.mueller.frontend.components;

import com.vaadin.flow.component.html.IFrame;

public class SpotifyPlayer extends IFrame {
    private SpotifyPlayer(String src) {
        super(src);
        setAllow("encrypted-media");
        getElement().setAttribute("allowtransparency", "true");
        getElement().setAttribute("width", "300");
        getElement().setAttribute("height", "380");
        getElement().setAttribute("frameborder", "0");
    }

    /**
     *
     * @param width
     */
    public void setWidth(int width) {
        getElement().setAttribute("width", String.valueOf(width));
    }

    /**
     *
     * @param height
     */
    public void setHeight(int height) {
        getElement().setAttribute("height", String.valueOf(height));
    }

    /**
     *
     * @param id
     * @return
     */
    public static SpotifyPlayer forId(String id) {
        return new SpotifyPlayer("https://open.spotify.com/embed/track/" + id);
    }
}
