package fr.corentin.roux.x_wing_score_tracker.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Convertisseurs personnalisés pour Room.
 * Permet de stocker des listes de chaînes de caractères en les convertissant en JSON.
 */
public class Converters
{
    /**
     * Constructeur privé pour la classe utilitaire.
     */
    private Converters()
    {
        //Nothing to do
    }

    /**
     * Convertit une chaîne JSON en liste de chaînes.
     *
     * @param value La chaîne JSON à convertir.
     * @return Une liste de {@link String}.
     */
    @TypeConverter
    public static List<String> fromString(String value)
    {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    /**
     * Convertit une liste de chaînes en format JSON.
     *
     * @param list La liste à convertir.
     * @return Une chaîne JSON.
     */
    @TypeConverter
    public static String fromArrayList(List<String> list)
    {
        return new Gson().toJson(list);
    }
}
