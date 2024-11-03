package fr.corentin.roux.x_wing_score_tracker.utils;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import fr.corentin.roux.x_wing_score_tracker.model.Persistable;
import io.vavr.control.Try;

public class PersistableUtils {

    /**
     * L'instance de la classe
     */
    private static PersistableUtils instance;
    /**
     * Mapper Jackson pour les objets
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private PersistableUtils() {
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static PersistableUtils getInstance() {
        if (instance == null) {
            instance = new PersistableUtils();
        }
        return instance;
    }

    /**
     * Permet de récupérer une liste de l'objet passé en param, dans le fichier ciblé
     *
     * @param filename le nom du fichier dans lequel on va lire les datas
     * @param clazz    le type d'objet a remonté par la méthode
     * @param <T>      le type paramétrable qui doit être un {@link Persistable}
     * @return Un objet {@link Persistable} dans le fichier {@field filename}
     */
    public <T extends Persistable> T get(final String filename, final Class<T> clazz, final Context context) {
        return Try.of(() -> filename)
                .map(file -> new File(context.getCacheDir(), file))
                .map(t -> {
                    try {
                        return this.mapper.readValue(t.toURI().toURL(), clazz);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
//                .mapTry(context::openFileInput)
//                .mapTry(t -> this.mapper.reader().readValue(t, clazz))
                .onFailure(throwable -> Log.e(this.getClass().getSimpleName(), "Erreur lors de la récupération des données.", throwable))
                .getOrNull();
    }

    /**
     * Permet d'écrire un objet passé en param, dans le fichier ciblé
     *
     * @param filename le nom du fichier dans lequel on va écrire les datas
     * @param object   un objet qui est de la classe {@link Persistable} à écrire dans le fichier ciblé
     * @param <T>      le type paramétrable qui doit être un {@link Persistable}
     */
    public <T extends Persistable> void write(final String filename, final T object, final Context context) {
        this.writeJsonToFileSystem(filename, object, context);
    }

    /**
     * Permet d'écrire un Object dans le fichier ciblé
     *
     * @param filename le nom du fichier choisit
     * @param object   l'object à écrire
     */
    private <T> void writeJsonToFileSystem(final String filename, final T object, final Context context) {
        Try.of(() -> filename)
                .andThen(file -> {
                    try {
                        this.mapper.writeValue(new File(context.getCacheDir(), file), object);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
//                .map(file -> new File(context.getCacheDir(), file))
//                .mapTry(t -> this.mapper.writer().writeValue(t.toURI().toURL(), object))
//                .mapTry(file -> context.openFileOutput(file, Context.MODE_PRIVATE))
//                .andThenTry(t -> this.mapper.writer().writeValue(t, object))
                .onFailure(throwable -> Log.e(this.getClass().getSimpleName(), "Erreur lors de l'écriture du fichier json", throwable));
    }
}
