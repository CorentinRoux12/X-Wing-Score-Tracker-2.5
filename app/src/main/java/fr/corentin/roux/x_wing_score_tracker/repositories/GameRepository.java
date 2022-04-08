package fr.corentin.roux.x_wing_score_tracker.repositories;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.utils.PersistableUtils;

public class GameRepository implements IRepository<Game> {
    /**
     * Nom du fichier JSON utilisé pour sauvegarder les dossiers agricultures
     */
    private static final String FILENAME = "game.json";
    /**
     * L'instance de la classe
     */
    private static GameRepository instance;
    /**
     * Utilitaire permettant la lecture et l'écriture sur les fichiers
     */
    private final PersistableUtils persistableUtils = PersistableUtils.getInstance();

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private GameRepository() {
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static GameRepository getInstance() {
        if (instance == null) {
            instance = new GameRepository();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> getList(final Context context) {
        final CollectionType javaType = new ObjectMapper().getTypeFactory()
                .constructCollectionType(List.class, Game.class);
        return this.persistableUtils.get(FILENAME, javaType, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final List<Game> t, final Context context) {
        this.persistableUtils.write(FILENAME, t, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final Game t, final Context context) {
        this.persistableUtils.write(FILENAME, t, context);
    }
}
