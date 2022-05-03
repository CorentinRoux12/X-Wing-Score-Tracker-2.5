package fr.corentin.roux.x_wing_score_tracker.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.N;

public class LocaleManager {
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_UKRAINIAN = "uk";
    public static final String LANGUAGE_RUSSIAN = "ru";
    private static final String LANGUAGE_KEY = "language_key";
    /**
     * L'instance de la classe
     */
    private static LocaleManager instance;
    private final SharedPreferences prefs;

    private LocaleManager(final Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static LocaleManager getInstance(final Context context) {
        if (instance == null) {
            instance = new LocaleManager(context);
        }

        return instance;
    }

    public static LocaleManager getInstance() {
        if (instance == null) {
            Log.e(LocaleManager.class.getSimpleName(), "Try get Instance on the null LocaleLanager !!");
        }
        return instance;
    }

    public static Locale getLocale(final Resources res) {
        final Configuration config = res.getConfiguration();
        return isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
    }

    public static boolean isAtLeastVersion(final int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public Context setLocale(final Context c) {
        return this.updateResources(c, this.getLanguage());
    }

    public Context setNewLocale(final Context c, final String language) {
        this.persistLanguage(language);
        return this.updateResources(c, language);
    }

    public String getLanguage() {
        return this.prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(final String language) {
        // use commit() instead of apply(), because sometimes we kill the application process
        // immediately that prevents apply() from finishing
        this.prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    private Context updateResources(Context context, final String language) {
        final Locale locale = new Locale(language);
        Locale.setDefault(locale);

        final Resources res = context.getResources();
        final Configuration config = new Configuration(res.getConfiguration());
        if (isAtLeastVersion(N)) {
            this.setLocaleForApi24(config, locale);
            context = context.createConfigurationContext(config);
        } else if (isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    @RequiresApi(api = N)
    private void setLocaleForApi24(final Configuration config, final Locale target) {
        final Set<Locale> set = new LinkedHashSet<>();
        // bring the target locale to the front of the list
        set.add(target);

        final LocaleList all = LocaleList.getDefault();
        for (int i = 0; i < all.size(); i++) {
            // append other locales supported by the user
            set.add(all.get(i));
        }

        final Locale[] locales = set.toArray(new Locale[0]);
        config.setLocales(new LocaleList(locales));
    }
}