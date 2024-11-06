package fr.corentin.roux.x_wing_score_tracker.services;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.dao.DaoRoom;
import fr.corentin.roux.x_wing_score_tracker.model.Game;

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


    public List<Game> getAll(final Context context)
    {
        try
        {
            return daoRoom.getDatabaseAccess(context).iGameDaoRoom().findAll();
        } catch (final Throwable throwable)
        {
            return List.of();
        }
    }

    public Game getById(final Context context, final int id)
    {
        try
        {
            return daoRoom.getDatabaseAccess(context).iGameDaoRoom().findById(id);
        } catch (final Throwable throwable)
        {
            return new Game();
        }
    }

    public void save(final Context context, final Game... game)
    {
        for (Game game1 : game)
        {
            try
            {
                daoRoom.getDatabaseAccess(context).iGameDaoRoom().save(game1.serializeForSave());
            } catch (final Throwable ignored)
            {
            }
        }
    }

    public void delete(final Context context, final Game game)
    {
        try
        {
            daoRoom.getDatabaseAccess(context).iGameDaoRoom().delete(game);
        } catch (final Throwable ignored)
        {
        }
    }

}
