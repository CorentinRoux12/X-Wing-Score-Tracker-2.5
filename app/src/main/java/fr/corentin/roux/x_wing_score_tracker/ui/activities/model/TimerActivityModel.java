package fr.corentin.roux.x_wing_score_tracker.ui.activities.model;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.model.DiceTurn;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Round;
import fr.corentin.roux.x_wing_score_tracker.model.Score;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;

public class TimerActivityModel implements Serializable {

    private Setting setting;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private boolean end = false;
    private boolean timerStart = false;
    private long timeToSet;
    private Game game;

    private int firstPlayerChoice = 0;
    private boolean alreadyEnd = false;
    private Score scoreRoundJoueur1 = new Score();
    private Score scoreRoundJoueur2 = new Score();
    private long timeStartRound = 0;
    private DiceTurn diceTurn = new DiceTurn();


    public void updateScoreRound() {
        final List<Round> rounds = this.game.getRounds();
        if (!rounds.isEmpty()) {
            int cptG1 = 0;
            int cptK1 = 0;
            int cptM1 = 0;
            int cptG2 = 0;
            int cptK2 = 0;
            int cptM2 = 0;
            for (final Round round : rounds) {
                cptG1 += round.getScorePlayer1().getScoreGlobal();
                cptK1 += round.getScorePlayer1().getScoreKill();
                cptM1 += round.getScorePlayer1().getScoreMission();
                cptG2 += round.getScorePlayer2().getScoreGlobal();
                cptK2 += round.getScorePlayer2().getScoreKill();
                cptM2 += round.getScorePlayer2().getScoreMission();
            }
            cptG1 = game.getScoreGlobalPlayer1() - cptG1;
            cptK1 = game.getScoreKillPlayer1() - cptK1;
            cptM1 = game.getScoreMissionPlayer1() - cptM1;
            cptG2 = game.getScoreGlobalPlayer2() - cptG2;
            cptK2 = game.getScoreKillPlayer2() - cptK2;
            cptM2 = game.getScoreMissionPlayer2() - cptM2;

            scoreRoundJoueur1.setScoreGlobal(cptG1);
            scoreRoundJoueur1.setScoreKill(cptK1);
            scoreRoundJoueur1.setScoreMission(cptM1);
            scoreRoundJoueur2.setScoreGlobal(cptG2);
            scoreRoundJoueur2.setScoreKill(cptK2);
            scoreRoundJoueur2.setScoreMission(cptM2);
        } else {
            scoreRoundJoueur1.setScoreGlobal(game.getScoreGlobalPlayer1());
            scoreRoundJoueur1.setScoreKill(game.getScoreKillPlayer1());
            scoreRoundJoueur1.setScoreMission(game.getScoreMissionPlayer1());
            scoreRoundJoueur2.setScoreGlobal(game.getScoreGlobalPlayer2());
            scoreRoundJoueur2.setScoreKill(game.getScoreKillPlayer2());
            scoreRoundJoueur2.setScoreMission(game.getScoreMissionPlayer2());
        }
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isTimerStart() {
        return timerStart;
    }

    public void setTimerStart(boolean timerStart) {
        this.timerStart = timerStart;
    }

    public long getTimeToSet() {
        return timeToSet;
    }

    public void setTimeToSet(long timeToSet) {
        this.timeToSet = timeToSet;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getFirstPlayerChoice() {
        return firstPlayerChoice;
    }

    public void setFirstPlayerChoice(int firstPlayerChoice) {
        this.firstPlayerChoice = firstPlayerChoice;
    }

    public boolean isAlreadyEnd() {
        return alreadyEnd;
    }

    public void setAlreadyEnd(boolean alreadyEnd) {
        this.alreadyEnd = alreadyEnd;
    }

    public Score getScoreRoundJoueur1() {
        return scoreRoundJoueur1;
    }

    public void setScoreRoundJoueur1(Score scoreRoundJoueur1) {
        this.scoreRoundJoueur1 = scoreRoundJoueur1;
    }

    public Score getScoreRoundJoueur2() {
        return scoreRoundJoueur2;
    }

    public void setScoreRoundJoueur2(Score scoreRoundJoueur2) {
        this.scoreRoundJoueur2 = scoreRoundJoueur2;
    }

    public long getTimeStartRound() {
        return timeStartRound;
    }

    public void setTimeStartRound(long timeStartRound) {
        this.timeStartRound = timeStartRound;
    }

    public DiceTurn getDiceTurn()
    {
        return diceTurn;
    }

    public void setDiceTurn(DiceTurn diceTurn)
    {
        this.diceTurn = diceTurn;
    }
}
