package fr.corentin.roux.x_wing_score_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Game;

/**
 * Interface DAO pour la table 'game' de la base de données Room.
 */
@Dao
public interface IGameDaoRoom {

    /**
     * Récupère toutes les parties enregistrées.
     * @return Une liste de toutes les instances de {@link Game}.
     */
    @Query("SELECT * FROM game")
    List<Game> findAll();

    /**
     * Récupère une partie par son identifiant unique.
     * @param id L'identifiant de la partie.
     * @return La partie correspondante ou null.
     */
    @Query("SELECT * FROM game WHERE id = :id")
    Game findById(int id);

    /**
     * Insère ou met à jour une ou plusieurs parties.
     * @param games Les parties à enregistrer.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Game... games);

    /**
     * Supprime une partie de la base de données.
     * @param game La partie à supprimer.
     */
    @Delete
    void delete(Game game);

}
