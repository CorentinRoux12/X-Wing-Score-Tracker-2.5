package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Games implements Persistable, Serializable {

    private List<Game> games = new ArrayList<>();

    public Games() {
    }

    public Games(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
