package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Date;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;
import fr.corentin.roux.x_wing_score_tracker.model.Player;
import fr.corentin.roux.x_wing_score_tracker.model.Round;
import fr.corentin.roux.x_wing_score_tracker.model.Score;
import fr.corentin.roux.x_wing_score_tracker.model.Ship;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.model.TimerActivityModel;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.ShipListAdapter;
import fr.corentin.roux.x_wing_score_tracker.ui.dialog.EndDialogTimer;
import fr.corentin.roux.x_wing_score_tracker.utils.UIUtils;
import lombok.NonNull;

/**
 * @author Corentin Roux
 * <p>
 * Activity for the view of the scoring board
 */
public class TimerActivity extends AbstractActivity {

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private static final String RED = "#9d0208";
    private static final String GREEN = "#2b9348";

    private TimerActivityModel timerActivityModel = new TimerActivityModel();


    @Override
    protected void initContentView() {
        this.setContentView(R.layout.timer_layout);
    }

    @Override
    protected void initGame() {
        this.timerActivityModel.setGame(new Game());
        this.timerActivityModel.setSetting(SettingService.getInstance().getSetting(this));
        //Option d affichage du timer
        this.timerActivityModel.getGame().setHideTimeLeft((boolean) this.getIntent().getSerializableExtra("hideTimeLeft"));
        this.timerActivityModel.getGame().setHideTimer((boolean) this.getIntent().getSerializableExtra("hideTimer"));
        if (this.timerActivityModel.getSetting().getName() != null && !"".equals(this.timerActivityModel.getSetting().getName().trim())) {
            this.timerActivityModel.getGame().getPlayer1().setName(this.timerActivityModel.getSetting().getName());
        }
        if (this.timerActivityModel.getSetting().getOpponent() != null && !"".equals(this.timerActivityModel.getSetting().getOpponent().trim())) {
            this.timerActivityModel.getGame().getPlayer2().setName(this.timerActivityModel.getSetting().getOpponent());
        }
        //We set the timer at the time in minutes
        this.timerActivityModel.setTimeToSet(Long.parseLong(String.valueOf(this.getIntent().getSerializableExtra("timer"))) * MINUTES);
        this.timerActivityModel.setTimeStartRound(this.timerActivityModel.getTimeToSet());
        //On recup la mission active dans le main page
        this.timerActivityModel.getGame().setMission((Mission) this.getIntent().getSerializableExtra("mission"));
        //Set des listes des joueurs dans leurs profils de game
        this.timerActivityModel.getGame().getPlayer1().setShips(this.timerActivityModel.getSetting().getListPlayer1());
        this.timerActivityModel.getGame().getPlayer2().setShips(this.timerActivityModel.getSetting().getListPlayer2());
    }

    @Override
    protected void findView() {
        this.timerActivityModel.setTimeClock(this.findViewById(R.id.timeClock));
        this.timerActivityModel.setBtnStartStop( this.findViewById(R.id.btnStart));
        this.timerActivityModel.setRoundNumber(this.findViewById(R.id.roundNumber));
        this.timerActivityModel.setBtnLessRound(this.findViewById(R.id.btnLessRound));
        this.timerActivityModel.setBtnPlusRound(this.findViewById(R.id.btnPlusRound));
        this.timerActivityModel.setTextViewTimeLeft(this.findViewById(R.id.textViewTimeLeft));
        this.timerActivityModel.setTextViewMission(this.findViewById(R.id.textViewMission));
        //GLOBAL
        this.timerActivityModel.setPlayerOne(this.findViewById(R.id.playerOne));
        this.timerActivityModel.setPlayerTwo(this.findViewById(R.id.playerTwo));
        this.timerActivityModel.setPlayerOneList(this.findViewById(R.id.playerOneList));
        this.timerActivityModel.setPlayerTwoList(this.findViewById(R.id.playerTwoList));
        this.timerActivityModel.setTextViewScorePlayerOne(this.findViewById(R.id.scorePlayerOne));
        this.timerActivityModel.setTextViewScorePlayerTwo(this.findViewById(R.id.scorePlayerTwo));
        //Kill
        this.timerActivityModel.setTextViewScorePlayerOneKill(this.findViewById(R.id.scorePlayerOneKill));
        this.timerActivityModel.setTextViewScorePlayerTwoKill(this.findViewById(R.id.scorePlayerTwoKill));
        this.timerActivityModel.setBtnLessPlayerOneKill(this.findViewById(R.id.btnLessPlayerOneKill));
        this.timerActivityModel.setBtnPlusPlayerOneKill(this.findViewById(R.id.btnPlusPlayerOneKill));
        this.timerActivityModel.setBtnLessPlayerTwoKill(this.findViewById(R.id.btnLessPlayerTwoKill));
        this.timerActivityModel.setBtnPlusPlayerTwoKill(this.findViewById(R.id.btnPlusPlayerTwoKill));
        //Mission
        this.timerActivityModel.setTextViewScorePlayerOneMission(this.findViewById(R.id.scorePlayerOneMission));
        this.timerActivityModel.setTextViewScorePlayerTwoMission(this.findViewById(R.id.scorePlayerTwoMission));
        this.timerActivityModel.setBtnLessPlayerOneMission(this.findViewById(R.id.btnLessPlayerOneMission));
        this.timerActivityModel.setBtnPlusPlayerOneMission(this.findViewById(R.id.btnPlusPlayerOneMission));
        this.timerActivityModel.setBtnLessPlayerTwoMission(this.findViewById(R.id.btnLessPlayerTwoMission));
        this.timerActivityModel.setBtnPlusPlayerTwoMission(this.findViewById(R.id.btnPlusPlayerTwoMission));

        this.timerActivityModel.setFirstPlayer1(this.findViewById(R.id.firstPlayer1));
        this.timerActivityModel.setFirstPlayer2(this.findViewById(R.id.firstPlayer2));
        this.timerActivityModel.setFirstPlayerName(this.findViewById(R.id.firstPlayerName));

        this.timerActivityModel.setListShipPlayer1(this.findViewById(R.id.listShipPlayer1));
        this.timerActivityModel.setListShipPlayer2(this.findViewById(R.id.listShipPlayer2));
    }

    @Override
    protected void initDatas() {
        //Init Ring
        this.initRing();
        //Update of the view for the first time => set the fields
        this.updateTimer();
        if (this.timerActivityModel.getGame().isHideTimer()) {
            this.timerActivityModel.getTimeClock().setText("Secret Time !!");
            this.timerActivityModel.getTextViewTimeLeft().setText("");
        }
        if (this.timerActivityModel.getGame().isHideTimeLeft() && !this.timerActivityModel.getGame().isHideTimer()) {
            this.timerActivityModel.getTimeClock().setText(this.generateTimeLeft((int) this.timerActivityModel.getTimeToSet()));
            this.timerActivityModel.getTextViewTimeLeft().setText("Time");
        }
        final String nameP1 = this.timerActivityModel.getGame().getPlayer1().getName();
        if (!"".equals(nameP1)) {
            this.timerActivityModel.getPlayerOne().setText(nameP1);
            this.timerActivityModel.getPlayerOneList().setText(nameP1);
        }
        final String nameP2 = this.timerActivityModel.getGame().getPlayer2().getName();
        if (!"".equals(nameP2)) {
            this.timerActivityModel.getPlayerTwo().setText(nameP2);
            this.timerActivityModel.getPlayerTwoList().setText(nameP2);
        }
        this.timerActivityModel.getFirstPlayer1().setText(this.timerActivityModel.getGame().getPlayer1().getName());
        this.timerActivityModel.getFirstPlayer2().setText(this.timerActivityModel.getGame().getPlayer2().getName());
        //Init data
        this.initMission();
        //Init des adapters
        this.timerActivityModel.setShipAdapter1(new ShipListAdapter(this, this.timerActivityModel.getGame().getPlayer1()));
        this.timerActivityModel.setShipAdapter2(new ShipListAdapter(this, this.timerActivityModel.getGame().getPlayer2()));

        this.timerActivityModel.getListShipPlayer1().setAdapter(this.timerActivityModel.getShipAdapter1());
        this.timerActivityModel.getListShipPlayer2().setAdapter(this.timerActivityModel.getShipAdapter2());

        UIUtils.setListViewHeightBasedOnItems(timerActivityModel.getListShipPlayer1());
        UIUtils.setListViewHeightBasedOnItems(timerActivityModel.getListShipPlayer2());

        //whenever the data changes
        this.timerActivityModel.getShipAdapter1().notifyDataSetChanged();
        this.timerActivityModel.getShipAdapter2().notifyDataSetChanged();
    }

    @Override
    protected void initListeners() {
        this.timerActivityModel.getBtnStartStop().setOnClickListener(t -> {
            if (this.timerActivityModel.isTimerStart()) {
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

        this.timerActivityModel.getTextViewMission().setOnClickListener(t -> {
            if (Mission.NO_MISSION.equals(this.timerActivityModel.getGame().getMission())) {
                Toast.makeText(this, "No Mission for this game !!", Toast.LENGTH_SHORT).show();
            } else {
                final Intent intent = new Intent(this, MissionDetailActivity.class);
                intent.putExtra("mission", this.timerActivityModel.getGame().getMission().getCode());
                this.startActivity(intent);
            }
        });
    }


    private void initMission() {
        if (this.timerActivityModel.getGame().getMission() != null && !Mission.NO_MISSION.equals(this.timerActivityModel.getGame().getMission())) {
            this.timerActivityModel.getTextViewMission().setText(this.timerActivityModel.getGame().getMission().getLibelle());
        }
    }

    private void initRing() {
        final Uri alarmTone = this.timerActivityModel.getSetting().getPathRingTone() != null ?
                Uri.parse(this.timerActivityModel.getSetting().getPathRingTone()) :
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        this.timerActivityModel.setRingtoneAlarm(RingtoneManager.getRingtone(this.getApplicationContext(), alarmTone));
        this.timerActivityModel.getRingtoneAlarm().setStreamType(AudioManager.STREAM_ALARM);
    }

    /**
     * Update the timer with the time left
     */
    private void updateTimer() {
        final StringBuilder timeLeft = this.generateTimeLeft((int) this.timerActivityModel.getTimeToSet());

        if (!this.timerActivityModel.getGame().isHideTimeLeft() && !this.timerActivityModel.getGame().isHideTimer()) {
            this.timerActivityModel.getTimeClock().setText(timeLeft.toString());
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


    private void checkBoxListeners() {
        this.timerActivityModel.getFirstPlayer1().setOnClickListener(t -> {
            if (this.timerActivityModel.getFirstPlayerChoice() != 1) {
                this.timerActivityModel.setFirstPlayerChoice(1);
                this.timerActivityModel.getFirstPlayerName().setText(timerActivityModel.getGame().getPlayer1().getName());
            }
        });
        this.timerActivityModel.getFirstPlayer2().setOnClickListener(t -> {
            if (this.timerActivityModel.getFirstPlayerChoice() != 2) {
                this.timerActivityModel.setFirstPlayerChoice(2);
                this.timerActivityModel.getFirstPlayerName().setText(timerActivityModel.getGame().getPlayer2().getName());
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
        this.timerActivityModel.getBtnLessPlayerOneKill().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().lessScoreKill(1);
            this.timerActivityModel.getGame().getPlayer1().lessScoreKill(1);
            this.updateScorePlayerOne();
        });

        this.timerActivityModel.getBtnPlusPlayerOneKill().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().addScoreKill(1);
            this.timerActivityModel.getGame().getPlayer1().addScoreKill(1);
            this.updateScorePlayerOne();
        });
    }

    private void playerOneListenersMission() {
        this.timerActivityModel.getBtnLessPlayerOneMission().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().lessScoreMission(1);
            this.timerActivityModel.getGame().getPlayer1().lessScoreMission(1);
            this.updateScorePlayerOne();
        });

        this.timerActivityModel.getBtnPlusPlayerOneMission().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().addScoreMission(1);
            this.timerActivityModel.getGame().getPlayer1().addScoreMission(1);
            this.updateScorePlayerOne();
        });
    }

    private void updateScorePlayerOne() {
        this.timerActivityModel.getTextViewScorePlayerOneKill().setText(String.valueOf(this.timerActivityModel.getGame().getPlayer1().getScore().getScoreKill()));
        this.timerActivityModel.getTextViewScorePlayerOneMission().setText(String.valueOf(this.timerActivityModel.getGame().getPlayer1().getScore().getScoreMission()));
        this.timerActivityModel.getTextViewScorePlayerOne().setText(String.valueOf(this.timerActivityModel.getGame().getPlayer1().getScore().getScoreGlobal()));
    }

    private void playerTwoListeners() {
        this.playerTwoListenersKill();

        this.playerTwoListenersMission();
    }

    private void playerTwoListenersKill() {
        this.timerActivityModel.getBtnLessPlayerTwoKill().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().lessScoreKill(1);
            this.timerActivityModel.getGame().getPlayer2().lessScoreKill(1);
            this.updateScorePlayerTwo();
        });

        this.timerActivityModel.getBtnPlusPlayerTwoKill().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().addScoreKill(1);
            this.timerActivityModel.getGame().getPlayer2().addScoreKill(1);
            this.updateScorePlayerTwo();
        });
    }

    private void playerTwoListenersMission() {
        this.timerActivityModel.getBtnLessPlayerTwoMission().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().lessScoreMission(1);
            this.timerActivityModel.getGame().getPlayer2().lessScoreMission(1);
            this.updateScorePlayerTwo();
        });

        this.timerActivityModel.getBtnPlusPlayerTwoMission().setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().addScoreMission(1);
            this.timerActivityModel.getGame().getPlayer2().addScoreMission(1);
            this.updateScorePlayerTwo();
        });
    }

    private void updateScorePlayerTwo() {
        this.timerActivityModel.getTextViewScorePlayerTwoKill().setText(String.valueOf(this.timerActivityModel.getGame().getPlayer2().getScore().getScoreKill()));
        this.timerActivityModel.getTextViewScorePlayerTwoMission().setText(String.valueOf(this.timerActivityModel.getGame().getPlayer2().getScore().getScoreMission()));
        this.timerActivityModel.getTextViewScorePlayerTwo().setText(String.valueOf(this.timerActivityModel.getGame().getPlayer2().getScore().getScoreGlobal()));
    }

    private void roundListeners() {
        this.timerActivityModel.getBtnLessRound().setOnClickListener(t -> removeRound());
        this.timerActivityModel.getBtnPlusRound().setOnClickListener(t -> this.addRound());
    }

    private void removeRound() {
        if (this.timerActivityModel.getGame().getRound() > 0) {
            this.timerActivityModel.getGame().removeRound();
            this.timerActivityModel.getRoundNumber().setText(String.valueOf(this.timerActivityModel.getGame().getRound()));
        }
    }

    private void addRound() {
        this.updateRoundDetail();
        this.timerActivityModel.getGame().addRound();
        if (this.timerActivityModel.getGame().getRound() == 12) {
            Toast.makeText(TimerActivity.this, "LAST TURN !!", Toast.LENGTH_LONG).show();
        }
        this.timerActivityModel.getRoundNumber().setText(String.valueOf(this.timerActivityModel.getGame().getRound()));
    }


    private void updateRoundDetail() {
        final Round round = new Round();
        round.setRoundNumber(this.timerActivityModel.getRoundNumber().getText().toString());
        round.setFirstPlayer(this.timerActivityModel.getFirstPlayerName().getText().toString());
        round.setScorePlayer1(this.timerActivityModel.getScoreRoundJoueur1());
        round.setScorePlayer2(this.timerActivityModel.getScoreRoundJoueur2());
        round.setTime(this.timerActivityModel.getTimeStartRound() - this.timerActivityModel.getTimeToSet());
        this.timerActivityModel.getGame().getRounds().add(round);
        //Reset des scores du round post save
        this.timerActivityModel.setTimeStartRound(this.timerActivityModel.getTimeToSet());
        this.timerActivityModel.setScoreRoundJoueur1(new Score());
        this.timerActivityModel.setScoreRoundJoueur2(new Score());
    }

    /**
     * the trigger for start the timer
     */
    private void startTimer() {
        this.timerActivityModel.setTimer( new CountDownTimer(this.timerActivityModel.getTimeToSet(), SECONDES) {
            /**
             * trigger every {@fields SECONDES} millisecondes
             * {@inheritDoc}
             */
            @Override
            public void onTick(final long time) {
                TimerActivity.this.timerActivityModel.setTimeToSet(time);
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
        }.start());
        this.timerActivityModel.getBtnStartStop().setText(this.getString(R.string.stop));
        this.timerActivityModel.getBtnStartStop().setBackgroundColor(Color.parseColor(RED));
        this.timerActivityModel.setTimerStart(true);
        if (this.timerActivityModel.getGame().getRound() == 0) {
            this.addRound();
        }
    }

    /**
     * the trigger for stop the timer
     */
    private void stopTimer() {
        if (timerActivityModel.getTimer() != null) {
            this.timerActivityModel.getTimer().cancel();
        }
        this.timerActivityModel.getBtnStartStop().setText(this.getString(R.string.start));
        this.timerActivityModel.getBtnStartStop().setBackgroundColor(Color.parseColor(GREEN));
        if (timerActivityModel.isTimerStart()) {
            this.timerActivityModel.setTimerStart(false);
            if (this.timerActivityModel.getRingtoneAlarm().isPlaying()) {
                this.timerActivityModel.getRingtoneAlarm().stop();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (!this.timerActivityModel.isEnd()) {
            final EndDialogTimer endDialogTimer = new EndDialogTimer(this);
            endDialogTimer.show(this.getSupportFragmentManager(), "dialogHelp");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        this.updateRoundDetail();
        this.timerActivityModel.getGame().setTimeLeft(this.timerActivityModel.getTimeToSet());
        this.timerActivityModel.getGame().setDate(this.timerActivityModel.getDateFormat().format(new Date()));

        HistoriqueService.getInstance().saveNewGame(this.getBaseContext(), this.timerActivityModel.getGame());

        if (timerActivityModel.getTimer() != null) {
            this.timerActivityModel.getTimer().cancel();
        }
        if (this.timerActivityModel.getRingtoneAlarm().isPlaying()) {
            this.timerActivityModel.getRingtoneAlarm().stop();
        }

        super.onDestroy();
    }


    private void playSound() {
        if (this.timerActivityModel.isAlreadyEnd()) {
            return;
        }
        this.timerActivityModel.setAlreadyEnd(true);
        this.timerActivityModel.getRingtoneAlarm().play();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("timerActivityModel", this.timerActivityModel);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.timerActivityModel = (TimerActivityModel) savedInstanceState.getSerializable("timerActivityModel");

        if (this.timerActivityModel.isTimerStart()) {
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
        return timerActivityModel.getGame();
    }

    public void setEnd(boolean end) {
        this.timerActivityModel.setEnd(end);
    }

    public void changeScoreByOpponentPlayer(Player player, Ship current, Ship.Statut oldStatut) {
        if (player == timerActivityModel.getGame().getPlayer1()) {
            changeScoreByPlayer(timerActivityModel.getGame().getPlayer2(), current, oldStatut);
        } else {
            changeScoreByPlayer(timerActivityModel.getGame().getPlayer1(), current, oldStatut);
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
