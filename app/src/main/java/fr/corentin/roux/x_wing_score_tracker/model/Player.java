package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

public class Player implements Persistable, Serializable {

    /**
     * Nom du joueur
     */
    private String name;

    /**
     * Score du joueur
     * - Global
     * - Kill
     * -  Mission
     */
    private Score score = new Score();

    public void addScoreKill(final int point) {
        this.score.addScoreKill(point);
    }

    public void lessScoreKill(final int point) {
        this.score.lessScoreKill(point);
    }

    public void addScoreMission(final int point) {
        this.score.addScoreMission(point);
    }

    public void lessScoreMission(final int point) {
        this.score.lessScoreMission(point);
    }

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, Score score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
