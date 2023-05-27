package fr.corentin.roux.x_wing_score_tracker.ui.activities.model;

import android.media.Ringtone;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Score;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.ShipListAdapter;

public class TimerActivityModel implements Serializable {

    private Setting setting;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private boolean end = false;
    private boolean timerStart = false;
    private long timeToSet;
    private CountDownTimer timer;
    private Ringtone ringtoneAlarm;
    private Game game;

    private TextView timeClock;
    private TextView roundNumber;
    private TextView textViewTimeLeft;
    private TextView textViewMission;
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
    private TextView firstPlayerName;

    private Button btnStartStop;
    private Button btnLessRound;
    private Button btnPlusRound;
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
    private int firstPlayerChoice = 0;
    private boolean alreadyEnd = false;
    private Score scoreRoundJoueur1 = new Score();
    private Score scoreRoundJoueur2 = new Score();
    private long timeStartRound = 0;
    private ShipListAdapter shipAdapter1;
    private ShipListAdapter shipAdapter2;
    private ListView listShipPlayer1;
    private ListView listShipPlayer2;

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isTimerStart() {
        return timerStart;
    }

    public void setTimerStart(boolean timerStart) {
        this.timerStart = timerStart;
    }

    public TextView getTimeClock() {
        return timeClock;
    }

    public void setTimeClock(TextView timeClock) {
        this.timeClock = timeClock;
    }

    public Button getBtnStartStop() {
        return btnStartStop;
    }

    public void setBtnStartStop(Button btnStartStop) {
        this.btnStartStop = btnStartStop;
    }

    public long getTimeToSet() {
        return timeToSet;
    }

    public void setTimeToSet(long timeToSet) {
        this.timeToSet = timeToSet;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public TextView getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(TextView roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Button getBtnLessRound() {
        return btnLessRound;
    }

    public void setBtnLessRound(Button btnLessRound) {
        this.btnLessRound = btnLessRound;
    }

    public Button getBtnPlusRound() {
        return btnPlusRound;
    }

    public void setBtnPlusRound(Button btnPlusRound) {
        this.btnPlusRound = btnPlusRound;
    }

    public TextView getTextViewTimeLeft() {
        return textViewTimeLeft;
    }

    public void setTextViewTimeLeft(TextView textViewTimeLeft) {
        this.textViewTimeLeft = textViewTimeLeft;
    }

    public Ringtone getRingtoneAlarm() {
        return ringtoneAlarm;
    }

    public void setRingtoneAlarm(Ringtone ringtoneAlarm) {
        this.ringtoneAlarm = ringtoneAlarm;
    }

    public TextView getTextViewMission() {
        return textViewMission;
    }

    public void setTextViewMission(TextView textViewMission) {
        this.textViewMission = textViewMission;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public TextView getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(TextView playerOne) {
        this.playerOne = playerOne;
    }

    public TextView getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(TextView playerTwo) {
        this.playerTwo = playerTwo;
    }

    public TextView getPlayerOneList() {
        return playerOneList;
    }

    public void setPlayerOneList(TextView playerOneList) {
        this.playerOneList = playerOneList;
    }

    public TextView getPlayerTwoList() {
        return playerTwoList;
    }

    public void setPlayerTwoList(TextView playerTwoList) {
        this.playerTwoList = playerTwoList;
    }

    public TextView getTextViewScorePlayerOne() {
        return textViewScorePlayerOne;
    }

    public void setTextViewScorePlayerOne(TextView textViewScorePlayerOne) {
        this.textViewScorePlayerOne = textViewScorePlayerOne;
    }

    public TextView getTextViewScorePlayerTwo() {
        return textViewScorePlayerTwo;
    }

    public void setTextViewScorePlayerTwo(TextView textViewScorePlayerTwo) {
        this.textViewScorePlayerTwo = textViewScorePlayerTwo;
    }

    public TextView getTextViewScorePlayerOneKill() {
        return textViewScorePlayerOneKill;
    }

    public void setTextViewScorePlayerOneKill(TextView textViewScorePlayerOneKill) {
        this.textViewScorePlayerOneKill = textViewScorePlayerOneKill;
    }

    public TextView getTextViewScorePlayerTwoKill() {
        return textViewScorePlayerTwoKill;
    }

    public void setTextViewScorePlayerTwoKill(TextView textViewScorePlayerTwoKill) {
        this.textViewScorePlayerTwoKill = textViewScorePlayerTwoKill;
    }

    public TextView getTextViewScorePlayerOneMission() {
        return textViewScorePlayerOneMission;
    }

    public void setTextViewScorePlayerOneMission(TextView textViewScorePlayerOneMission) {
        this.textViewScorePlayerOneMission = textViewScorePlayerOneMission;
    }

    public TextView getTextViewScorePlayerTwoMission() {
        return textViewScorePlayerTwoMission;
    }

    public void setTextViewScorePlayerTwoMission(TextView textViewScorePlayerTwoMission) {
        this.textViewScorePlayerTwoMission = textViewScorePlayerTwoMission;
    }

    public Button getBtnLessPlayerOneKill() {
        return btnLessPlayerOneKill;
    }

    public void setBtnLessPlayerOneKill(Button btnLessPlayerOneKill) {
        this.btnLessPlayerOneKill = btnLessPlayerOneKill;
    }

    public Button getBtnPlusPlayerOneKill() {
        return btnPlusPlayerOneKill;
    }

    public void setBtnPlusPlayerOneKill(Button btnPlusPlayerOneKill) {
        this.btnPlusPlayerOneKill = btnPlusPlayerOneKill;
    }

    public Button getBtnLessPlayerTwoKill() {
        return btnLessPlayerTwoKill;
    }

    public void setBtnLessPlayerTwoKill(Button btnLessPlayerTwoKill) {
        this.btnLessPlayerTwoKill = btnLessPlayerTwoKill;
    }

    public Button getBtnPlusPlayerTwoKill() {
        return btnPlusPlayerTwoKill;
    }

    public void setBtnPlusPlayerTwoKill(Button btnPlusPlayerTwoKill) {
        this.btnPlusPlayerTwoKill = btnPlusPlayerTwoKill;
    }

    public Button getBtnLessPlayerOneMission() {
        return btnLessPlayerOneMission;
    }

    public void setBtnLessPlayerOneMission(Button btnLessPlayerOneMission) {
        this.btnLessPlayerOneMission = btnLessPlayerOneMission;
    }

    public Button getBtnPlusPlayerOneMission() {
        return btnPlusPlayerOneMission;
    }

    public void setBtnPlusPlayerOneMission(Button btnPlusPlayerOneMission) {
        this.btnPlusPlayerOneMission = btnPlusPlayerOneMission;
    }

    public Button getBtnLessPlayerTwoMission() {
        return btnLessPlayerTwoMission;
    }

    public void setBtnLessPlayerTwoMission(Button btnLessPlayerTwoMission) {
        this.btnLessPlayerTwoMission = btnLessPlayerTwoMission;
    }

    public Button getBtnPlusPlayerTwoMission() {
        return btnPlusPlayerTwoMission;
    }

    public void setBtnPlusPlayerTwoMission(Button btnPlusPlayerTwoMission) {
        this.btnPlusPlayerTwoMission = btnPlusPlayerTwoMission;
    }

    public Button getFirstPlayer1() {
        return firstPlayer1;
    }

    public void setFirstPlayer1(Button firstPlayer1) {
        this.firstPlayer1 = firstPlayer1;
    }

    public Button getFirstPlayer2() {
        return firstPlayer2;
    }

    public void setFirstPlayer2(Button firstPlayer2) {
        this.firstPlayer2 = firstPlayer2;
    }

    public TextView getFirstPlayerName() {
        return firstPlayerName;
    }

    public void setFirstPlayerName(TextView firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public int getFirstPlayerChoice() {
        return firstPlayerChoice;
    }

    public void setFirstPlayerChoice(int firstPlayerChoice) {
        this.firstPlayerChoice = firstPlayerChoice;
    }

    public boolean isAlreadyEnd() {
        return alreadyEnd;
    }

    public void setAlreadyEnd(boolean alreadyEnd) {
        this.alreadyEnd = alreadyEnd;
    }

    public Score getScoreRoundJoueur1() {
        return scoreRoundJoueur1;
    }

    public void setScoreRoundJoueur1(Score scoreRoundJoueur1) {
        this.scoreRoundJoueur1 = scoreRoundJoueur1;
    }

    public Score getScoreRoundJoueur2() {
        return scoreRoundJoueur2;
    }

    public void setScoreRoundJoueur2(Score scoreRoundJoueur2) {
        this.scoreRoundJoueur2 = scoreRoundJoueur2;
    }

    public long getTimeStartRound() {
        return timeStartRound;
    }

    public void setTimeStartRound(long timeStartRound) {
        this.timeStartRound = timeStartRound;
    }

    public ShipListAdapter getShipAdapter1() {
        return shipAdapter1;
    }

    public void setShipAdapter1(ShipListAdapter shipAdapter1) {
        this.shipAdapter1 = shipAdapter1;
    }

    public ShipListAdapter getShipAdapter2() {
        return shipAdapter2;
    }

    public void setShipAdapter2(ShipListAdapter shipAdapter2) {
        this.shipAdapter2 = shipAdapter2;
    }

    public ListView getListShipPlayer1() {
        return listShipPlayer1;
    }

    public void setListShipPlayer1(ListView listShipPlayer1) {
        this.listShipPlayer1 = listShipPlayer1;
    }

    public ListView getListShipPlayer2() {
        return listShipPlayer2;
    }

    public void setListShipPlayer2(ListView listShipPlayer2) {
        this.listShipPlayer2 = listShipPlayer2;
    }
}
