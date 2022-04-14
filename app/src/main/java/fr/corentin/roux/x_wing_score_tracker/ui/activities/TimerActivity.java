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

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Actions;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
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
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private boolean timerStart = false;
    private TextView timeClock;
    private Button btnStartStop;
    private long timeToSet;
    private CountDownTimer timer;
    private TextView roundNumber;
    private Button btnLessRound;
    private Button btnPlusRound;
    private TextView textViewTimeLeft;
    private Ringtone ringtoneAlarm;
    private TextView textViewMission;
    private Game game;
    private TextView textViewScorePlayerOne;
    private TextView textViewScorePlayerTwo;
    private TextView textViewScorePlayerOneKill;
    private TextView textViewScorePlayerTwoKill;
    private TextView textViewScorePlayerOneMission;
    private TextView textViewScorePlayerTwoMission;
    private Button btnLessPlayerOneKill;
    private Button btnPlusPlayerOneKill;
    private Button btnLessPlayerTwoKill;
    private Button btnPlusPlayerTwoKill;
    private Button btnPlusPlusPlayerOneKill;
    private Button btnPlusPlusPlayerTwoKill;
    private Button btnLessLessPlayerOneKill;
    private Button btnLessLessPlayerTwoKill;
    private Button btnLessPlayerOneMission;
    private Button btnPlusPlayerOneMission;
    private Button btnLessPlayerTwoMission;
    private Button btnPlusPlayerTwoMission;
    private Button btnPlusPlusPlayerOneMission;
    private Button btnPlusPlusPlayerTwoMission;
    private Button btnLessLessPlayerOneMission;
    private Button btnLessLessPlayerTwoMission;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We set the view who will be use for display the datas
        this.setContentView(R.layout.timer_layout);
        //TODO a voir si on ajout un name dans la partie Menu
        //Init Basic data
        this.game = new Game();
        //Option d affichage du timer
        this.game.setHideTimeLeft((boolean) this.getIntent().getSerializableExtra("hideTimeLeft"));
        this.game.setHideTimer((boolean) this.getIntent().getSerializableExtra("hideTimer"));
        //We set the timer at the time in minutes
        this.timeToSet = Long.parseLong(String.valueOf(this.getIntent().getSerializableExtra("timer"))) * MINUTES;
        //On recup la mission active dans le main page
        this.game.setMission((Mission) this.getIntent().getSerializableExtra("mission"));
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
        if (this.game.isHideTimer()) {
            this.timeClock.setText("Secret Time !!");
            this.textViewTimeLeft.setText("");
        }
        if (this.game.isHideTimeLeft() && !this.game.isHideTimer()) {
            this.timeClock.setText(this.generateTimeLeft((int) this.timeToSet));
            this.textViewTimeLeft.setText("Time");
        }
        //Init data
        this.initMission();
    }

    private void initMission() {
        if (this.game.getMission() != null) {
            this.textViewMission.setText(this.game.getMission().getLibelle());
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

        if (!this.game.isHideTimeLeft() && !this.game.isHideTimer()) {
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

    private void playerOneListeners() {
        this.playerOneListenersKill();

        this.playerOneListenersMission();
    }

    private void playerOneListenersKill() {
        this.btnLessPlayerOneKill.setOnClickListener(t -> {
            this.game.getPlayer1().lessScoreKill(1);
            this.updateScorePlayerOne();
        });
        this.btnLessLessPlayerOneKill.setOnClickListener(t -> {
            this.game.getPlayer1().lessScoreKill(2);
            this.updateScorePlayerOne();
        });

        this.btnPlusPlayerOneKill.setOnClickListener(t -> {
            this.game.getPlayer1().addScoreKill(1);
            this.updateScorePlayerOne();
        });
        this.btnPlusPlusPlayerOneKill.setOnClickListener(t -> {
            this.game.getPlayer1().addScoreKill(2);
            this.updateScorePlayerOne();
        });
    }

    private void playerOneListenersMission() {
        this.btnLessPlayerOneMission.setOnClickListener(t -> {
            this.game.getPlayer1().lessScoreMission(1);
            this.updateScorePlayerOne();
        });
        this.btnLessLessPlayerOneMission.setOnClickListener(t -> {
            this.game.getPlayer1().lessScoreMission(2);
            this.updateScorePlayerOne();
        });

        this.btnPlusPlayerOneMission.setOnClickListener(t -> {
            this.game.getPlayer1().addScoreMission(1);
            this.updateScorePlayerOne();
        });
        this.btnPlusPlusPlayerOneMission.setOnClickListener(t -> {
            this.game.getPlayer1().addScoreMission(2);
            this.updateScorePlayerOne();
        });
    }

    private void updateScorePlayerOne() {
        this.textViewScorePlayerOneKill.setText(String.valueOf(this.game.getPlayer1().getScoreKill()));
        this.textViewScorePlayerOneMission.setText(String.valueOf(this.game.getPlayer1().getScoreMission()));
        this.textViewScorePlayerOne.setText(String.valueOf(this.game.getPlayer1().getScore()));
    }

    private void playerTwoListeners() {
        this.playerTwoListenersKill();

        this.playerTwoListenersMission();
    }

    private void playerTwoListenersKill() {
        this.btnLessPlayerTwoKill.setOnClickListener(t -> {
            this.game.getPlayer2().lessScoreKill(1);
            this.updateScorePlayerTwo();
        });
        this.btnLessLessPlayerTwoKill.setOnClickListener(t -> {
            this.game.getPlayer2().lessScoreKill(2);
            this.updateScorePlayerTwo();
        });

        this.btnPlusPlayerTwoKill.setOnClickListener(t -> {
            this.game.getPlayer2().addScoreKill(1);
            this.updateScorePlayerTwo();
        });
        this.btnPlusPlusPlayerTwoKill.setOnClickListener(t -> {
            this.game.getPlayer2().addScoreKill(2);
            this.updateScorePlayerTwo();
        });
    }

    private void playerTwoListenersMission() {
        this.btnLessPlayerTwoMission.setOnClickListener(t -> {
            this.game.getPlayer2().lessScoreMission(1);
            this.updateScorePlayerTwo();
        });
        this.btnLessLessPlayerTwoMission.setOnClickListener(t -> {
            this.game.getPlayer2().lessScoreMission(2);
            this.updateScorePlayerTwo();
        });

        this.btnPlusPlayerTwoMission.setOnClickListener(t -> {
            this.game.getPlayer2().addScoreMission(1);
            this.updateScorePlayerTwo();
        });
        this.btnPlusPlusPlayerTwoMission.setOnClickListener(t -> {
            this.game.getPlayer2().addScoreMission(2);
            this.updateScorePlayerTwo();
        });
    }

    private void updateScorePlayerTwo() {
        this.textViewScorePlayerTwoKill.setText(String.valueOf(this.game.getPlayer2().getScoreKill()));
        this.textViewScorePlayerTwoMission.setText(String.valueOf(this.game.getPlayer2().getScoreMission()));
        this.textViewScorePlayerTwo.setText(String.valueOf(this.game.getPlayer2().getScore()));
    }

    private void roundListeners() {
        this.btnLessRound.setOnClickListener(t -> {
            if (this.game.getRound() > 0) {
                this.game.removeRound();
                this.roundNumber.setText(String.valueOf(this.game.getRound()));
                this.addAction(Actions.REMOVE_ROUND, "General", this.timeToSet, this.game.getRound());
            }
        });
        this.btnPlusRound.setOnClickListener(t -> {
            this.game.addRound();
            if (this.game.getRound() == 12) {
                Toast.makeText(TimerActivity.this, "LAST TURN !!", Toast.LENGTH_LONG).show();
            }
            this.roundNumber.setText(String.valueOf(this.game.getRound()));
            this.addAction(Actions.ADD_ROUND, "General", this.timeToSet, this.game.getRound());
            this.updateRoundDetail();
        });
    }

    private void updateRoundDetail() {
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - Player 1 Kill Point : " + this.game.getPlayer1().getScoreKill() + "\n");
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - Player 1 Mission Point : " + this.game.getPlayer1().getScoreMission() + "\n");
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - Player 2 Kill Point : " + this.game.getPlayer2().getScoreKill() + "\n");
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - Player 2 Mission Point : " + this.game.getPlayer2().getScoreMission() + "\n");
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
        this.addAction(Actions.START_TIMER, "General", this.timeToSet, this.game.getRound());
    }

    /**
     * the trigger for stop the timer
     */
    private void stopTimer() {
        this.timer.cancel();
        this.btnStartStop.setText(START);
        this.btnStartStop.setBackgroundColor(Color.parseColor(GREEN));
        this.timerStart = false;
        this.addAction(Actions.STOP_TIMER, "General", this.timeToSet, this.game.getRound());
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
        this.roundNumber = this.findViewById(R.id.roundNumber);
        this.btnLessRound = this.findViewById(R.id.btnLessRound);
        this.btnPlusRound = this.findViewById(R.id.btnPlusRound);
        this.textViewTimeLeft = this.findViewById(R.id.textViewTimeLeft);
        this.textViewMission = this.findViewById(R.id.textViewMission);
        //GLOBAL
        this.textViewScorePlayerOne = this.findViewById(R.id.scorePlayerOne);
        this.textViewScorePlayerTwo = this.findViewById(R.id.scorePlayerTwo);
        //Kill
        this.textViewScorePlayerOneKill = this.findViewById(R.id.scorePlayerOneKill);
        this.textViewScorePlayerTwoKill = this.findViewById(R.id.scorePlayerTwoKill);
        this.btnLessPlayerOneKill = this.findViewById(R.id.btnLessPlayerOneKill);
        this.btnPlusPlayerOneKill = this.findViewById(R.id.btnPlusPlayerOneKill);
        this.btnLessPlayerTwoKill = this.findViewById(R.id.btnLessPlayerTwoKill);
        this.btnPlusPlayerTwoKill = this.findViewById(R.id.btnPlusPlayerTwoKill);
        this.btnPlusPlusPlayerOneKill = this.findViewById(R.id.btnPlusPlusPlayerOneKill);
        this.btnPlusPlusPlayerTwoKill = this.findViewById(R.id.btnPlusPlusPlayerTwoKill);
        this.btnLessLessPlayerOneKill = this.findViewById(R.id.btnLessLessPlayerOneKill);
        this.btnLessLessPlayerTwoKill = this.findViewById(R.id.btnLessLessPlayerTwoKill);
        //Mission
        this.textViewScorePlayerOneMission = this.findViewById(R.id.scorePlayerOneMission);
        this.textViewScorePlayerTwoMission = this.findViewById(R.id.scorePlayerTwoMission);
        this.btnLessPlayerOneMission = this.findViewById(R.id.btnLessPlayerOneMission);
        this.btnPlusPlayerOneMission = this.findViewById(R.id.btnPlusPlayerOneMission);
        this.btnLessPlayerTwoMission = this.findViewById(R.id.btnLessPlayerTwoMission);
        this.btnPlusPlayerTwoMission = this.findViewById(R.id.btnPlusPlayerTwoMission);
        this.btnPlusPlusPlayerOneMission = this.findViewById(R.id.btnPlusPlusPlayerOneMission);
        this.btnPlusPlusPlayerTwoMission = this.findViewById(R.id.btnPlusPlusPlayerTwoMission);
        this.btnLessLessPlayerOneMission = this.findViewById(R.id.btnLessLessPlayerOneMission);
        this.btnLessLessPlayerTwoMission = this.findViewById(R.id.btnLessLessPlayerTwoMission);

    }

    private void acquireLock() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void releaseLock() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        this.addAction(Actions.END, "General", this.timeToSet, this.game.getRound());

        this.updateRoundDetail();

        this.game.setTimeLeft(this.getTimeToSet());
        this.game.setHistorique(this.historique.toString());
        this.game.setDate(this.dateFormat.format(new Date()));
        this.historiqueService.saveNewGame(this.getBaseContext(), this.game);

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
