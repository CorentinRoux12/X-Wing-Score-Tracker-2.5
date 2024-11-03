package fr.corentin.roux.x_wing_score_tracker.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Game implements Persistable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    private final ArrayList<Round> rounds = new ArrayList<>();
    @Ignore
    private ArrayList<DiceTurn> diceTurns = new ArrayList<>();

    private String roundsJson = "";
    private String diceJson = "";

    //    private Player player1 = new Player("Player 1");
//    private Player player2 = new Player("Player 2");
    private long timeLeft;
    private int round = 0;
    private String date;
    private String mission = Mission.NO_MISSION.getLibelle();
    private boolean hideTimeLeft;
    private boolean hideTimer;

    private String namePlayer1 = "Player 1";
    private int scoreGlobalPlayer1 = 0;
    private int scoreKillPlayer1 = 0;
    private int scoreMissionPlayer1 = 0;
    private String xwsShipsPlayer1 = "";

    private String namePlayer2 = "Player 2";
    private int scoreGlobalPlayer2 = 0;
    private int scoreKillPlayer2 = 0;
    private int scoreMissionPlayer2 = 0;
    private String xwsShipsPlayer2 = "";




    public Game serializeForSave() {
        this.roundsJson = new Gson().toJson(this.rounds);
        this.diceJson = new Gson().toJson(this.diceTurns);
        return this;
    }

    public void removeRound() {
        this.round--;
    }

    public void addRound() {
        this.round++;
    }

    public void addScoreKill(final String namePlayer, final int point) {
        if (!namePlayer.equals(namePlayer1)) {
            this.addScoreKillPlayer1(point);
        } else {
            this.addScoreKillPlayer2(point);
        }
    }

    public void lessScoreKill(final String namePlayer, final int point) {
        if (!namePlayer.equals(namePlayer1)) {
            this.lessScoreKillPlayer1(point);
        } else {
            this.lessScoreKillPlayer2(point);
        }
    }

    public void addScoreMission(final String namePlayer, final int point) {
        if (!namePlayer.equals(namePlayer1)) {
            this.addScoreMissionPlayer1(point);
        } else {
            this.addScoreMissionPlayer2(point);
        }
    }

    public void lessScoreMission(final String namePlayer, final int point) {
        if (!namePlayer.equals(namePlayer1)) {
            this.lessScoreMissionPlayer1(point);
        } else {
            this.lessScoreMissionPlayer2(point);
        }
    }

    //////////////////--------------------Player1--------------------//////////////////

    private void updateScorePlayer1() {
        this.scoreGlobalPlayer1 = this.scoreKillPlayer1 + this.scoreMissionPlayer1;
    }

    public void addScoreKillPlayer1(final int point) {
        this.scoreKillPlayer1 += point;
        this.updateScorePlayer1();
    }

    public void lessScoreKillPlayer1(final int point) {
        this.scoreKillPlayer1 -= point;
        if (this.scoreKillPlayer1 < 0) {
            this.scoreKillPlayer1 = 0;
        }
        this.updateScorePlayer1();
    }

    public void addScoreMissionPlayer1(final int point) {
        this.scoreMissionPlayer1 += point;
        this.updateScorePlayer1();
    }

    public void lessScoreMissionPlayer1(final int point) {
        this.scoreMissionPlayer1 -= point;
        if (this.scoreMissionPlayer1 < 0) {
            this.scoreMissionPlayer1 = 0;
        }
        this.updateScorePlayer1();
    }

    //////////////////--------------------Player2--------------------//////////////////

    private void updateScorePlayer2() {
        this.scoreGlobalPlayer2 = this.scoreKillPlayer2 + this.scoreMissionPlayer2;
    }

    public void addScoreKillPlayer2(final int point) {
        this.scoreKillPlayer2 += point;
        this.updateScorePlayer2();
    }

    public void lessScoreKillPlayer2(final int point) {
        this.scoreKillPlayer2 -= point;
        if (this.scoreKillPlayer2 < 0) {
            this.scoreKillPlayer2 = 0;
        }
        this.updateScorePlayer2();
    }

    public void addScoreMissionPlayer2(final int point) {
        this.scoreMissionPlayer2 += point;
        this.updateScorePlayer2();
    }

    public void lessScoreMissionPlayer2(final int point) {
        this.scoreMissionPlayer2 -= point;
        if (this.scoreMissionPlayer2 < 0) {
            this.scoreMissionPlayer2 = 0;
        }
        this.updateScorePlayer2();
    }


    @Ignore
    public Game(int id, ArrayList<DiceTurn> diceTurns, String roundsJson, String diceJson, long timeLeft, int round, String date, String mission, boolean hideTimeLeft, boolean hideTimer, String namePlayer1, int scoreGlobalPlayer1, int scoreKillPlayer1, int scoreMissionPlayer1, String xwsShipsPlayer1, String namePlayer2, int scoreGlobalPlayer2, int scoreKillPlayer2, int scoreMissionPlayer2, String xwsShipsPlayer2)
    {
        this.id = id;
        this.diceTurns = diceTurns;
        this.roundsJson = roundsJson;
        this.diceJson = diceJson;
        this.timeLeft = timeLeft;
        this.round = round;
        this.date = date;
        this.mission = mission;
        this.hideTimeLeft = hideTimeLeft;
        this.hideTimer = hideTimer;
        this.namePlayer1 = namePlayer1;
        this.scoreGlobalPlayer1 = scoreGlobalPlayer1;
        this.scoreKillPlayer1 = scoreKillPlayer1;
        this.scoreMissionPlayer1 = scoreMissionPlayer1;
        this.xwsShipsPlayer1 = xwsShipsPlayer1;
        this.namePlayer2 = namePlayer2;
        this.scoreGlobalPlayer2 = scoreGlobalPlayer2;
        this.scoreKillPlayer2 = scoreKillPlayer2;
        this.scoreMissionPlayer2 = scoreMissionPlayer2;
        this.xwsShipsPlayer2 = xwsShipsPlayer2;
    }


    public Game() {
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public boolean isHideTimeLeft() {
        return hideTimeLeft;
    }

    public void setHideTimeLeft(boolean hideTimeLeft) {
        this.hideTimeLeft = hideTimeLeft;
    }

    public boolean isHideTimer() {
        return hideTimer;
    }

    public void setHideTimer(boolean hideTimer) {
        this.hideTimer = hideTimer;
    }

    public String getNamePlayer1() {
        return namePlayer1;
    }

    public void setNamePlayer1(String namePlayer1) {
        this.namePlayer1 = namePlayer1;
    }

    public int getScoreGlobalPlayer1() {
        return scoreGlobalPlayer1;
    }

    public void setScoreGlobalPlayer1(int scoreGlobalPlayer1) {
        this.scoreGlobalPlayer1 = scoreGlobalPlayer1;
    }

    public int getScoreKillPlayer1() {
        return scoreKillPlayer1;
    }

    public void setScoreKillPlayer1(int scoreKillPlayer1) {
        this.scoreKillPlayer1 = scoreKillPlayer1;
    }

    public int getScoreMissionPlayer1() {
        return scoreMissionPlayer1;
    }

    public void setScoreMissionPlayer1(int scoreMissionPlayer1) {
        this.scoreMissionPlayer1 = scoreMissionPlayer1;
    }

    public String getXwsShipsPlayer1() {
        return xwsShipsPlayer1;
    }

    public void setXwsShipsPlayer1(String xwsShipsPlayer1) {
        this.xwsShipsPlayer1 = xwsShipsPlayer1;
    }

    public String getNamePlayer2() {
        return namePlayer2;
    }

    public void setNamePlayer2(String namePlayer2) {
        this.namePlayer2 = namePlayer2;
    }

    public int getScoreGlobalPlayer2() {
        return scoreGlobalPlayer2;
    }

    public void setScoreGlobalPlayer2(int scoreGlobalPlayer2) {
        this.scoreGlobalPlayer2 = scoreGlobalPlayer2;
    }

    public int getScoreKillPlayer2() {
        return scoreKillPlayer2;
    }

    public void setScoreKillPlayer2(int scoreKillPlayer2) {
        this.scoreKillPlayer2 = scoreKillPlayer2;
    }

    public int getScoreMissionPlayer2() {
        return scoreMissionPlayer2;
    }

    public void setScoreMissionPlayer2(int scoreMissionPlayer2) {
        this.scoreMissionPlayer2 = scoreMissionPlayer2;
    }

    public String getXwsShipsPlayer2() {
        return xwsShipsPlayer2;
    }

    public void setXwsShipsPlayer2(String xwsShipsPlayer2) {
        this.xwsShipsPlayer2 = xwsShipsPlayer2;
    }

    public String getRoundsJson() {
        return roundsJson;
    }

    public void setRoundsJson(String roundsJson) {
        this.roundsJson = roundsJson;
    }

    public ArrayList<DiceTurn> getDiceTurns()
    {
        return diceTurns;
    }

    public void setDiceTurns(ArrayList<DiceTurn> diceTurns)
    {
        this.diceTurns = diceTurns;
    }

    public String getDiceJson()
    {
        return diceJson;
    }

    public void setDiceJson(String diceJson)
    {
        this.diceJson = diceJson;
    }
}
