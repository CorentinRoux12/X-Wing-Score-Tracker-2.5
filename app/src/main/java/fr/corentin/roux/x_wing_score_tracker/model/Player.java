package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

/**
 * Énumération identifiant les deux joueurs d'une partie.
 */
public enum Player implements Serializable, Persistable
{
    /** Joueur 1 */
    ONE,
    /** Joueur 2 */
    TWO
}
