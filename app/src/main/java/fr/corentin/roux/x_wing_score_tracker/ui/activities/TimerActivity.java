package fr.corentin.roux.x_wing_score_tracker.ui.activities;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;
import fr.corentin.roux.x_wing_score_tracker.model.Player;
import fr.corentin.roux.x_wing_score_tracker.model.Round;
import fr.corentin.roux.x_wing_score_tracker.model.Score;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.model.Ship;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.ShipListAdapter;
import fr.corentin.roux.x_wing_score_tracker.ui.dialog.EndDialogTimer;
import fr.corentin.roux.x_wing_score_tracker.utils.UIUtils;
import lombok.NonNull;

/**
 * @author Corentin Roux
 * <p>
 * Activity for the view of the scoring board
 */
public class TimerActivity extends AppCompatActivity {

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private static final String RED = "#9d0208";
    private static final String GREEN = "#2b9348";
    private final HistoriqueService historiqueService;
    private final SettingService service = SettingService.getInstance();
    private Setting setting;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
    private TextView playerOneList;
    private TextView playerTwoList;
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
    private boolean alreadyEnd = false;

    private Score scoreRoundJoueur1 = new Score();
    private Score scoreRoundJoueur2 = new Score();
    private long timeStartRound = 0;

    private ShipListAdapter shipAdapter1;
    private ShipListAdapter shipAdapter2;
    private ListView listShipPlayer1;
    private ListView listShipPlayer2;

    public TimerActivity() {
        this.historiqueService = HistoriqueService.getInstance();
    }

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
        this.timeStartRound = this.timeToSet;
        //On recup la mission active dans le main page
        this.game.setMission((Mission) this.getIntent().getSerializableExtra("mission"));
        //Set des listes des joueurs dans leurs profils de game
        this.game.getPlayer1().setShips(this.setting.getListPlayer1());
        this.game.getPlayer2().setShips(this.setting.getListPlayer2());
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
            this.playerOneList.setText(nameP1);
        }
        final String nameP2 = this.game.getPlayer2().getName();
        if (!"".equals(nameP2)) {
            this.playerTwo.setText(nameP2);
            this.playerTwoList.setText(nameP2);
        }
        this.firstPlayer1.setText(game.getPlayer1().getName());
        this.firstPlayer2.setText(game.getPlayer2().getName());
        //Init data
        this.initMission();
        //Init des adapters
        this.shipAdapter1 = new ShipListAdapter(this, game.getPlayer1());
        this.shipAdapter2 = new ShipListAdapter(this, game.getPlayer2());

        this.listShipPlayer1.setAdapter(this.shipAdapter1);
        this.listShipPlayer2.setAdapter(this.shipAdapter2);

        UIUtils.setListViewHeightBasedOnItems(listShipPlayer1);
        UIUtils.setListViewHeightBasedOnItems(listShipPlayer2);

        //whenever the data changes
        shipAdapter1.notifyDataSetChanged();
        shipAdapter2.notifyDataSetChanged();
    }

    private void initMission() {
        if (this.game.getMission() != null && !Mission.NO_MISSION.equals(this.game.getMission())) {
            this.textViewMission.setText(this.game.getMission().getLibelle());
        }
    }

    private void initRing() {
        final Uri alarmTone = this.setting.getPathRingTone() != null ?
                Uri.parse(this.setting.getPathRingTone()) :
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

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
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            } else {
                this.startTimer();
                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    public void updateScorePlayers() {
        this.updateScorePlayerOne();
        this.updateScorePlayerTwo();
    }

    private void playerOneListeners() {
        this.playerOneListenersKill();

        this.playerOneListenersMission();
    }

    private void playerOneListenersKill() {
        this.btnLessPlayerOneKill.setOnClickListener(t -> {
            this.scoreRoundJoueur1.lessScoreKill(1);
            this.game.getPlayer1().lessScoreKill(1);
            this.updateScorePlayerOne();
        });

        this.btnPlusPlayerOneKill.setOnClickListener(t -> {
            this.scoreRoundJoueur1.addScoreKill(1);
            this.game.getPlayer1().addScoreKill(1);
            this.updateScorePlayerOne();
        });
    }

    private void playerOneListenersMission() {
        this.btnLessPlayerOneMission.setOnClickListener(t -> {
            this.scoreRoundJoueur1.lessScoreMission(1);
            this.game.getPlayer1().lessScoreMission(1);
            this.updateScorePlayerOne();
        });

        this.btnPlusPlayerOneMission.setOnClickListener(t -> {
            this.scoreRoundJoueur1.addScoreMission(1);
            this.game.getPlayer1().addScoreMission(1);
            this.updateScorePlayerOne();
        });
    }

    private void updateScorePlayerOne() {
        this.textViewScorePlayerOneKill.setText(String.valueOf(this.game.getPlayer1().getScore().getScoreKill()));
        this.textViewScorePlayerOneMission.setText(String.valueOf(this.game.getPlayer1().getScore().getScoreMission()));
        this.textViewScorePlayerOne.setText(String.valueOf(this.game.getPlayer1().getScore().getScoreGlobal()));
    }

    private void playerTwoListeners() {
        this.playerTwoListenersKill();

        this.playerTwoListenersMission();
    }

    private void playerTwoListenersKill() {
        this.btnLessPlayerTwoKill.setOnClickListener(t -> {
            this.scoreRoundJoueur2.lessScoreKill(1);
            this.game.getPlayer2().lessScoreKill(1);
            this.updateScorePlayerTwo();
        });

        this.btnPlusPlayerTwoKill.setOnClickListener(t -> {
            this.scoreRoundJoueur2.addScoreKill(1);
            this.game.getPlayer2().addScoreKill(1);
            this.updateScorePlayerTwo();
        });
    }

    private void playerTwoListenersMission() {
        this.btnLessPlayerTwoMission.setOnClickListener(t -> {
            this.scoreRoundJoueur2.lessScoreMission(1);
            this.game.getPlayer2().lessScoreMission(1);
            this.updateScorePlayerTwo();
        });

        this.btnPlusPlayerTwoMission.setOnClickListener(t -> {
            this.scoreRoundJoueur2.addScoreMission(1);
            this.game.getPlayer2().addScoreMission(1);
            this.updateScorePlayerTwo();
        });
    }

    private void updateScorePlayerTwo() {
        this.textViewScorePlayerTwoKill.setText(String.valueOf(this.game.getPlayer2().getScore().getScoreKill()));
        this.textViewScorePlayerTwoMission.setText(String.valueOf(this.game.getPlayer2().getScore().getScoreMission()));
        this.textViewScorePlayerTwo.setText(String.valueOf(this.game.getPlayer2().getScore().getScoreGlobal()));
    }

    private void roundListeners() {
        this.btnLessRound.setOnClickListener(t -> removeRound());
        this.btnPlusRound.setOnClickListener(t -> this.addRound());
    }

    private void removeRound() {
        if (this.game.getRound() > 0) {
            this.game.removeRound();
            this.roundNumber.setText(String.valueOf(this.game.getRound()));
        }
    }

    private void addRound() {
        this.updateRoundDetail();
        this.game.addRound();
        if (this.game.getRound() == 12) {
            Toast.makeText(TimerActivity.this, "LAST TURN !!", Toast.LENGTH_LONG).show();
        }
        this.roundNumber.setText(String.valueOf(this.game.getRound()));
    }


    private void updateRoundDetail() {
        final Round round = new Round();
        round.setRoundNumber(this.roundNumber.getText().toString());
        round.setFirstPlayer(this.firstPlayerName.getText().toString());
        round.setScorePlayer1(this.scoreRoundJoueur1);
        round.setScorePlayer2(this.scoreRoundJoueur2);
        round.setTime(this.timeStartRound - this.timeToSet);
        this.game.getRounds().add(round);
        //Reset des scores du round post save
        this.timeStartRound = this.timeToSet;
        this.scoreRoundJoueur1 = new Score();
        this.scoreRoundJoueur2 = new Score();
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
        this.btnStartStop.setText(this.getString(R.string.stop));
        this.btnStartStop.setBackgroundColor(Color.parseColor(RED));
        this.timerStart = true;
        if (this.game.getRound() == 0) {
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
        if (timerStart) {
            this.timerStart = false;
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
        this.playerOneList = this.findViewById(R.id.playerOneList);
        this.playerTwoList = this.findViewById(R.id.playerTwoList);
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

        this.listShipPlayer1 = this.findViewById(R.id.listShipPlayer1);
        this.listShipPlayer2 = this.findViewById(R.id.listShipPlayer2);
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
        this.updateRoundDetail();
        this.game.setTimeLeft(this.timeToSet);
        this.game.setDate(this.dateFormat.format(new Date()));

        this.historiqueService.saveNewGame(this.getBaseContext(), this.game);

        if (timer != null) {
            this.timer.cancel();
        }
        if (this.ringtoneAlarm.isPlaying()) {
            this.ringtoneAlarm.stop();
        }

        super.onDestroy();
    }


    private void playSound() {
        if (this.alreadyEnd) {
            return;
        }
        this.alreadyEnd = true;
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
        outState.putBoolean("alreadyEnd", this.alreadyEnd);
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
        this.playerOneList.setText(savedInstanceState.getString("playerOne"));
        this.playerTwoList.setText(savedInstanceState.getString("playerTwo"));
        this.firstPlayer1.setText(savedInstanceState.getString("firstPlayer1"));
        this.firstPlayer2.setText(savedInstanceState.getString("firstPlayer2"));

        this.game = (Game) savedInstanceState.getSerializable("game");
        this.timerStart = savedInstanceState.getBoolean("timerStart");
        this.alreadyEnd = savedInstanceState.getBoolean("alreadyEnd");
        this.timeToSet = savedInstanceState.getLong("timeToSet");

        if (timerStart) {
            this.startTimer();
        } else {
            this.stopTimer();
        }

        //Init des datas de la page
        this.initDatas();
        //Initialization of the listeners
        this.initListeners();
    }

    public Game getGame() {
        return game;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void changeScoreByOpponentPlayer(Player player, Ship current, Ship.Statut oldStatut) {
        if (player == game.getPlayer1()) {
            changeScoreByPlayer(game.getPlayer2(), current, oldStatut);
        } else {
            changeScoreByPlayer(game.getPlayer1(), current, oldStatut);
        }
    }

    public void changeScoreByPlayer(Player player, Ship current, Ship.Statut oldStatut) {
        if (oldStatut.equals(Ship.Statut.FULL)) {
            player.getScore().addScoreKill(current.getPoints() / 2);
        } else if (oldStatut.equals(Ship.Statut.HALF)) {
            player.getScore().lessScoreKill(current.getPoints() / 2);
            player.getScore().addScoreKill(current.getPoints());
        } else if (oldStatut.equals(Ship.Statut.DEAD)) {
            player.getScore().lessScoreKill(current.getPoints());
        }
    }
}
