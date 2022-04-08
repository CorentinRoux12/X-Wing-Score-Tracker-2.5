package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;

/**
 * @author Corentin Roux
 * <p>
 * The Main view at the start of the application
 */
public class MainActivity extends AppCompatActivity {

    private TextInputEditText timer;
    private Button btnStart;
    private Button btnHistorique;
    private Button btnRandom;
    private CheckBox checkHideTimer;
    private boolean timerHideCheck = false;
    private String time;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.main_layout);

        this.findView();

        this.initListeners();
    }

    /**
     * Initialization of the listeners for each interaction in the view
     */
    private void initListeners() {
        this.btnStart.setOnClickListener(t -> {
            if (this.timer != null && this.timer.getText() != null && !this.timer.getText().toString().equals("")) {
                this.time = this.timer.getText().toString();
                this.startTimerActivity();
            } else {
                Toast.makeText(this, "Timer is not correctly set !!", Toast.LENGTH_LONG).show();
            }
        });

        this.btnHistorique.setOnClickListener(t -> {
            final Intent intent = new Intent(this, HistoriqueActivity.class);
            this.startActivity(intent);
        });
        this.btnRandom.setOnClickListener(t -> {
            this.time = this.generateRandomTime();
            this.startTimerActivity();
        });
        this.checkHideTimer.setOnClickListener(t -> {
            this.timerHideCheck = this.checkHideTimer.isChecked();
        });
    }

    private String generateRandomTime() {
        final long attackDice = ((Double) (Math.random() * 100 % 8)).longValue();
        //Defense
        //0&1&2 = Blank
        //3&4 = Eyes
        //5-7 = Evades
        final long defenseDice1 = ((Double) (Math.random() * 100 % 8)).longValue();
        final long defenseDice2 = ((Double) (Math.random() * 100 % 8)).longValue();
        final long defenseDice3 = ((Double) (Math.random() * 100 % 8)).longValue();
        final List<Long> defenseDice = Arrays.asList(defenseDice1, defenseDice2, defenseDice3);
        int basicTime = 75;
        if (0L == attackDice || 1L == attackDice) {//0&1 = Blank
            for (final Long l : defenseDice) {
                if (l >= 3L) {
                    basicTime--;
                }
            }
            return String.valueOf(basicTime);
        } else if (2L == attackDice || 3L == attackDice) {//2&3 = Eyes
            return "75";
        } else { //4-7 = Hit&Crit
            for (final Long l : defenseDice) {
                if (l >= 3L) {
                    basicTime++;
                }
            }
            return String.valueOf(basicTime);
        }
    }

    private void startTimerActivity() {
        final Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("hideTimer", this.timerHideCheck);
        intent.putExtra("timer", this.time);
        this.startActivity(intent);
    }

    /**
     * Binding of all the fields XML and the fields JAVA
     */
    private void findView() {
        this.timer = this.findViewById(R.id.inputTimer);
        this.btnStart = this.findViewById(R.id.btnStart);
        this.btnHistorique = this.findViewById(R.id.btnHistorique);
        this.btnRandom = this.findViewById(R.id.btnRandom);
        this.checkHideTimer = this.findViewById(R.id.checkHideTimer);
    }

}