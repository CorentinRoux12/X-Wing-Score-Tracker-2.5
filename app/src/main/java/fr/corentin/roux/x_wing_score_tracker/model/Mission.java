package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

public enum Mission implements Persistable, Serializable {

    NO_MISSION("No Mission", 0, null, null),
    SATELLITE("Assault At The Satellite Array", 1, "assault", ".pdf"),
    ENGAGEMENT("Chance Engagement", 2, "engagement", ".pdf"),
    SALVAGE("Salvage Mission", 3, "salvage", ".pdf"),
    SCRAMBLE("Scramble The Transmissions", 4, "scramble", ".pdf");

    private final String libelle;

    private final int code;

    private final String ressource;

    private final String extension;

    public static Mission parseCode(final int code) {
        for (final Mission mission : Mission.values()) {
            if (mission.getCode() == code) {
                return mission;
            }
        }
        return null;
    }

    Mission(final String libelle, final int code, final String ressource, final String extension) {
        this.libelle = libelle;
        this.code = code;
        this.ressource = ressource;
        this.extension = extension;
    }

    public static Mission parseLibelle(final String libelle) {
        for (final Mission mission : Mission.values()) {
            if (mission.getLibelle().equals(libelle)) {
                return mission;
            }
        }
        return null;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getCode() {
        return code;
    }

    public String getRessource() {
        return ressource;
    }

    public String getExtension() {
        return extension;
    }
}
