package fr.corentin.roux.x_wing_score_tracker;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Corentin Roux
 * <p>
 * Activity for the view of the scoring board
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class TimerActivity extends AppCompatActivity {

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private boolean timerStart = false;
    private TextView timeClock;
    private Button btnStartStop;
    private Button btnLessPlayerOne;
    private TextView textViewScorePlayerOne;
    private Button btnPlusPlayerOne;
    private Button btnLessPlayerTwo;
    private TextView textViewScorePlayerTwo;
    private Button btnPlusPlayerTwo;
    private long timeToSet;
    private CountDownTimer timer;
    private int scorePlayerOne = 0;
    private int scorePlayerTwo = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We set the view who will be use for display the datas
        this.setContentView(R.layout.timer_layout);
        //We set the timer at the time in minutes
        this.timeToSet = Long.parseLong(String.valueOf(this.getIntent().getSerializableExtra("timer"))) * MINUTES;
        //Bind the xml and the fields
        this.findView();
        //Initialization of the listeners
        this.initListeners();
        //Update of the view for the first time => set the fields
        this.updateTimer();
    }

    /**
     * Update the timer with the time left
     */
    private void updateTimer() {
        final int minutes = (int) this.timeToSet / MINUTES;
        final int secondes = (int) this.timeToSet % MINUTES / SECONDES;

        final StringBuilder timeLeft = new StringBuilder()
                .append(minutes)
                .append(":");
        if (secondes < 10) {
            timeLeft.append("0");
        }
        timeLeft.append(secondes);

        this.timeClock.setText(timeLeft.toString());
    }

    /**
     * Initialization of the listeners for each interaction in the view
     */
    private void initListeners() {
        this.btnStartStop.setOnClickListener(t -> {
            if (this.timerStart) {
                this.stopTimer();
                this.releaseLock();
            } else {
                this.startTimer();
                this.acquireLock();
            }
        });

        this.btnLessPlayerOne.setOnClickListener(t -> {
            if (this.scorePlayerOne > 0) {
                this.scorePlayerOne--;
                this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
            }
        });
        this.btnLessPlayerTwo.setOnClickListener(t -> {
            if (this.scorePlayerTwo > 0) {
                this.scorePlayerTwo--;
                this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
            }
        });
        this.btnPlusPlayerOne.setOnClickListener(t -> {
            this.scorePlayerOne++;
            this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
        });
        this.btnPlusPlayerTwo.setOnClickListener(t -> {
            this.scorePlayerTwo++;
            this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
        });
    }

    /**
     * the trigger for start the timer
     */
    private void startTimer() {
        this.timer = new CountDownTimer(this.timeToSet, SECONDES) {
            /**
             * trigger every {@fields SECONDES} millisecondes
             * {@inheritDoc}
             */
            @Override
            public void onTick(final long l) {
                TimerActivity.this.timeToSet = l;
                TimerActivity.this.updateTimer();
            }

            /**
             * at the end of the time
             * {@inheritDoc}
             */
            @Override
            public void onFinish() {
                //TODO Alarm Minuterie / autre a faire
                Toast.makeText(TimerActivity.this, "TIME OVER !!", Toast.LENGTH_LONG).show();
            }
        }.start();
        this.btnStartStop.setText("STOP");
        this.btnStartStop.setBackgroundColor(Color.RED);
        this.timerStart = true;
    }

    /**
     * the trigger for stop the timer
     */
    private void stopTimer() {
        this.timer.cancel();
        this.btnStartStop.setText("START");
        this.btnStartStop.setBackgroundColor(Color.GREEN);
        this.timerStart = false;
    }

    /**
     * Binding of all the fields XML and the fields JAVA
     */
    private void findView() {
        this.timeClock = this.findViewById(R.id.timeClock);
        this.btnStartStop = this.findViewById(R.id.btnStart);
        this.btnLessPlayerOne = this.findViewById(R.id.btnLessPlayerOne);
        this.textViewScorePlayerOne = this.findViewById(R.id.scorePlayerOne);
        this.btnPlusPlayerOne = this.findViewById(R.id.btnPlusPlayerOne);
        this.btnLessPlayerTwo = this.findViewById(R.id.btnLessPlayerTwo);
        this.textViewScorePlayerTwo = this.findViewById(R.id.scorePlayerTwo);
        this.btnPlusPlayerTwo = this.findViewById(R.id.btnPlusPlayerTwo);
    }

    private void acquireLock() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void releaseLock() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
