package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Player implements Persistable, Serializable {

    private String name;

    private int score = 0;

    private int scoreKill = 0;

    private int scoreMission = 0;

    public void addScoreKill(final int point) {
        this.scoreKill += point;
        this.updateScore();
    }

    public void lessScoreKill(final int point) {
        this.scoreKill -= point;
        if (this.scoreKill < 0) {
            this.scoreKill = 0;
        }
        this.updateScore();
    }

    public void addScoreMission(final int point) {
        this.scoreMission += point;
        this.updateScore();
    }

    public void lessScoreMission(final int point) {
        this.scoreMission -= point;
        if (this.scoreMission < 0) {
            this.scoreMission = 0;
        }
        this.updateScore();
    }

    private void updateScore() {
        this.score = this.scoreKill + this.scoreMission;
    }

    public Player() {
    }

    public Player(String name, int score, int scoreKill, int scoreMission) {
        this.name = name;
        this.score = score;
        this.scoreKill = scoreKill;
        this.scoreMission = scoreMission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreKill() {
        return scoreKill;
    }

    public void setScoreKill(int scoreKill) {
        this.scoreKill = scoreKill;
    }

    public int getScoreMission() {
        return scoreMission;
    }

    public void setScoreMission(int scoreMission) {
        this.scoreMission = scoreMission;
    }
}
