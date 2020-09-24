package de.berlin.htw.mueller._deprecated.translator;

public class Translation {
    private String german;
    private String english;

    public Translation(String german, String english) {
        this.german = german;
        this.english = english;
    }

    public String getGerman() {
        return german;
    }

    public String getEnglish() {
        return english;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "german='" + german + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
