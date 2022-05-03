package fr.corentin.roux.x_wing_score_tracker.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    public static void updateResources(final Context context, final String language) {
        final Locale locale = new Locale(language);
        Locale.setDefault(locale);

        final Resources res = context.getResources();
        final Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
