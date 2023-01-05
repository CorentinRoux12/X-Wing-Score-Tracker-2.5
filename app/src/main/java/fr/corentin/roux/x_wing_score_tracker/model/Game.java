package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Game implements Persistable, Serializable {

    private Player player1 = new Player();
    private Player player2 = new Player();
    private long timeLeft;
    private int round = 0;
    private String historique;
    private String date;
    private Mission mission = Mission.NO_MISSION;
    private boolean hideTimeLeft;
    private boolean hideTimer;

    public void removeRound() {
        this.round--;
    }

    public void addRound() {
        this.round++;
    }

    public Game() {
    }

    public Game(Player player1, Player player2, long timeLeft, int round, String historique, String date, Mission mission, boolean hideTimeLeft, boolean hideTimer) {
        this.player1 = player1;
        this.player2 = player2;
        this.timeLeft = timeLeft;
        this.round = round;
        this.historique = historique;
        this.date = date;
        this.mission = mission;
        this.hideTimeLeft = hideTimeLeft;
        this.hideTimer = hideTimer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
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

    public String getHistorique() {
        return historique;
    }

    public void setHistorique(String historique) {
        this.historique = historique;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
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
}
