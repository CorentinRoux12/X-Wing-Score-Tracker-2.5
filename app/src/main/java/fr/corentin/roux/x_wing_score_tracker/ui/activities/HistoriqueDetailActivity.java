package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;

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
        final int minutes = (int) this.current.getTimeLeft() / MINUTES;
        final int secondes = (int) this.current.getTimeLeft() % MINUTES / SECONDES;

        final StringBuilder time = new StringBuilder()
                .append(minutes)
                .append(":");
        if (secondes < 10) {
            time.append("0");
        }
        time.append(secondes);
        final StringBuilder finalScore = new StringBuilder();

        finalScore.append(this.current.getPlayer1().getName())
                .append(" ")
                .append(this.current.getPlayer1().getScore())
                .append(" - ")
                .append(this.current.getPlayer2().getScore())
                .append(" ")
                .append(this.current.getPlayer2().getName());

        this.finalDate.setText(this.current.getDate());
        this.finalRound.setText((String.valueOf(this.current.getRound())));
        this.finalTime.setText(time.toString());
        this.finalScore.setText(finalScore.toString());
        this.details.setText(this.current.getHistorique());
        this.mission.setText(this.current.getMission().getLibelle());
    }
}
