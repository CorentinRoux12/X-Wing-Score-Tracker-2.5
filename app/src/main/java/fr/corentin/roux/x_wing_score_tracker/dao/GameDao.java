package fr.corentin.roux.x_wing_score_tracker.dao;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Games;
import fr.corentin.roux.x_wing_score_tracker.utils.PersistableUtils;

public class GameDao implements IDao<Games> {
    /**
     * Nom du fichier JSON utilisé pour sauvegarder les dossiers agricultures
     */
    private static final String FILENAME = "game.json";
    /**
     * L'instance de la classe
     */
    private static GameDao instance;
    /**
     * Utilitaire permettant la lecture et l'écriture sur les fichiers
     */
    private final PersistableUtils persistableUtils = PersistableUtils.getInstance();

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private GameDao() {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Games> getList(final Context context) {
        final CollectionType javaType = new ObjectMapper().getTypeFactory()
                .constructCollectionType(List.class, Game.class);
        return this.persistableUtils.get(FILENAME, javaType, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Games findFirst(final Context context) {
        final Games games = this.persistableUtils.get(FILENAME, Games.class, context);
        return games == null ?
                new Games() :
                games;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final List<Games> games, final Context context) {
        for (Games game : games) {
            this.save(game,context);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final Games game, final Context context) {
        this.persistableUtils.write(FILENAME, game, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(final Context context) {
        this.persistableUtils.write(FILENAME, new ArrayList<>(), context);
    }
}
