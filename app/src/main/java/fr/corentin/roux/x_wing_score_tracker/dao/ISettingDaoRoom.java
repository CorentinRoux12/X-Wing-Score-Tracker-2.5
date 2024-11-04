package fr.corentin.roux.x_wing_score_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Setting;

@Dao
public interface ISettingDaoRoom {

    @Query("SELECT * FROM setting")
    List<Setting> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Setting... setting);

    @Delete
    void delete(Setting user);
}
