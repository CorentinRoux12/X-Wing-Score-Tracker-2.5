package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

public enum Mission implements Persistable, Serializable {

    NO_MISSION("No Mission", 0, "scenarios", ".pdf",0),
    SATELLITE("Assault At The Satellite Array", 1, "scenarios", ".pdf",2),
    ENGAGEMENT("Chance Engagement", 2, "scenarios", ".pdf",3),
    SALVAGE("Salvage Mission", 3, "scenarios", ".pdf",4),
    SCRAMBLE("Scramble The Transmissions", 4, "scenarios", ".pdf",5),
    KNOWLEDGE("Ancient Knowledge", 5, "scenarios", ".pdf",6);

    private final String libelle;

    private final int code;

    private final String ressource;

    private final String extension;

    private final int page;

    public static Mission parseCode(final int code) {
        for (final Mission mission : Mission.values()) {
            if (mission.getCode() == code) {
                return mission;
            }
        }
        return null;
    }

    Mission(final String libelle, final int code, final String ressource, final String extension, int page) {
        this.libelle = libelle;
        this.code = code;
        this.ressource = ressource;
        this.extension = extension;
        this.page = page;
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

    public int getPage()
    {
        return page;
    }
}
