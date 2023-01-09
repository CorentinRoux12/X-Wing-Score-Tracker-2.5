package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.ui.dialog.EndDialogTimer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Corentin Roux
 * <p>
 * Activity for the view of the scoring board
 */
@Getter
@SuppressLint("SetTextI18n")
public class TimerActivity extends AppCompatActivity {

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private static final String RED = "#9d0208";
    private static final String GREEN = "#2b9348";
    private final StringBuilder historique = new StringBuilder();
    private final HistoriqueService historiqueService = HistoriqueService.getInstance();
    private final SettingService service = SettingService.getInstance();
    private Setting setting;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Setter
    private boolean end = false;
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
    private TextView playerOne;
    private TextView playerTwo;
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
    private Button btnLessPlayerOneMission;
    private Button btnPlusPlayerOneMission;
    private Button btnLessPlayerTwoMission;
    private Button btnPlusPlayerTwoMission;
    private Button firstPlayer1;
    private Button firstPlayer2;
    private TextView firstPlayerName;
    private int firstPlayerChoice = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We set the view who will be use for display the datas
        this.setContentView(R.layout.timer_layout);
        //Init Basic data
        this.initGame();
        //Bind the xml and the fields
        this.findView();
        //Init des datas de la page
        this.initDatas();
        //Initialization of the listeners
        this.initListeners();
    }

    private void initGame() {
        this.game = new Game();
        this.setting = this.service.getSetting(this);
        //Option d affichage du timer
        this.game.setHideTimeLeft((boolean) this.getIntent().getSerializableExtra("hideTimeLeft"));
        this.game.setHideTimer((boolean) this.getIntent().getSerializableExtra("hideTimer"));
        if (setting.getName() != null && !"".equals(setting.getName().trim())) {
            this.game.getPlayer1().setName(setting.getName());
        }
        if (setting.getOpponent() != null && !"".equals(setting.getOpponent().trim())) {
            this.game.getPlayer2().setName(setting.getOpponent());
        }
        //We set the timer at the time in minutes
        this.timeToSet = Long.parseLong(String.valueOf(this.getIntent().getSerializableExtra("timer"))) * MINUTES;
        //On recup la mission active dans le main page
        this.game.setMission((Mission) this.getIntent().getSerializableExtra("mission"));
    }

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
        final String nameP1 = this.game.getPlayer1().getName();
        if (!"".equals(nameP1)) {
            this.playerOne.setText(nameP1);
        }
        final String nameP2 = this.game.getPlayer2().getName();
        if (!"".equals(nameP2)) {
            this.playerTwo.setText(nameP2);
        }
        this.firstPlayer1.setText(game.getPlayer1().getName());
        this.firstPlayer2.setText(game.getPlayer2().getName());
        //Init data
        this.initMission();
    }

    private void initMission() {
        if (this.game.getMission() != null && !Mission.NO_MISSION.equals(this.game.getMission())) {
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

        this.checkBoxListeners();
        //Init les listeners pour la gestion des rounds
        this.roundListeners();

        this.textViewMission.setOnClickListener(t -> {
            if (Mission.NO_MISSION.equals(this.game.getMission())) {
                Toast.makeText(this, "No Mission for this game !!", Toast.LENGTH_SHORT).show();
            } else {
                final Intent intent = new Intent(this, MissionDetailActivity.class);
                intent.putExtra("mission", this.game.getMission().getCode());
                this.startActivity(intent);
            }
        });
    }


    private void checkBoxListeners() {
        this.firstPlayer1.setOnClickListener(t -> {
            if (this.firstPlayerChoice != 1) {
                this.firstPlayerChoice = 1;
                this.firstPlayerName.setText(game.getPlayer1().getName());
            }
        });
        this.firstPlayer2.setOnClickListener(t -> {
            if (this.firstPlayerChoice != 2) {
                this.firstPlayerChoice = 2;
                this.firstPlayerName.setText(game.getPlayer2().getName());
            }
        });
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

        this.btnPlusPlayerOneKill.setOnClickListener(t -> {
            this.game.getPlayer1().addScoreKill(1);
            this.updateScorePlayerOne();
        });
    }

    private void playerOneListenersMission() {
        this.btnLessPlayerOneMission.setOnClickListener(t -> {
            this.game.getPlayer1().lessScoreMission(1);
            this.updateScorePlayerOne();
        });

        this.btnPlusPlayerOneMission.setOnClickListener(t -> {
            this.game.getPlayer1().addScoreMission(1);
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

        this.btnPlusPlayerTwoKill.setOnClickListener(t -> {
            this.game.getPlayer2().addScoreKill(1);
            this.updateScorePlayerTwo();
        });
    }

    private void playerTwoListenersMission() {
        this.btnLessPlayerTwoMission.setOnClickListener(t -> {
            this.game.getPlayer2().lessScoreMission(1);
            this.updateScorePlayerTwo();
        });

        this.btnPlusPlayerTwoMission.setOnClickListener(t -> {
            this.game.getPlayer2().addScoreMission(1);
            this.updateScorePlayerTwo();
        });
    }

    private void updateScorePlayerTwo() {
        this.textViewScorePlayerTwoKill.setText(String.valueOf(this.game.getPlayer2().getScoreKill()));
        this.textViewScorePlayerTwoMission.setText(String.valueOf(this.game.getPlayer2().getScoreMission()));
        this.textViewScorePlayerTwo.setText(String.valueOf(this.game.getPlayer2().getScore()));
    }

    private void roundListeners() {
        this.btnLessRound.setOnClickListener(t -> removeRound());
        this.btnPlusRound.setOnClickListener(t -> this.addRound());
    }

    private void removeRound() {
        if (this.game.getRound() > 0) {
            this.game.removeRound();
            this.roundNumber.setText(String.valueOf(this.game.getRound()));
            this.addAction(Actions.REMOVE_ROUND, "General", this.timeToSet, this.game.getRound());
        }
    }

    private void addRound() {
        this.game.addRound();
        if (this.game.getRound() == 12) {
            Toast.makeText(TimerActivity.this, "LAST TURN !!", Toast.LENGTH_LONG).show();
        }
        this.roundNumber.setText(String.valueOf(this.game.getRound()));
        this.addAction(Actions.ADD_ROUND, "General", this.timeToSet, this.game.getRound());
        this.updateRoundDetail();
    }

    private void updateRoundDetail() {
        this.historique.insert(0, Actions.FIRST_PLAYER.getLibelle() + " - " + this.firstPlayerName.getText() + "\n");
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - " + this.game.getPlayer1().getName() + " Kill Point : " + this.game.getPlayer1().getScoreKill() + "\n");
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - " + this.game.getPlayer1().getName() + " Mission Point : " + this.game.getPlayer1().getScoreMission() + "\n");
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - " + this.game.getPlayer2().getName() + " Kill Point : " + this.game.getPlayer2().getScoreKill() + "\n");
        this.historique.insert(0, Actions.DETAIL_ROUND.getLibelle() + " - " + this.game.getPlayer2().getName() + " Mission Point : " + this.game.getPlayer2().getScoreMission() + "\n");
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
                if (TimerActivity.this.timeToSet == 0) {
                    TimerActivity.this.playSound();
                }
            }
        }.start();
        this.btnStartStop.setText(this.getString(R.string.stop));
        this.btnStartStop.setBackgroundColor(Color.parseColor(RED));
        this.timerStart = true;
        this.addAction(Actions.START_TIMER, "General", this.timeToSet, this.game.getRound());
        if (this.game.getRound() == 0) {
            this.historique.insert(0, Actions.FIRST_PLAYER.getLibelle() + " - " + this.firstPlayerName.getText() + "\n");
            this.addRound();
        }
    }

    /**
     * the trigger for stop the timer
     */
    private void stopTimer() {
        if (timer != null) {
            this.timer.cancel();
        }
        this.btnStartStop.setText(this.getString(R.string.start));
        this.btnStartStop.setBackgroundColor(Color.parseColor(GREEN));
        if (timerStart){
            this.timerStart = false;
            this.addAction(Actions.STOP_TIMER, "General", this.timeToSet, this.game.getRound());
            if (this.ringtoneAlarm.isPlaying()) {
                this.ringtoneAlarm.stop();
            }
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
        this.playerOne = this.findViewById(R.id.playerOne);
        this.playerTwo = this.findViewById(R.id.playerTwo);
        this.textViewScorePlayerOne = this.findViewById(R.id.scorePlayerOne);
        this.textViewScorePlayerTwo = this.findViewById(R.id.scorePlayerTwo);
        //Kill
        this.textViewScorePlayerOneKill = this.findViewById(R.id.scorePlayerOneKill);
        this.textViewScorePlayerTwoKill = this.findViewById(R.id.scorePlayerTwoKill);
        this.btnLessPlayerOneKill = this.findViewById(R.id.btnLessPlayerOneKill);
        this.btnPlusPlayerOneKill = this.findViewById(R.id.btnPlusPlayerOneKill);
        this.btnLessPlayerTwoKill = this.findViewById(R.id.btnLessPlayerTwoKill);
        this.btnPlusPlayerTwoKill = this.findViewById(R.id.btnPlusPlayerTwoKill);
        //Mission
        this.textViewScorePlayerOneMission = this.findViewById(R.id.scorePlayerOneMission);
        this.textViewScorePlayerTwoMission = this.findViewById(R.id.scorePlayerTwoMission);
        this.btnLessPlayerOneMission = this.findViewById(R.id.btnLessPlayerOneMission);
        this.btnPlusPlayerOneMission = this.findViewById(R.id.btnPlusPlayerOneMission);
        this.btnLessPlayerTwoMission = this.findViewById(R.id.btnLessPlayerTwoMission);
        this.btnPlusPlayerTwoMission = this.findViewById(R.id.btnPlusPlayerTwoMission);

        this.firstPlayer1 = this.findViewById(R.id.firstPlayer1);
        this.firstPlayer2 = this.findViewById(R.id.firstPlayer2);
        this.firstPlayerName = this.findViewById(R.id.firstPlayerName);
    }

    private void acquireLock() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void releaseLock() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        if (!this.end) {
            final EndDialogTimer endDialogTimer = new EndDialogTimer(this);
            endDialogTimer.show(this.getSupportFragmentManager(), "dialogHelp");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        this.addAction(Actions.END, "General", this.timeToSet, this.game.getRound());

        this.updateRoundDetail();

        this.game.setTimeLeft(this.timeToSet);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("roundNumber", this.roundNumber.getText().toString());
        outState.putString("textViewScorePlayerOne", this.textViewScorePlayerOne.getText().toString());
        outState.putString("textViewScorePlayerOneKill", this.textViewScorePlayerOneKill.getText().toString());
        outState.putString("textViewScorePlayerOneMission", this.textViewScorePlayerOneMission.getText().toString());
        outState.putString("textViewScorePlayerTwo", this.textViewScorePlayerTwo.getText().toString());
        outState.putString("textViewScorePlayerTwoKill", this.textViewScorePlayerTwoKill.getText().toString());
        outState.putString("textViewScorePlayerTwoMission", this.textViewScorePlayerTwoMission.getText().toString());
        outState.putString("firstPlayerName", this.firstPlayerName.getText().toString());
        outState.putString("timeClock", this.timeClock.getText().toString());
        outState.putString("textViewTimeLeft", this.textViewTimeLeft.getText().toString());
        outState.putString("playerOne", this.playerOne.getText().toString());
        outState.putString("playerTwo", this.playerTwo.getText().toString());
        outState.putString("firstPlayer1", this.firstPlayer1.getText().toString());
        outState.putString("firstPlayer2", this.firstPlayer2.getText().toString());

        outState.putSerializable("game", this.game);
        outState.putBoolean("timerStart", this.timerStart);
        outState.putLong("timeToSet", this.timeToSet);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.roundNumber.setText(savedInstanceState.getString("roundNumber"));
        this.textViewScorePlayerOne.setText(savedInstanceState.getString("textViewScorePlayerOne"));
        this.textViewScorePlayerOneKill.setText(savedInstanceState.getString("textViewScorePlayerOneKill"));
        this.textViewScorePlayerOneMission.setText(savedInstanceState.getString("textViewScorePlayerOneMission"));
        this.textViewScorePlayerTwo.setText(savedInstanceState.getString("textViewScorePlayerTwo"));
        this.textViewScorePlayerTwoKill.setText(savedInstanceState.getString("textViewScorePlayerTwoKill"));
        this.textViewScorePlayerTwoMission.setText(savedInstanceState.getString("textViewScorePlayerTwoMission"));
        this.firstPlayerName.setText(savedInstanceState.getString("firstPlayerName"));
        this.timeClock.setText(savedInstanceState.getString("timeClock"));
        this.textViewTimeLeft.setText(savedInstanceState.getString("textViewTimeLeft"));
        this.playerOne.setText(savedInstanceState.getString("playerOne"));
        this.playerTwo.setText(savedInstanceState.getString("playerTwo"));
        this.firstPlayer1.setText(savedInstanceState.getString("firstPlayer1"));
        this.firstPlayer2.setText(savedInstanceState.getString("firstPlayer2"));

        this.game = (Game) savedInstanceState.getSerializable("game");
        this.timerStart = savedInstanceState.getBoolean("timerStart");
        this.timeToSet = savedInstanceState.getLong("timeToSet");

        if (timerStart) {
            this.startTimer();
        } else {
            this.stopTimer();
        }

    }
}
