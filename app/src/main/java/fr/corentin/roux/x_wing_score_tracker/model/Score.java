package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

public class Score implements Persistable, Serializable {

    private int scoreGlobal = 0;

    private int scoreKill = 0;

    private int scoreMission = 0;

    public Score() {
    }

    public Score(int scoreGlobal, int scoreKill, int scoreMission) {
        this.scoreGlobal = scoreGlobal;
        this.scoreKill = scoreKill;
        this.scoreMission = scoreMission;
    }

    private void updateScore() {
        this.scoreGlobal = this.scoreKill + this.scoreMission;
    }

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

    public int getScoreGlobal() {
        return scoreGlobal;
    }

    public void setScoreGlobal(int scoreGlobal) {
        this.scoreGlobal = scoreGlobal;
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
