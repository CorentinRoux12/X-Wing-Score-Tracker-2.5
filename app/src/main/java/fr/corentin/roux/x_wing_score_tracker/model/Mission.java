package fr.corentin.roux.x_wing_score_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Mission {

    SATELLITE("Assault At The Satellite Array", 1, "assault.pdf"),
    ENGAGEMENT("Chance Engagement", 2, "engagement.pdf"),
    SALVAGE("Salvage Mission", 3, "salvage.pdf"),
    SCRAMBLE("Scramble The Transmissions", 4, "scramble.pdf");

    private final String libelle;

    private final int code;

    private final String ressource;

    public static Mission parseCode(final int code) {
        for (final Mission mission : Mission.values()) {
            if (mission.getCode() == code) {
                return mission;
            }
        }
        return null;
    }

}