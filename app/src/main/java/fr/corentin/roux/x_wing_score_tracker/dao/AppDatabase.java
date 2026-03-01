package fr.corentin.roux.x_wing_score_tracker.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.utils.Converters;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;

/**
 * Base de données Room de l'application.
 * Contient les tables pour les paramètres et les parties enregistrées.
 */
@Database(entities = {
        Setting.class,
        Game.class
}, version = 5) //Remember ME: A CHANGER A CHAQUE update des pojos
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    /**
     * @return Le DAO pour la gestion des paramètres.
     */
    public abstract ISettingDaoRoom iSettingDaoRoom();

    /**
     * @return Le DAO pour la gestion des parties.
     */
    public abstract IGameDaoRoom iGameDaoRoom();

}
