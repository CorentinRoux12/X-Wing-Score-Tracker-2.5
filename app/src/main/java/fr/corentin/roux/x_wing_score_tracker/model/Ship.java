package fr.corentin.roux.x_wing_score_tracker.model;

import androidx.room.Ignore;

import java.io.Serializable;

/**
 * Représente un vaisseau avec son nom, ses points et son statut actuel (Intact, Moitié, Détruit).
 */
public class Ship implements Persistable, Serializable {
    String name;

    Integer points;

    Statut statut = Statut.FULL;

    /**
     * Alterne le statut du vaisseau de manière cyclique : FULL -> HALF -> DEAD -> FULL.
     * @return L'ancien statut avant le changement.
     */
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


    /**
     * Constructeur complet.
     * @param name Nom du vaisseau.
     * @param points Valeur en points du vaisseau.
     * @param statut Statut initial du vaisseau.
     */
    public Ship(String name, Integer points, Statut statut) {
        this.name = name;
        this.points = points;
        this.statut = statut;
    }

    /**
     * Constructeur par défaut.
     */
    public Ship() {
    }

    /**
     * Constructeur simplifié avec statut FULL par défaut.
     * @param name Nom du vaisseau.
     * @param points Valeur en points du vaisseau.
     */
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

    /**
     * Énumération des statuts possibles d'un vaisseau.
     */
    public enum Statut implements Persistable, Serializable {
        /** Points de vie complets */
        FULL,
        /** Moitié des points de vie (Half points) */
        HALF,
        /** Vaisseau détruit */
        DEAD
    }
}
