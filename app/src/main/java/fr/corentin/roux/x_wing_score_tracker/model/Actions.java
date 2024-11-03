package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

public enum Actions implements Persistable, Serializable {
    END("End GAME !!"),
    DETAIL_ROUND("Details Round"),
    ADD_ROUND("Add Round"),
    ADD_POINT("Add Point"),
    REMOVE_ROUND("Less Round"),
    REMOVE_POINT("Less Point"),
    FIRST_PLAYER("First Player"),
    START_TIMER("Start Timer"),
    STOP_TIMER("Stop Timer");

    private final String libelle;

    Actions(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
