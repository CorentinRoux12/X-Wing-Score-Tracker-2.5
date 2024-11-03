package fr.corentin.roux.x_wing_score_tracker.dao;

import android.content.Context;

import androidx.room.Room;

import java.util.Objects;

import fr.corentin.roux.x_wing_score_tracker.utils.Converters;

public class DaoRoom {

    private static DaoRoom instance;

    private AppDatabase databaseAccess;

    private DaoRoom() {
    }

    public static DaoRoom getInstance() {
        if (Objects.isNull(instance)) {
            instance = new DaoRoom();
        }
        return instance;
    }


    public AppDatabase getDatabaseAccess(final Context applicationContext) {
        if (Objects.isNull(databaseAccess)) {
            databaseAccess = Room.databaseBuilder(applicationContext, AppDatabase.class, "database-name")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseAccess;
    }


}
