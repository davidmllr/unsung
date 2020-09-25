package de.berlin.htw.mueller.frontend.components;

import com.vaadin.flow.component.html.IFrame;

/**
 * A simple wrapper for the Spotify iframe.
 */
public class SpotifyPlayer extends IFrame {
    /**
     * Basic constructor that initializes a new iframe which contains a Spotify player.
     * @param src is the URL for a given song.
     */
    private SpotifyPlayer(String src) {
        super(src);
        setAllow("encrypted-media");
        getElement().setAttribute("allowtransparency", "true");
        getElement().setAttribute("width", "300");
        getElement().setAttribute("height", "380");
        getElement().setAttribute("frameborder", "0");
    }

    /**
     * Sets the width for the player.
     * @param width is a given width.
     */
    public void setWidth(int width) {
        getElement().setAttribute("width", String.valueOf(width));
    }

    /**
     * Sets the height for the player.
     * @param height is a given height.
     */
    public void setHeight(int height) {
        getElement().setAttribute("height", String.valueOf(height));
    }

    /**
     * Convenience method to create a Spotify player for a song ID.
     * @param id is the given song id.
     * @return a reference to a Spotify player.
     */
    public static SpotifyPlayer forId(String id) {
        return new SpotifyPlayer("https://open.spotify.com/embed/track/" + id);
    }
}
