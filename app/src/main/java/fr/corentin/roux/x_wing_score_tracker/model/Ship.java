package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

public class Ship implements Serializable {

    String name;

    Integer points;

    Statut statut = Statut.FULL;

    public Statut changeStatut() {
        if (this.statut.equals(Statut.FULL)){
            statut = Statut.HALF;
            return Statut.FULL;
        } else if (this.statut.equals(Statut.HALF)){
            statut = Statut.DEAD;
            return Statut.HALF;
        } else {
            statut = Statut.FULL;
            return Statut.DEAD;
        }
    }

    public enum Statut{
        FULL,
        HALF,
        DEAD;
    }

    public Ship() {
    }

    public Ship(String name, Integer points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}
