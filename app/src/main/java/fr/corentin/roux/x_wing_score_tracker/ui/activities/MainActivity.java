package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;

/**
 * @author Corentin Roux
 * <p>
 * The Main view at the start of the application
 */
public class MainActivity extends AppCompatActivity {

    private final Random random = new Random();
    private TextInputEditText timer;
    private Button btnStart;
    private Button btnHistorique;
    private Button btnRandom;
    private CheckBox checkHideTimer;
    private boolean timerHideCheck = false;
    private String time;
    private Button btnRandomMission;
    private TextView textViewRandomMission;
    private Mission mission;

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
            this.startHistoriqueActivity();
        });
        this.btnRandom.setOnClickListener(t -> {
            this.time = this.generateRandomTime();
            this.startTimerActivity();
        });
        this.checkHideTimer.setOnClickListener(t -> {
            this.timerHideCheck = this.checkHideTimer.isChecked();
        });
        this.btnRandomMission.setOnClickListener(t -> {
            this.generateRandomMission();
        });
        this.textViewRandomMission.setOnClickListener(t -> {
            this.startMissionDetailActivity();
        });
    }

    private void startMissionDetailActivity() {
        if (this.mission != null) {
            final Intent intent = new Intent(this, MissionDetailActivity.class);
            intent.putExtra("mission", this.mission.getCode());
            this.startActivity(intent);
        }
    }

    private void generateRandomMission() {
        this.mission = Mission.parseCode(this.random.nextInt(4) + 1);
        this.textViewRandomMission.setText(this.mission.getLibelle(), TextView.BufferType.SPANNABLE);
        Toast.makeText(this, "Touch the mission for details", Toast.LENGTH_SHORT).show();
    }

    private String generateRandomTime() {
        final long attackDice = this.random.nextInt(8);
        //Defense
        //0&1&2 = Blank
        //3&4 = Eyes
        //5-7 = Evades
        final long defenseDice1 = this.random.nextInt(8);
        final long defenseDice2 = this.random.nextInt(8);
        final long defenseDice3 = this.random.nextInt(8);
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

    private void startHistoriqueActivity() {
        final Intent intent = new Intent(this, HistoriqueActivity.class);
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
        this.btnRandomMission = this.findViewById(R.id.btnRandomMission);
        this.textViewRandomMission = this.findViewById(R.id.textViewRandomMission);
    }

}