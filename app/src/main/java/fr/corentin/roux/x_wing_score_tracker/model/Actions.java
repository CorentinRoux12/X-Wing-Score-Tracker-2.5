package fr.corentin.roux.x_wing_score_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Actions {
    END("End GAME !!"),
    DETAIL_ROUND("Details Round"),
    ADD_ROUND("Add Round"),
    ADD_POINT("Add Point"),
    REMOVE_ROUND("Less Round"),
    REMOVE_POINT("Less Point"),
    START_TIMER("Start Timer"),
    STOP_TIMER("Stop Timer");

    private final String libelle;

}
