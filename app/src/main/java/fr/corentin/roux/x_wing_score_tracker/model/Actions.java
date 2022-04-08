package fr.corentin.roux.x_wing_score_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Actions {
    ADD_ROUND("Round_ADD"),
    ADD_POINT("Point_ADD"),
    REMOVE_ROUND("Round_LESS"),
    REMOVE_POINT("Point_LESS"),
    START_TIMER("Start_Timer"),
    STOP_TIMER("Stop_Timer");

    private final String libelle;

}
