package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game implements Persistable, Serializable {

    private Player player1 = new Player();
    private Player player2 = new Player();
    private long timeLeft;
    private int round = 0;
    private String historique;
    private String date;
    private Mission mission = Mission.NO_MISSION;
    private boolean hideTimeLeft;
    private boolean hideTimer;

    public void removeRound() {
        this.round--;
    }

    public void addRound() {
        this.round++;
    }


}
