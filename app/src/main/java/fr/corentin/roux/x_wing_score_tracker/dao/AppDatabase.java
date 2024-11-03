package fr.corentin.roux.x_wing_score_tracker.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.utils.Converters;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;

@Database(entities = {
        Setting.class,
        Game.class
}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ISettingDaoRoom iSettingDaoRoom();

    public abstract IGameDaoRoom iGameDaoRoom();

}
