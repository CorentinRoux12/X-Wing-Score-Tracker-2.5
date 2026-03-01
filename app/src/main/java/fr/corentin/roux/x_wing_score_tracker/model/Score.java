package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

/**
 * Représente le score d'un joueur, composé des points de destruction (kill) 
 * et des points de mission.
 */
public class Score implements Persistable, Serializable {

    private int scoreGlobal = 0;

    private int scoreKill = 0;

    private int scoreMission = 0;

    /**
     * Constructeur par défaut.
     */
    public Score() {
    }

    /**
     * Constructeur avec initialisation des scores.
     * @param scoreGlobal Score total.
     * @param scoreKill Points de destruction.
     * @param scoreMission Points de mission.
     */
    public Score(int scoreGlobal, int scoreKill, int scoreMission) {
        this.scoreGlobal = scoreGlobal;
        this.scoreKill = scoreKill;
        this.scoreMission = scoreMission;
    }

    /**
     * Met à jour le score global en sommant les points de kill et de mission.
     */
    private void updateScore() {
        this.scoreGlobal = this.scoreKill + this.scoreMission;
    }

    /**
     * Ajoute des points de destruction.
     * @param point Points à ajouter.
     */
    public void addScoreKill(final int point) {
        this.scoreKill += point;
        this.updateScore();
    }

    /**
     * Retire des points de destruction (minimum 0).
     * @param point Points à retirer.
     */
    public void lessScoreKill(final int point) {
        this.scoreKill -= point;
        if (this.scoreKill < 0) {
            this.scoreKill = 0;
        }
        this.updateScore();
    }

    /**
     * Ajoute des points de mission.
     * @param point Points à ajouter.
     */
    public void addScoreMission(final int point) {
        this.scoreMission += point;
        this.updateScore();
    }

    /**
     * Retire des points de mission (minimum 0).
     * @param point Points à retirer.
     */
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
