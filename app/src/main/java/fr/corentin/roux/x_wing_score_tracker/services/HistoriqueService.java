package fr.corentin.roux.x_wing_score_tracker.services;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.repositories.GameRepository;
import fr.corentin.roux.x_wing_score_tracker.repositories.IRepository;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.TimerActivity;

public class HistoriqueService {
    /**
     * L'instance de la classe
     */
    private static HistoriqueService instance;

    private final IRepository<Game> repository = GameRepository.getInstance();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Constructeur privé de la classe permettant de bloquer l'instanciation depuis l'extérieure de la classe
     */
    private HistoriqueService() {
        Log.d(this.getClass().getSimpleName(), "Génération du singleton.");
    }

    /**
     * Permet de créer une instance de la classe si elle n'existe pas déjà et la récupérer
     *
     * @return l'instance créer de la classe
     */
    public static HistoriqueService getInstance() {
        if (instance == null) {
            instance = new HistoriqueService();
        }
        return instance;
    }

    public void saveNewGame(final TimerActivity timerActivity) {
        //repository => save la game en data
        final Game game = new Game(timerActivity.getScorePlayerOne(), timerActivity.getScorePlayerTwo(), timerActivity.getTimeToSet(),
                timerActivity.getRound(), timerActivity.getHistorique().toString(), this.dateFormat.format(new Date()));

        List<Game> games = this.repository.getList(timerActivity.getBaseContext());
        if (games == null) {
            games = new ArrayList<>();
        }
        games.add(game);

        this.repository.save(games, timerActivity.getBaseContext());
    }

    public List<Game> getAllGames(final Context context) {
        return this.repository.getList(context);
    }

    public void saveGames(final List<Game> games, final Context context) {
        this.repository.save(games, context);
    }
}
