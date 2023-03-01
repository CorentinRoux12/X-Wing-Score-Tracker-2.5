package fr.corentin.roux.x_wing_score_tracker.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Persistable;

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
     * @param javaType le type de collection a remonté par la méthode
     * @param <T>      le type paramétrable qui doit être un {@link Persistable}
     * @return Une liste d'objet {@link Persistable} dans le fichier {@field filename}
     */
    public <T extends Persistable> List<T> get(final String filename, final CollectionType javaType, final Context context) {
        List<T> result = null;
        try {
            result = new ObjectMapper().readValue(context.openFileInput(filename), javaType);
        } catch (final IOException e) {
            Log.e(this.getClass().getSimpleName(), "Erreur lors de la récupération de liste des données");
        }
        return result;
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
        T result = null;
        try {
//            T cacheFile = this.mapper.readValue(new File(context.getCacheDir(), filename), clazz);
//            result = cacheFile;
            result = new ObjectMapper().readValue(context.openFileInput(filename), clazz);
        } catch (final IOException e) {
            try {
                this.gett(filename, clazz, context);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            Log.e(this.getClass().getSimpleName(), "Erreur lors de la récupération des données");
        }
        return result;
    }

    public <T extends Persistable> T gett(final String filename,final Class<T> clazz, final Context context) throws FileNotFoundException {
        FileInputStream fis = context.openFileInput(filename);
        InputStreamReader inputStreamReader =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            String content = stringBuilder.toString();

            try {
                return new ObjectMapper().readValue(content,clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Permet d'écrire une liste de l'objet passé en param, dans le fichier ciblé
     *
     * @param filename le nom du fichier dans lequel on va écrire les datas
     * @param list     une liste d objet <T> qui doit etre ecrit dans le fichier ciblé
     * @param <T>      le type paramétrable qui doit être un {@link Persistable}
     */
    public <T extends Persistable> void write(final String filename, final List<T> list, final Context context) {
        this.writeJsonToFileSystem(filename, list, context);
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
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            new ObjectMapper().writer().writeValue(fos, object);
        } catch (final IOException e) {
            Log.e(this.getClass().getSimpleName(), "Erreur lors de l'écriture du fichier json");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (final IOException e) {
                Log.e(this.getClass().getSimpleName(), "Erreur lors de l' écriture du fichier json");
            }
        }
    }

//    private void write(final Context context){
//        String filename = "myfile";
//        String fileContents = "Hello world!";
//        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
//            fos.write(fileContents.toByteArray());
//        }
//    }

}
