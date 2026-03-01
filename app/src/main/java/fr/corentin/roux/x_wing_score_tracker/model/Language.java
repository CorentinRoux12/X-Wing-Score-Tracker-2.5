package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

/**
 * Énumération des langues supportées par l'application.
 * Contient les codes de langue ISO et les libellés pour l'interface utilisateur.
 */
public enum Language implements Persistable, Serializable {
    POLSKI("pl","Polski"),
    CHINOIS("zh","中国人"),
    ITALIANO("it", "Italiano"),
    SPANNISH("es", "Español"),
    DEUTSCH("de", "Deutsch"),
    FRENCH("fr", "Français"),
    ENGLISH("en", "English");

    private final String codeLanguage;

    private final String codeIhm;

    /**
     * Recherche une langue à partir de son libellé IHM.
     * @param codeIhm Le libellé à rechercher.
     * @return La langue correspondante ou ENGLISH par défaut.
     */
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
