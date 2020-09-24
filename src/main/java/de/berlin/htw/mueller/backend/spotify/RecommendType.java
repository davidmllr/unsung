package de.berlin.htw.mueller.backend.spotify;

public enum RecommendType {
    TRACKS("Find something based on my library."), GENRE("Show me a random Pop song!");

    private String desc;

    /**
     *
     * @param desc
     */
    RecommendType(String desc) {
        this.desc = desc;
    }

    /**
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }
}
