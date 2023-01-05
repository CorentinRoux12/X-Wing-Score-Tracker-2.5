package fr.corentin.roux.x_wing_score_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum Language {
    FRENCH("fr", "French"),
    ENGLISH("en", "English");

    private final String codeLanguage;

    private final String codeIhm;

    public static Language parseCodeIhm(final String codeIhm) {
        for (final Language language : Language.values()) {
            if (language.getCodeIhm().equals(codeIhm)) {
                return language;
            }
        }
        return ENGLISH;
    }

    Language(String codeLanguage, String codeIhm) {
        this.codeLanguage = codeLanguage;
        this.codeIhm = codeIhm;
    }

    public String getCodeLanguage() {
        return codeLanguage;
    }

    public String getCodeIhm() {
        return codeIhm;
    }
}
