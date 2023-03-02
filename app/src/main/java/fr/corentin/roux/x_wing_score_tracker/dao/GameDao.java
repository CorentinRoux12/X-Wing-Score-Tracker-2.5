package fr.corentin.roux.x_wing_score_tracker.dao;

import android.util.Log;

import fr.corentin.roux.x_wing_score_tracker.model.Games;

public class GameDao extends ADao<Games> {
    /**
     * Nom du fichier JSON utilisé pour sauvegarder les dossiers agricultures
     */
    private static final String FILENAME = "games.json";
    /**
     * L'instance de la classe
     */
    private static GameDao instance;
    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private GameDao() {
        super(FILENAME);
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static GameDao getInstance() {
        if (instance == null) {
            instance = new GameDao();
        }
        return instance;
    }

    @Override
    Class<Games> getClassType() {
        return Games.class;
    }

    @Override
    Games defaultObject() {
        return new Games();
    }

}
