package fr.corentin.roux.x_wing_score_tracker.repositories;

import android.content.Context;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Persistable;

public interface IRepository<T extends Persistable> {
    /**
     * Permet de récupérer les objects présent dans la liste la sauvegarde
     *
     * @return la liste des objects qui sont présent dans le fichier de sauvegarde
     */
    List<T> getList(final Context context);

    /**
     * Permet de sauvegarder une liste d'objects
     *
     * @param t la liste des objects a save
     */
    void save(final List<T> t, Context context);

    /**
     * Permet de sauvegarder un object
     *
     * @param t l'objects a save
     */
    void save(final T t, Context context);

}
