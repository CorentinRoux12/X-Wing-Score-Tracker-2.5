package fr.corentin.roux.x_wing_score_tracker.model;

public enum Language {
    CHINOIS("zh","中国人"),
    ITALIANO("it", "Italiano"),
    SPANNISH("es", "Español"),
    DEUTSCH("de", "Deutsch"),
    FRENCH("fr", "Français"),
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
