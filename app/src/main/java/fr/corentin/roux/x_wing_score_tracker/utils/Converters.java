package fr.corentin.roux.x_wing_score_tracker.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters
{

    private Converters()
    {
        //Nothing to do
    }

    @TypeConverter
    public static List<String> fromString(String value)
    {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<String> list)
    {
        return new Gson().toJson(list);
    }
}