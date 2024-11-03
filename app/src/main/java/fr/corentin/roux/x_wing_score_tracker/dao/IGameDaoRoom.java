package fr.corentin.roux.x_wing_score_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Game;

@Dao
public interface IGameDaoRoom {

    @Query("SELECT * FROM game")
    List<Game> findAll();

    @Query("SELECT * FROM game WHERE id = :id")
    Game findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Game... games);

    @Delete
    void delete(Game game);

}
