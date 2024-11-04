package fr.corentin.roux.x_wing_score_tracker.dao.old;

import android.content.Context;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Persistable;

public interface IDao<T extends Persistable> {

    /**
     * Permet de récupérer les objects présent dans la liste la sauvegarde
     *
     * @return la liste des objects qui sont présent dans le fichier de sauvegarde
     */
    T findFirst(final Context context);

    /**
     * Permet de sauvegarder un object
     *
     * @param t l'objects a save
     */
    void save(final T t, Context context);

}
