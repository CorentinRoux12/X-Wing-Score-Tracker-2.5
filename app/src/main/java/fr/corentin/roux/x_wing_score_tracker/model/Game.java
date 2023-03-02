package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private final List<Round> rounds = new ArrayList<>();
    private Player player1 = new Player("Player 1");
    private Player player2 = new Player("Player 2");
    private long timeLeft;
    private int round = 0;
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

    public List<Round> getRounds() {
        return rounds;
    }
}
