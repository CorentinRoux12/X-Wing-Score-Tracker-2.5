package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe de base abstraite pour toutes les activités de l'application.
 * Définit le cycle d'initialisation standard (View, Data, Listeners).
 */
public abstract class AbstractActivity extends AppCompatActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //On définit la vue qui sera utilisée pour l'affichage
        this.initContentView();
        //Initialisation des données de base
        this.initGame();
        //Liaison entre le XML et les champs java
        this.findView();
        //Initialisation des données de la page
        this.initDatas();
        //Initialisation des adaptateurs
        this.initAdapters();
        //Initialisation des écouteurs
        this.initListeners();
        //Initialisation des valeurs par défaut post-listeners
        this.initDefaultValue();
    }

    /**
     * Initialisation des valeurs par défaut après la mise en place des listeners.
     */
    protected void initDefaultValue() {
    }

    /**
     * Définit le layout de l'activité (setContentView).
     */
    protected abstract void initContentView();

    /**
     * Initialisation des données de base (services, objets métier) avant le mapping des vues.
     */
    protected void initGame() {
    }

    /**
     * Charge et mappe tous les composants graphiques à partir du XML.
     */
    protected abstract void findView();

    /**
     * Initialisation de tous les adaptateurs nécessaires à l'activité.
     */
    protected void initAdapters() {
    }

    /**
     * Initialisation des données à l'intérieur des composants graphiques.
     */
    protected void initDatas() {
    }

    /**
     * Initialisation des écouteurs d'événements (onClick, etc.) de la page.
     */
    protected void initListeners() {
    }

}
