//package fr.corentin.roux.x_wing_score_tracker.services;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import fr.corentin.roux.x_wing_score_tracker.dao.DaoRoom;
//import fr.corentin.roux.x_wing_score_tracker.dao.IDao;
//import fr.corentin.roux.x_wing_score_tracker.model.Game;
//import fr.corentin.roux.x_wing_score_tracker.model.Games;
//
//public class HistoriqueService {
//    /**
//     * L'instance de la classe
//     */
//    private static HistoriqueService instance;
//
////    private final IDao<Games> repository = GameDao.getInstance();
//
//    private final DaoRoom daoRoom = DaoRoom.getInstance();
//
//    /**
//     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
//     */
//    private HistoriqueService() {
//        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
//    }
//
//    /**
//     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
//     *
//     * @return l'instance créer de la classe
//     */
//    public static HistoriqueService getInstance() {
//        if (instance == null) {
//            instance = new HistoriqueService();
//        }
//        return instance;
//    }
//
////    public void saveNewGame(final Context context, final Game game) {
////        //repository => save la game en data
////
////        Games games = this.getAllGames(context);
//////        List<Game> games = this.repository.getList(context);
////        if (games == null) {
////            games = new Games();
////        }
////        games.getGames().add(game);
////
////        this.daoRoom.getDatabaseAccess(context).iGameDaoRoom().save(games.getGames().toArray(new Game[0]));
////
////        //this.repository.save(games, context);
////    }
//
////    public Games getAllGames(final Context context) {
////        return  new Games(this.daoRoom.getDatabaseAccess(context).iGameDaoRoom().findAll());
////        this.repository.findFirst(context);
////    }
//
////    public void saveGames(final Games games, final Context context) {
////        this.daoRoom.getDatabaseAccess(context).iGameDaoRoom().save(games.getGames().toArray(new Game[0]));
//////        this.repository.save(games, context);
////    }
//}
