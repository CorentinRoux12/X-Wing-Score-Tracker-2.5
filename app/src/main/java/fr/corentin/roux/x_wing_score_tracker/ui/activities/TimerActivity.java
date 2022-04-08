package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Actions;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import lombok.Getter;

/**
 * @author Corentin Roux
 * <p>
 * Activity for the view of the scoring board
 */
@Getter
public class TimerActivity extends AppCompatActivity {

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private static final String RED = "#9d0208";
    private static final String GREEN = "#2b9348";
    private static final String START = "START";
    private static final String STOP = "STOP";
    private final StringBuilder historique = new StringBuilder();
    private final HistoriqueService historiqueService = HistoriqueService.getInstance();
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
    private int round = 0;
    private TextView roundNumber;
    private Button btnLessRound;
    private Button btnPlusRound;
    private boolean hideTimer;
    private TextView textViewTimeLeft;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We set the view who will be use for display the datas
        this.setContentView(R.layout.timer_layout);
        //Option d affichage du timer
        this.hideTimer = (boolean) this.getIntent().getSerializableExtra("hideTimer");
        //We set the timer at the time in minutes
        this.timeToSet = Long.parseLong(String.valueOf(this.getIntent().getSerializableExtra("timer"))) * MINUTES;
        //Bind the xml and the fields
        this.findView();
        //Initialization of the listeners
        this.initListeners();
        //Update of the view for the first time => set the fields
        this.updateTimer();
        if (this.hideTimer) {
            this.timeClock.setText(this.generateTimeLeft((int) this.timeToSet));
            this.textViewTimeLeft.setText("Time");
        }
    }

    /**
     * Update the timer with the time left
     */
    private void updateTimer() {
        final StringBuilder timeLeft = this.generateTimeLeft((int) this.timeToSet);

        if (!this.hideTimer) {
            this.timeClock.setText(timeLeft.toString());
        }
    }

    private StringBuilder generateTimeLeft(final int timeToSet) {
        final int minutes = timeToSet / MINUTES;
        final int secondes = timeToSet % MINUTES / SECONDES;

        final StringBuilder timeLeft = new StringBuilder()
                .append(minutes)
                .append(":");
        if (secondes < 10) {
            timeLeft.append("0");
        }
        timeLeft.append(secondes);
        return timeLeft;
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
                this.addAction(Actions.REMOVE_POINT, "Player_1", this.timeToSet, this.round);
                //ROUND TIME ACTION PLAYER
            }
        });
        this.btnLessPlayerTwo.setOnClickListener(t -> {
            if (this.scorePlayerTwo > 0) {
                this.scorePlayerTwo--;
                this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
                this.addAction(Actions.REMOVE_POINT, "Player_2", this.timeToSet, this.round);
            }
        });
        this.btnPlusPlayerOne.setOnClickListener(t -> {
            this.scorePlayerOne++;
            this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
            this.addAction(Actions.ADD_POINT, "Player_1", this.timeToSet, this.round);
        });
        this.btnPlusPlayerTwo.setOnClickListener(t -> {
            this.scorePlayerTwo++;
            this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
            this.addAction(Actions.ADD_POINT, "Player_2", this.timeToSet, this.round);
        });
        this.btnLessRound.setOnClickListener(t -> {
            if (this.round > 0) {
                this.round--;
                this.roundNumber.setText(String.valueOf(this.round));
                this.addAction(Actions.REMOVE_ROUND, "General", this.timeToSet, this.round);
            }
        });
        this.btnPlusRound.setOnClickListener(t -> {
            this.round++;
            if (this.round == 12) {
                Toast.makeText(TimerActivity.this, "LAST TURN !!", Toast.LENGTH_LONG).show();
            }
            this.roundNumber.setText(String.valueOf(this.round));
            this.addAction(Actions.ADD_ROUND, "General", this.timeToSet, this.round);
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
        this.btnStartStop.setText(STOP);
        this.btnStartStop.setBackgroundColor(Color.parseColor(RED));
        this.timerStart = true;
        this.addAction(Actions.START_TIMER, "General", this.timeToSet, this.round);
    }

    /**
     * the trigger for stop the timer
     */
    private void stopTimer() {
        this.timer.cancel();
        this.btnStartStop.setText(START);
        this.btnStartStop.setBackgroundColor(Color.parseColor(GREEN));
        this.timerStart = false;
        this.addAction(Actions.STOP_TIMER, "General", this.timeToSet, this.round);
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
        this.roundNumber = this.findViewById(R.id.roundNumber);
        this.btnLessRound = this.findViewById(R.id.btnLessRound);
        this.btnPlusRound = this.findViewById(R.id.btnPlusRound);
        this.textViewTimeLeft = this.findViewById(R.id.textViewTimeLeft);
    }

    private void acquireLock() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void releaseLock() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        this.historiqueService.saveNewGame(this);
        super.onDestroy();
    }

    public void addAction(final Actions actions, final String player, final long timeLeft, final int round) {
        final StringBuilder time = this.generateTimeLeft((int) timeLeft);
        this.historique.insert(0, time.toString() + "-" + "Round " + round + "-" + player + "-" + actions.getLibelle() + "\n");
    }
}
