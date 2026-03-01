package fr.corentin.roux.x_wing_score_tracker.services;

import android.content.Context;
import android.util.Log;

import fr.corentin.roux.x_wing_score_tracker.dao.DaoRoom;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;

/**
 * Service gérant la logique métier pour les paramètres (Setting).
 * Permet de récupérer et de sauvegarder les préférences utilisateur en base de données.
 */
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

    /**
     * Récupère les paramètres actuels de l'application.
     * @param context Le contexte de l'application.
     * @return L'objet {@link Setting} contenant les préférences ou un nouvel objet par défaut.
     */
    public Setting get(final Context context)
    {
        try
        {
            return this.daoRoom.getDatabaseAccess(context).iSettingDaoRoom().findAll().stream()
                    .findFirst()
                    .get();
        } catch (final Throwable throwable)
        {
            return new Setting();
        }
    }

    /**
     * Sauvegarde les paramètres en base de données.
     * @param context Le contexte de l'application.
     * @param setting L'objet {@link Setting} à enregistrer.
     */
    public void save(final Context context, final Setting setting)
    {
        try
        {
            this.daoRoom.getDatabaseAccess(context).iSettingDaoRoom().save(setting);
        } catch (Throwable ignored)
        {
            //Nothing to do
        }
    }
}
