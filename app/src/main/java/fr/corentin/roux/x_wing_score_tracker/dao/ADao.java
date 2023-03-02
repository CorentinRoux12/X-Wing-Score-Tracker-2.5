package fr.corentin.roux.x_wing_score_tracker.dao;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Persistable;
import fr.corentin.roux.x_wing_score_tracker.utils.PersistableUtils;

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

    abstract T defaultObject();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getList(final Context context) {
        final CollectionType javaType = new ObjectMapper().getTypeFactory()
                .constructCollectionType(List.class, getClassType());
        return this.persistableUtils.get(filename, javaType, context);
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
    public void save(final List<T> objects, final Context context) {
        for (T object : objects) {
            this.save(object,context);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final T object, final Context context) {
        this.persistableUtils.write(filename, object, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(final Context context) {
        this.persistableUtils.write(filename, new ArrayList<>(), context);
    }
}
