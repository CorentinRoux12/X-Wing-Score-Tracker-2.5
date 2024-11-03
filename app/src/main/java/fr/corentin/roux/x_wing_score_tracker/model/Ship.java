package fr.corentin.roux.x_wing_score_tracker.model;

import androidx.room.Ignore;

import java.io.Serializable;

public class Ship implements Persistable, Serializable {
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


    public Ship(String name, Integer points, Statut statut) {
        this.name = name;
        this.points = points;
        this.statut = statut;
    }

    public Ship() {
    }

    @Ignore
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

    public enum Statut implements Persistable, Serializable {
        FULL,
        HALF,
        DEAD;
    }
}
