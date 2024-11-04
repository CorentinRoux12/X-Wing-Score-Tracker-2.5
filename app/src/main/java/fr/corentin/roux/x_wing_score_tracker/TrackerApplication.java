package fr.corentin.roux.x_wing_score_tracker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

public class TrackerApplication extends Application {

    /**
     * L'instance de la classe
     */
    private static TrackerApplication instance;

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private TrackerApplication() {
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static TrackerApplication getInstance() {
        if (instance == null) {
            instance = new TrackerApplication();
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

}
