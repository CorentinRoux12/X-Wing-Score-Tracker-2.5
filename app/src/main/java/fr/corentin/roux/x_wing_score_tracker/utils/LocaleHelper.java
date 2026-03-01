package fr.corentin.roux.x_wing_score_tracker.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;
import java.util.Optional;

import fr.corentin.roux.x_wing_score_tracker.model.Language;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;

/**
 * Classe utilitaire pour la gestion de la localisation (langue) de l'application.
 * Permet de forcer une langue spécifique à partir des paramètres utilisateur.
 */
public class LocaleHelper
{
    /**
     * Constructeur privé pour la classe utilitaire.
     */
    private LocaleHelper()
    {
        //Nothing to do
    }

    /**
     * Vérifie et applique la langue configurée dans les paramètres au contexte donné.
     *
     * @param setting Les paramètres contenant la langue choisie.
     * @param context Le contexte de base.
     * @return Un nouveau contexte configuré avec la bonne {@link Locale}.
     */
    public static Context checkDefaultLanguage(Setting setting, Context context)
    {
        final Locale locale = getLocaleFromSettings(setting);
        Locale.setDefault(locale);

        final Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    /**
     * Détermine l'objet Locale à partir de la langue enregistrée dans les paramètres.
     */
    private static Locale getLocaleFromSettings(Setting setting)
    {
        final Language language = Optional.ofNullable(setting.getLanguage())
                .map(Language::parseCodeIhm)
                .orElse(Language.ENGLISH);

        return switch (language)
        {
            case FRENCH -> Locale.FRENCH;
            case ITALIANO -> Locale.ITALIAN;
            case SPANNISH -> new Locale("es");
            case DEUTSCH -> Locale.GERMAN;
            case CHINOIS -> Locale.SIMPLIFIED_CHINESE;
            case POLSKI -> new Locale("pl");
            default -> Locale.ENGLISH;
        };
    }
}
