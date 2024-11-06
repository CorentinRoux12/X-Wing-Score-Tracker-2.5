package fr.corentin.roux.x_wing_score_tracker.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import fr.corentin.roux.x_wing_score_tracker.model.Language;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;

public class LocaleHelper
{

    public static Context checkDefaultLanguage(Setting setting, Context context)
    {
        final Locale locale = getLocaleFromSettings(setting);
        Locale.setDefault(locale);

        final Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Locale getLocaleFromSettings(Setting setting)
    {
        Language language;
        if (setting.getLanguage() == null)
        {
            language = Language.ENGLISH;
        } else
        {
            language = Language.parseCodeIhm(setting.getLanguage());
        }

        switch (language)
        {
            case FRENCH:
                return Locale.FRENCH;
            case ITALIANO:
                return Locale.ITALIAN;
            case SPANNISH:
                return new Locale("es");
            case DEUTSCH:
                return Locale.GERMAN;
            case CHINOIS:
                return Locale.SIMPLIFIED_CHINESE;
            default:
                return Locale.ENGLISH;
        }
    }
}
