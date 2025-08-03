//package fr.corentin.roux.x_wing_score_tracker.dao;
//
//import android.util.Log;
//
//import fr.corentin.roux.x_wing_score_tracker.model.Setting;
//
//public class SettingDao extends ADao<Setting> {
//    /**
//     * Nom du fichier JSON utilisé pour sauvegarder les dossiers agricultures
//     */
//    private static final String FILENAME = "settings.json";
//    /**
//     * L'instance de la classe
//     */
//    private static SettingDao instance;
//
//    /**
//     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
//     */
//    private SettingDao() {
//        super(FILENAME);
//        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
//    }
//
//    /**
//     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
//     *
//     * @return l'instance créer de la classe
//     */
//    public static SettingDao getInstance() {
//        if (instance == null) {
//            instance = new SettingDao();
//        }
//        return instance;
//    }
//
//
//    @Override
//    Class<Setting> getClassType() {
//        return Setting.class;
//    }
//
//    @Override
//    Setting defaultObject() {
//        return new Setting();
//    }
//}
