package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Round;

public class HistoriqueDetailActivity extends AppCompatActivity {

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private Game current;
    private TextView finalDate;
    private TextView finalRound;
    private TextView finalTime;
    private TextView finalScore;
    private TextView details;
    private TextView mission;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We set the view who will be use for display the datas
        this.setContentView(R.layout.historique_detail_layout);
        //We set the timer at the time in minutes
        this.current = (Game) this.getIntent().getSerializableExtra("game");
        //Bind the xml and the fields
        this.findView();
        //Initialization des datas
        this.initialisationData();
    }

    private void findView() {
        this.finalDate = this.findViewById(R.id.finalDate);
        this.finalRound = this.findViewById(R.id.finalRound);
        this.finalTime = this.findViewById(R.id.finalTime);
        this.finalScore = this.findViewById(R.id.finalScore);
        this.details = this.findViewById(R.id.details);
        this.mission = this.findViewById(R.id.mission);
    }

    private void initialisationData() {
        final StringBuilder time = generateTimerAffichage(current.getTimeLeft());
        final StringBuilder finalScore = new StringBuilder();

        finalScore.append(this.current.getPlayer1().getName())
                .append(" ")
                .append(this.current.getPlayer1().getScore().getScoreGlobal())
                .append(" - ")
                .append(this.current.getPlayer2().getScore().getScoreGlobal())
                .append(" ")
                .append(this.current.getPlayer2().getName());

        final StringBuilder historique = new StringBuilder();

        int scoreTempJ1 = 0;
        int scoreTempJ2 = 0;
        for (final Round round : current.getRounds()) {
            scoreTempJ1 += round.getScorePlayer1().getScoreGlobal();
            scoreTempJ2 += round.getScorePlayer2().getScoreGlobal();

            historique
                    .append("----------------------------------------------------\n")
                    .append("ROUND : ").append(round.getRoundNumber()).append("\n")
                    .append("Duration of the round : ").append(generateTimerAffichage(round.getTime())).append("\n")
                    .append("First Player : ").append(round.getFirstPlayer()).append("\n")
                    .append("Score Global : \n")
                    .append("  - ").append(current.getPlayer1().getName().toUpperCase()).append(" : ").append(scoreTempJ1).append("\n")
                    .append("  - ").append(current.getPlayer2().getName().toUpperCase()).append(" : ").append(scoreTempJ2).append("\n")
                    .append("Scores of the round : \n")
                    .append("  ").append(current.getPlayer1().getName().toUpperCase()).append("\n")
                    .append("    - Global ").append(round.getScorePlayer1().getScoreGlobal()).append("\n")
                    .append("      - Kill ").append(round.getScorePlayer1().getScoreKill()).append("\n")
                    .append("      - Mission ").append(round.getScorePlayer1().getScoreMission()).append("\n")
                    .append("  ").append(current.getPlayer2().getName().toUpperCase()).append("\n")
                    .append("    - Global ").append(round.getScorePlayer2().getScoreGlobal()).append("\n")
                    .append("      - Kill ").append(round.getScorePlayer2().getScoreKill()).append("\n")
                    .append("      - Mission ").append(round.getScorePlayer2().getScoreMission()).append("\n");
        }

        this.finalDate.setText(this.current.getDate());
        this.finalRound.setText((String.valueOf(this.current.getRound())));
        this.finalTime.setText(time.toString());
        this.finalScore.setText(finalScore.toString());
        this.details.setText(historique.toString());
        this.mission.setText(this.current.getMission().getLibelle());
    }

    private StringBuilder generateTimerAffichage(long timeLeft) {
        final int minutes = generateMinutes(timeLeft);
        final int secondes = generateSecondes(timeLeft);

        final StringBuilder time = new StringBuilder()
                .append(minutes)
                .append(":");
        if (secondes < 10) {
            time.append("0");
        }
        time.append(secondes);
        return time;
    }

    private int generateMinutes(final long time) {
        return (int) time / MINUTES;
    }

    private int generateSecondes(final long time) {
        return (int) time % MINUTES / SECONDES;
    }
}
