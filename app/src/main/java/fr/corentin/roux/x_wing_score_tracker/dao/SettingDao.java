package fr.corentin.roux.x_wing_score_tracker.dao;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Games;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.utils.PersistableUtils;

public class SettingDao implements IDao<Setting> {
    /**
     * Nom du fichier JSON utilisé pour sauvegarder les dossiers agricultures
     */
    private static final String FILENAME = "settings.json";
    /**
     * L'instance de la classe
     */
    private static SettingDao instance;
    /**
     * Utilitaire permettant la lecture et l'écriture sur les fichiers
     */
    private final PersistableUtils persistableUtils = PersistableUtils.getInstance();

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private SettingDao() {
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static SettingDao getInstance() {
        if (instance == null) {
            instance = new SettingDao();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Setting> getList(final Context context) {
        final CollectionType javaType = new ObjectMapper().getTypeFactory()
                .constructCollectionType(List.class, Setting.class);
        return this.persistableUtils.get(FILENAME, javaType, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Setting findFirst(final Context context) {
        final Setting setting = this.persistableUtils.get(FILENAME, Setting.class, context);
        return setting == null ?
                new Setting() :
                setting;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final List<Setting> settings, final Context context) {
        for (Setting setting : settings) {
            this.save(setting,context);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final Setting setting, final Context context) {
        this.persistableUtils.write(FILENAME, setting, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(final Context context) {
        this.persistableUtils.write(FILENAME, new ArrayList<>(), context);
    }
}
