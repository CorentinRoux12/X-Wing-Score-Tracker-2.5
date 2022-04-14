package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Actions;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;
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
    private boolean hideTimeLeft;
    private boolean hideTimer;
    private TextView textViewTimeLeft;
    private Ringtone ringtoneAlarm;
    private Mission mission;
    private TextView textViewMission;
    private Button btnPlusPlusPlayerOne;
    //    private Button btnPlusPlusPlusPlayerOne;
    private Button btnPlusPlusPlayerTwo;
    //    private Button btnPlusPlusPlusPlayerTwo;
    private Button btnLessLessPlayerOne;
    private Button btnLessLessPlayerTwo;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We set the view who will be use for display the datas
        this.setContentView(R.layout.timer_layout);
        //Option d affichage du timer
        this.hideTimeLeft = (boolean) this.getIntent().getSerializableExtra("hideTimeLeft");
        this.hideTimer = (boolean) this.getIntent().getSerializableExtra("hideTimer");
        //We set the timer at the time in minutes
        this.timeToSet = Long.parseLong(String.valueOf(this.getIntent().getSerializableExtra("timer"))) * MINUTES;
        //On recup la mission active dans le main page
        this.mission = (Mission) this.getIntent().getSerializableExtra("mission");
        //Bind the xml and the fields
        this.findView();
        //Initialization of the listeners
        this.initListeners();
        //Init des datas de la page
        this.initDatas();
    }

    @SuppressLint("SetTextI18n")
    private void initDatas() {
        //Init Ring
        this.initRing();
        //Update of the view for the first time => set the fields
        this.updateTimer();
        if (this.hideTimer) {
            this.timeClock.setText("Secret Time !!");
            this.textViewTimeLeft.setText("");
        }
        if (this.hideTimeLeft && !this.hideTimer) {
            this.timeClock.setText(this.generateTimeLeft((int) this.timeToSet));
            this.textViewTimeLeft.setText("Time");
        }
        //Init data
        this.initMission();
    }

    private void initMission() {
        if (this.mission != null) {
            this.textViewMission.setText(this.mission.getLibelle());
        }
    }

    private void initRing() {
        final Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        this.ringtoneAlarm = RingtoneManager.getRingtone(this.getApplicationContext(), alarmTone);
        this.ringtoneAlarm.setStreamType(AudioManager.STREAM_ALARM);
    }

    /**
     * Update the timer with the time left
     */
    private void updateTimer() {
        final StringBuilder timeLeft = this.generateTimeLeft((int) this.timeToSet);

        if (!this.hideTimeLeft && !this.hideTimer) {
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
        //Init des listeners lié au Player 1
        this.playerOneListeners();
        //Init des listeners lié au Player 2
        this.playerTwoListeners();
        //Init les listeners pour la gestion des rounds
        this.roundListeners();
    }

    private void playerTwoListeners() {
        this.btnLessPlayerTwo.setOnClickListener(t -> {
            this.removeOnePlayerTwo();
            this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
        });
        this.btnLessLessPlayerTwo.setOnClickListener(t -> {
            this.removeOnePlayerTwo();
            this.removeOnePlayerTwo();
            this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
        });

        this.btnPlusPlayerTwo.setOnClickListener(t -> {
            this.addOnePlayerTwo();
            this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
        });
        this.btnPlusPlusPlayerTwo.setOnClickListener(t -> {
            this.addOnePlayerTwo();
            this.addOnePlayerTwo();
            this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
        });
//        this.btnPlusPlusPlusPlayerTwo.setOnClickListener(t -> {
//            this.addOnePlayerTwo();
//            this.addOnePlayerTwo();
//            this.addOnePlayerTwo();
//            this.textViewScorePlayerTwo.setText(String.valueOf(this.scorePlayerTwo));
//        });
    }

    private void playerOneListeners() {
        this.btnLessPlayerOne.setOnClickListener(t -> {
            this.removeOnePlayerOne();
            this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
        });
        this.btnLessLessPlayerOne.setOnClickListener(t -> {
            this.removeOnePlayerOne();
            this.removeOnePlayerOne();
            this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
        });

        this.btnPlusPlayerOne.setOnClickListener(t -> {
            this.addOnePlayerOne();
            this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
        });
        this.btnPlusPlusPlayerOne.setOnClickListener(t -> {
            this.addOnePlayerOne();
            this.addOnePlayerOne();
            this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
        });
//        this.btnPlusPlusPlusPlayerOne.setOnClickListener(t -> {
//            this.addOnePlayerOne();
//            this.addOnePlayerOne();
//            this.addOnePlayerOne();
//            this.textViewScorePlayerOne.setText(String.valueOf(this.scorePlayerOne));
//        });
    }

    private void roundListeners() {
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

    private void addOnePlayerTwo() {
        this.scorePlayerTwo++;
        this.addAction(Actions.ADD_POINT, "Player_2", this.timeToSet, this.round);
    }

    private void addOnePlayerOne() {
        this.scorePlayerOne++;
        this.addAction(Actions.ADD_POINT, "Player_1", this.timeToSet, this.round);
    }

    private void removeOnePlayerTwo() {
        if (this.scorePlayerTwo > 0) {
            this.scorePlayerTwo--;
            this.addAction(Actions.REMOVE_POINT, "Player_2", this.timeToSet, this.round);
        }
    }

    private void removeOnePlayerOne() {
        if (this.scorePlayerOne > 0) {
            this.scorePlayerOne--;
            this.addAction(Actions.REMOVE_POINT, "Player_1", this.timeToSet, this.round);
        }
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
                Toast.makeText(TimerActivity.this, "TIME OVER !!", Toast.LENGTH_LONG).show();
                TimerActivity.this.playSound();
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
        if (this.ringtoneAlarm.isPlaying()) {
            this.ringtoneAlarm.stop();
        }
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
        this.textViewMission = this.findViewById(R.id.textViewMission);
        this.btnPlusPlusPlayerOne = this.findViewById(R.id.btnPlusPlusPlayerOne);
//        this.btnPlusPlusPlusPlayerOne = this.findViewById(R.id.btnPlusPlusPlusPlayerOne);
        this.btnPlusPlusPlayerTwo = this.findViewById(R.id.btnPlusPlusPlayerTwo);
//        this.btnPlusPlusPlusPlayerTwo = this.findViewById(R.id.btnPlusPlusPlusPlayerTwo);
        this.btnLessLessPlayerOne = this.findViewById(R.id.btnLessLessPlayerOne);
        this.btnLessLessPlayerTwo = this.findViewById(R.id.btnLessLessPlayerTwo);
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
        if (this.ringtoneAlarm.isPlaying()) {
            this.ringtoneAlarm.stop();
        }
        super.onDestroy();
    }

    private void addAction(final Actions actions, final String player, final long timeLeft, final int round) {
        final StringBuilder time = this.generateTimeLeft((int) timeLeft);
        this.historique.insert(0, time.toString() + "-" + "Round " + round + "-" + player + "-" + actions.getLibelle() + "\n");
    }

    private void playSound() {
        this.ringtoneAlarm.play();
    }

}
