package fr.corentin.roux.x_wing_score_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Setting;

/**
 * Interface DAO pour la table 'setting' de la base de données Room.
 */
@Dao
public interface ISettingDaoRoom {

    /**
     * Récupère tous les paramètres de l'application.
     * @return Une liste d'instances de {@link Setting}.
     */
    @Query("SELECT * FROM setting")
    List<Setting> findAll();

    /**
     * Insère ou met à jour un ou plusieurs paramètres.
     * @param setting Les paramètres à enregistrer.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Setting... setting);

    /**
     * Supprime un paramètre de la base de données.
     * @param user Le paramètre à supprimer.
     */
    @Delete
    void delete(Setting user);
}
