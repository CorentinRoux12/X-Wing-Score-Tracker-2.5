package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game implements Persistable, Serializable {

    private int scorePlayer1;
    private int scorePlayer2;
    private long timeLeft;
    private int round;
    private String historique;
    private String date;
    private String mission;
}
