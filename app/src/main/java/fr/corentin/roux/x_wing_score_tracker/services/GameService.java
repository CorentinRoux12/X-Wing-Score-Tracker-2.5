package fr.corentin.roux.x_wing_score_tracker.services;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.dao.DaoRoom;
import fr.corentin.roux.x_wing_score_tracker.model.Game;

/**
 * Service gérant la logique métier pour les parties (Game).
 * Assure la liaison entre l'interface utilisateur et la couche de persistance.
 */
public class GameService
{

    /**
     * L'instance de la classe
     */
    private static GameService instance;

    private final DaoRoom daoRoom = DaoRoom.getInstance();

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private GameService()
    {
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static GameService getInstance()
    {
        if (instance == null)
        {
            instance = new GameService();
        }
        return instance;
    }


    /**
     * Récupère la liste de toutes les parties enregistrées.
     * @param context Le contexte de l'application.
     * @return Une liste d'objets {@link Game}.
     */
    public List<Game> getAll(final Context context)
    {
        try
        {
            return daoRoom.getDatabaseAccess(context).iGameDaoRoom().findAll();
        } catch (final Exception throwable)
        {
            return List.of();
        }
    }

    /**
     * Enregistre une ou plusieurs parties en base de données après sérialisation.
     * @param context Le contexte de l'application.
     * @param game Les parties à sauvegarder.
     */
    public void save(final Context context, final Game... game)
    {
        for (Game game1 : game)
        {
            try
            {
                daoRoom.getDatabaseAccess(context).iGameDaoRoom().save(game1.serializeForSave());
            } catch (final Exception ignored)
            {
                //Nothing to do
            }
        }
    }

    /**
     * Supprime une partie de la base de données.
     * @param context Le contexte de l'application.
     * @param game La partie à supprimer.
     */
    public void delete(final Context context, final Game game)
    {
        try
        {
            daoRoom.getDatabaseAccess(context).iGameDaoRoom().delete(game);
        } catch (final Exception ignored)
        {
            //Nothing to do
        }
    }

}
