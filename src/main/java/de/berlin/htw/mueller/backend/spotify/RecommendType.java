package de.berlin.htw.mueller.backend.spotify;

/**
 * This enum is used to determine whether a recommendation process should be based on personal top tracks or a given genre.
 */
public enum RecommendType {
    TRACKS("Find something based on my library."), GENRE("Show me a random Pop song!");

    private String desc;

    /**
     * Basic constructor for the enum.
     * @param desc Description of the enum value to be used e.g. in a ComboBox.
     */
    RecommendType(String desc) {
        this.desc = desc;
    }

    /**
     *
     * @return description of the enum value.
     */
    public String getDesc() {
        return desc;
    }
}
