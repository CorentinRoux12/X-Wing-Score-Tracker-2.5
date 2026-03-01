package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

/**
 * Représente un round de jeu au sein d'une partie.
 * Stocke les scores des deux joueurs, le premier joueur du round et le temps écoulé.
 */
public class Round implements Persistable, Serializable {

    /**
     * Numéro du round
     */
    private String roundNumber;

    /**
     * Score du round du Joueur1
     */
    private Score scorePlayer1;

    /**
     * Score du round du Joueur2
     */
    private Score scorePlayer2;

    /**
     * Nom du premier joueur du round
     */
    private String firstPlayer;

    /**
     * Temps du round
     */
    private long time;

    /**
     * Constructeur par défaut.
     */
    public Round() {
    }

    /**
     * Constructeur avec tous les champs.
     * @param roundNumber Le numéro du round.
     * @param scorePlayer1 Le score du joueur 1.
     * @param scorePlayer2 Le score du joueur 2.
     * @param firstPlayer Le nom du premier joueur.
     * @param time Le temps du round.
     */
    public Round(final String roundNumber, final Score scorePlayer1, final Score scorePlayer2, final String firstPlayer, final long time) {
        this.roundNumber = roundNumber;
        this.scorePlayer1 = scorePlayer1;
        this.scorePlayer2 = scorePlayer2;
        this.firstPlayer = firstPlayer;
        this.time = time;
    }

    public String getRoundNumber() {
        return this.roundNumber;
    }

    public void setRoundNumber(final String roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Score getScorePlayer1() {
        return this.scorePlayer1;
    }

    public void setScorePlayer1(final Score scorePlayer1) {
        this.scorePlayer1 = scorePlayer1;
    }

    public Score getScorePlayer2() {
        return this.scorePlayer2;
    }

    public void setScorePlayer2(final Score scorePlayer2) {
        this.scorePlayer2 = scorePlayer2;
    }

    public String getFirstPlayer() {
        return this.firstPlayer;
    }

    public void setFirstPlayer(final String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(final long time) {
        this.time = time;
    }
}
