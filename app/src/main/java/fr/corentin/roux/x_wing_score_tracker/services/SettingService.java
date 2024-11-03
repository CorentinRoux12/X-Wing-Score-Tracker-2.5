package fr.corentin.roux.x_wing_score_tracker.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Optional;

import fr.corentin.roux.x_wing_score_tracker.dao.DaoRoom;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class SettingService
{

    /**
     * L'instance de la classe
     */
    private static SettingService instance;

    private final DaoRoom daoRoom = DaoRoom.getInstance();

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private SettingService()
    {
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static SettingService getInstance()
    {
        if (instance == null)
        {
            instance = new SettingService();
        }
        return instance;
    }

    public Setting get(final Context context)
    {
        return Try.of(() -> this.daoRoom.getDatabaseAccess(context).iSettingDaoRoom().findAll().stream()
                        .findFirst())
                .get()
                .orElse(new Setting());
    }

    public void save(final Context context, final Setting setting)
    {
        Try.of(() -> daoRoom.getDatabaseAccess(context))
                .andThenTry(t -> t.iSettingDaoRoom().save(setting));
    }


}
