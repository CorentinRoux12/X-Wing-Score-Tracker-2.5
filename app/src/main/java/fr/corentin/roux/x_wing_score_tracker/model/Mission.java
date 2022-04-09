package fr.corentin.roux.x_wing_score_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Mission {

    SATELLITE("Assault At The Satellite Array", 1),
    ENGAGEMENT("Chance Engagement", 2),
    SALVAGE("Salvage Mission", 3),
    SCRAMBLE("Scramble The Transmissions", 4);

    private final String libelle;

    private final int code;

    public static Mission parseCode(final int code) {
        for (final Mission mission : Mission.values()) {
            if (mission.getCode() == code) {
                return mission;
            }
        }
        return null;
    }

}
