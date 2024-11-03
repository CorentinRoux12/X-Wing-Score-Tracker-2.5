package fr.corentin.roux.x_wing_score_tracker.dao.old;

import android.content.Context;

import fr.corentin.roux.x_wing_score_tracker.model.Persistable;
import fr.corentin.roux.x_wing_score_tracker.utils.PersistableUtils;
import io.vavr.control.Try;

public abstract class ADao<T extends Persistable> implements IDao<T> {

    /**
     * Nom du fichier JSON utilisé pour sauvegarder les dossiers agricultures
     */
    private final String filename;

    /**
     * Utilitaire permettant la lecture et l'écriture sur les fichiers
     */
    private final PersistableUtils persistableUtils = PersistableUtils.getInstance();

    protected ADao(final String filename) {
        this.filename = filename;
    }

    abstract Class<T> getClassType();

    T defaultObject() {
        return Try.of(this::getClassType)
                .mapTry(Class::newInstance)
                .getOrNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findFirst(final Context context) {
        final T object = this.persistableUtils.get(filename, getClassType(), context);
        return object == null ?
                defaultObject() :
                object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final T object, final Context context) {
        this.persistableUtils.write(filename, object, context);
    }

}
