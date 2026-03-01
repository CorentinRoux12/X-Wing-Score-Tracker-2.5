package fr.corentin.roux.x_wing_score_tracker.dao;

import android.content.Context;

import androidx.room.Room;

import java.util.Objects;

/**
 * Singleton permettant l'accès à la base de données Room.
 * Gère l'initialisation de la base de données SQLite via Room.
 */
public class DaoRoom
{

    private static DaoRoom instance;

    private AppDatabase databaseAccess;

    /**
     * Constructeur privé pour le singleton.
     */
    private DaoRoom()
    {
    }

    /**
     * @return L'unique instance de DaoRoom.
     */
    public static DaoRoom getInstance()
    {
        if (Objects.isNull(instance))
        {
            instance = new DaoRoom();
        }
        return instance;
    }


    /**
     * Initialise et retourne l'accès à la base de données.
     * @param applicationContext Le contexte de l'application.
     * @return L'instance de {@link AppDatabase}.
     */
    public AppDatabase getDatabaseAccess(final Context applicationContext)
    {
        if (Objects.isNull(databaseAccess))
        {
            databaseAccess = Room.databaseBuilder(applicationContext, AppDatabase.class, "database-name")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration(false)
                    .build();
        }
        return databaseAccess;
    }


}
