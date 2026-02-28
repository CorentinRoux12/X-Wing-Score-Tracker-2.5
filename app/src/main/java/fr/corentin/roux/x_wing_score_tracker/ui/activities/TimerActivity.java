package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.DiceCounter;
import fr.corentin.roux.x_wing_score_tracker.model.DiceFace;
import fr.corentin.roux.x_wing_score_tracker.model.DiceTurn;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;
import fr.corentin.roux.x_wing_score_tracker.model.Player;
import fr.corentin.roux.x_wing_score_tracker.model.Round;
import fr.corentin.roux.x_wing_score_tracker.model.Score;
import fr.corentin.roux.x_wing_score_tracker.model.Ship;
import fr.corentin.roux.x_wing_score_tracker.services.GameService;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.model.TimerActivityModel;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.ShipListAdapter;
import fr.corentin.roux.x_wing_score_tracker.ui.dialog.EndDialogTimer;
import fr.corentin.roux.x_wing_score_tracker.utils.UIUtils;
import io.vavr.control.Try;

/**
 * @author Corentin Roux
 * <p>
 * Activity for the view of the scoring board
 */
@SuppressLint("SetTextI18n")
public class TimerActivity extends AbstractActivity
{

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private static final String RED = "#CD1B1D";
    private static final String GREEN = "#2FC859";

    private TimerActivityModel timerActivityModel = new TimerActivityModel();

    private CountDownTimer timer;
    private Ringtone ringtoneAlarm;
    private LinearLayout linearDiceLayout;
    private LinearLayout linearListLayout;
    private LinearLayout linearListLayout1;
    private LinearLayout linearListLayout2;

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
    //DICE
    private TextView totalRedPlayerOne;
    private TextView totalRedPlayerTwo;
    private TextView totalGreenPlayerOne;
    private TextView totalGreenPlayerTwo;
    private TextView scorePlayerOneAttackDicesCrit;
    private TextView scorePlayerTwoAttackDicesCrit;
    private TextView scorePlayerOneAttackDicesHit;
    private TextView scorePlayerTwoAttackDicesHit;
    private TextView scorePlayerOneAttackDicesEye;
    private TextView scorePlayerTwoAttackDicesEye;
    private TextView scorePlayerOneAttackDicesBlank;
    private TextView scorePlayerTwoAttackDicesBlank;
    private TextView scorePlayerOneDefenseDicesEvade;
    private TextView scorePlayerTwoDefenseDicesEvade;
    private TextView scorePlayerOneDefenseDicesEye;
    private TextView scorePlayerTwoDefenseDicesEye;
    private TextView scorePlayerOneDefenseDicesBlank;
    private TextView scorePlayerTwoDefenseDicesBlank;

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
    //DICE
    private Button btnLessPlayerOneAttackDicesCrit;
    private Button btnPlusPlayerOneAttackDicesCrit;
    private Button btnLessPlayerTwoAttackDicesCrit;
    private Button btnPlusPlayerTwoAttackDicesCrit;
    private Button btnLessPlayerOneAttackDicesHit;
    private Button btnPlusPlayerOneAttackDicesHit;
    private Button btnLessPlayerTwoAttackDicesHit;
    private Button btnPlusPlayerTwoAttackDicesHit;
    private Button btnLessPlayerOneAttackDicesEye;
    private Button btnPlusPlayerOneAttackDicesEye;
    private Button btnLessPlayerTwoAttackDicesEye;
    private Button btnPlusPlayerTwoAttackDicesEye;
    private Button btnLessPlayerOneAttackDicesBlank;
    private Button btnPlusPlayerOneAttackDicesBlank;
    private Button btnLessPlayerTwoAttackDicesBlank;
    private Button btnPlusPlayerTwoAttackDicesBlank;
    private Button btnLessPlayerOneDefenseDicesEvade;
    private Button btnPlusPlayerOneDefenseDicesEvade;
    private Button btnLessPlayerTwoDefenseDicesEvade;
    private Button btnPlusPlayerTwoDefenseDicesEvade;
    private Button btnLessPlayerOneDefenseDicesEye;
    private Button btnPlusPlayerOneDefenseDicesEye;
    private Button btnLessPlayerTwoDefenseDicesEye;
    private Button btnPlusPlayerTwoDefenseDicesEye;
    private Button btnLessPlayerOneDefenseDicesBlank;
    private Button btnPlusPlayerOneDefenseDicesBlank;
    private Button btnLessPlayerTwoDefenseDicesBlank;
    private Button btnPlusPlayerTwoDefenseDicesBlank;

    private ListView listShipPlayer1;
    private ListView listShipPlayer2;

    private ShipListAdapter shipAdapter1;
    private ShipListAdapter shipAdapter2;

    @Override
    protected void initContentView()
    {
        this.setContentView(R.layout.timer_layout);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void initGame()
    {
        this.timerActivityModel.setGame(new Game());
        this.timerActivityModel.setSetting(SettingService.getInstance().get(this));
        //Option d affichage du timer
        this.timerActivityModel.getGame().setHideTimeLeft((boolean) this.getIntent().getSerializableExtra("hideTimeLeft"));
        this.timerActivityModel.getGame().setHideTimer((boolean) this.getIntent().getSerializableExtra("hideTimer"));
        if (this.timerActivityModel.getSetting().getName() != null && !this.timerActivityModel.getSetting().getName().trim().isEmpty())
        {
            this.timerActivityModel.getGame().setNamePlayer1(this.timerActivityModel.getSetting().getName());
        }
        if (this.timerActivityModel.getSetting().getOpponent() != null && !this.timerActivityModel.getSetting().getOpponent().trim().isEmpty())
        {
            this.timerActivityModel.getGame().setNamePlayer2(this.timerActivityModel.getSetting().getOpponent());
        }
        //We set the timer at the time in minutes
        this.timerActivityModel.setTimeToSet(Long.parseLong(String.valueOf(this.getIntent().getSerializableExtra("timer"))) * MINUTES);
        this.timerActivityModel.setTimeStartRound(this.timerActivityModel.getTimeToSet());
        //On recup la mission active dans le main page
        this.timerActivityModel.getGame().setMission(((Mission) this.getIntent().getSerializableExtra("mission")).getLibelle());
        //Set des listes des joueurs dans leurs profils de game
        this.timerActivityModel.getGame().setXwsShipsPlayer1(this.timerActivityModel.getSetting().getListPlayer1());
        this.timerActivityModel.getGame().setXwsShipsPlayer2(this.timerActivityModel.getSetting().getListPlayer2());
    }

    @Override
    protected void initDatas()
    {
        //Init Ring
        this.initRing();
        //Update of the view for the first time => set the fields
        this.updateTimer();
        if (this.timerActivityModel.getGame().isHideTimer())
        {
            this.timeClock.setText("Secret Time !!");
            this.textViewTimeLeft.setText("");
        }
        if (this.timerActivityModel.getGame().isHideTimeLeft() && !this.timerActivityModel.getGame().isHideTimer())
        {
            this.timeClock.setText(this.generateTimeLeft((int) this.timerActivityModel.getTimeToSet()));
            this.textViewTimeLeft.setText("Time");
        }
        final String nameP1 = this.timerActivityModel.getGame().getNamePlayer1();
        if (!"".equals(nameP1))
        {
            this.playerOne.setText(nameP1);
            this.playerOneList.setText(nameP1);
        }
        final String nameP2 = this.timerActivityModel.getGame().getNamePlayer2();
        if (!"".equals(nameP2))
        {
            this.playerTwo.setText(nameP2);
            this.playerTwoList.setText(nameP2);
        }
        this.firstPlayer1.setText(this.timerActivityModel.getGame().getNamePlayer1());
        this.firstPlayer2.setText(this.timerActivityModel.getGame().getNamePlayer2());
        //Dice
        if (Boolean.FALSE.equals(this.timerActivityModel.getSetting().getDiceCounter()))
        {
            this.linearDiceLayout.setVisibility(View.GONE);
        }
        //List
        if (this.timerActivityModel.getSetting().getListPlayer1().isBlank() && this.timerActivityModel.getSetting().getListPlayer2().isBlank())
        {
            this.linearListLayout.setVisibility(View.GONE);
        } else if (this.timerActivityModel.getSetting().getListPlayer1().isBlank())
        {
            this.linearListLayout1.setVisibility(View.GONE);
        } else if (this.timerActivityModel.getSetting().getListPlayer2().isBlank())
        {
            this.linearListLayout2.setVisibility(View.GONE);
        }
        //Init data
        this.initMission();
        //Init des adapters
        this.shipAdapter1 = (new ShipListAdapter(this, this.timerActivityModel.getGame().getNamePlayer1(), this.timerActivityModel.getGame().getXwsShipsPlayer1()));
        this.shipAdapter2 = (new ShipListAdapter(this, this.timerActivityModel.getGame().getNamePlayer2(), this.timerActivityModel.getGame().getXwsShipsPlayer2()));

        this.listShipPlayer1.setAdapter(this.shipAdapter1);
        this.listShipPlayer2.setAdapter(this.shipAdapter2);

        UIUtils.setListViewHeightBasedOnItems(listShipPlayer1);
        UIUtils.setListViewHeightBasedOnItems(listShipPlayer2);

        //whenever the data changes
        this.shipAdapter1.notifyDataSetChanged();
        this.shipAdapter2.notifyDataSetChanged();
    }

    @Override
    protected void initListeners()
    {
        this.btnStartStop.setOnClickListener(t -> {
            if (this.timerActivityModel.isTimerStart())
            {
                this.stopTimer();
            } else
            {
                this.startTimer();
            }
        });
        //Init des listeners lié au Player 1
        this.playerOneListeners();
        //Init des listeners lié au Player 2
        this.playerTwoListeners();

        this.checkBoxListeners();
        //Init les listeners pour la gestion des rounds
        this.roundListeners();
        //Init les listeners pour la gestion des dices
        this.diceListeners();

        this.textViewMission.setOnClickListener(t -> {
            if (Mission.NO_MISSION.equals(Mission.parseLibelle(this.timerActivityModel.getGame().getMission())))
            {
                Toast.makeText(this, "No Mission for this game !!", Toast.LENGTH_SHORT).show();
            } else
            {
                final Intent intent = new Intent(this, MissionDetailActivity.class);
                intent.putExtra("mission", Mission.parseLibelle(this.timerActivityModel.getGame().getMission()).getCode());
                this.startActivity(intent);
            }
        });
    }


    private void initMission()
    {
        if (this.timerActivityModel.getGame().getMission() != null && !Mission.NO_MISSION.equals(Mission.parseLibelle(this.timerActivityModel.getGame().getMission())))
        {
            this.textViewMission.setText(this.timerActivityModel.getGame().getMission());
        }
    }

    private void initRing()
    {
        final Uri alarmTone = this.timerActivityModel.getSetting().getPathRingTone() != null ?
                Uri.parse(this.timerActivityModel.getSetting().getPathRingTone()) :
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        this.ringtoneAlarm = (RingtoneManager.getRingtone(this.getApplicationContext(), alarmTone));
        this.ringtoneAlarm.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setLegacyStreamType(AudioManager.STREAM_ALARM)
                .build());
    }

    /**
     * Update the timer with the time left
     */
    private void updateTimer()
    {
        final StringBuilder timeLeft = this.generateTimeLeft((int) this.timerActivityModel.getTimeToSet());

        if (!this.timerActivityModel.getGame().isHideTimeLeft() && !this.timerActivityModel.getGame().isHideTimer())
        {
            this.timeClock.setText(timeLeft.toString());
        }
    }

    private StringBuilder generateTimeLeft(final int timeToSet)
    {
        final int minutes = timeToSet / MINUTES;
        final int secondes = timeToSet % MINUTES / SECONDES;

        final StringBuilder timeLeft = new StringBuilder()
                .append(minutes)
                .append(":");
        if (secondes < 10)
        {
            timeLeft.append("0");
        }
        timeLeft.append(secondes);
        return timeLeft;
    }

    private void checkBoxListeners()
    {
        this.firstPlayer1.setOnClickListener(t -> {
            if (this.timerActivityModel.getFirstPlayerChoice() != 1)
            {
                this.timerActivityModel.setFirstPlayerChoice(1);
                this.firstPlayerName.setText(timerActivityModel.getGame().getNamePlayer1());
                this.firstPlayer1.setBackgroundColor(Color.parseColor(RED));
                this.firstPlayer2.setBackgroundColor(Color.parseColor(GREEN));
            }
        });
        this.firstPlayer2.setOnClickListener(t -> {
            if (this.timerActivityModel.getFirstPlayerChoice() != 2)
            {
                this.timerActivityModel.setFirstPlayerChoice(2);
                this.firstPlayerName.setText(timerActivityModel.getGame().getNamePlayer2());
                this.firstPlayer1.setBackgroundColor(Color.parseColor(GREEN));
                this.firstPlayer2.setBackgroundColor(Color.parseColor(RED));
            }
        });
    }

    public void updateScorePlayers()
    {
        this.updateScorePlayerOne();
        this.updateScorePlayerTwo();
    }

    private void playerOneListeners()
    {
        this.playerOneListenersKill();

        this.playerOneListenersMission();
    }

    private void playerOneListenersKill()
    {
        this.btnLessPlayerOneKill.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().lessScoreKill(1);
            this.timerActivityModel.getGame().lessScoreKillPlayer1(1);
            this.updateScorePlayerOne();
        });

        this.btnPlusPlayerOneKill.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().addScoreKill(1);
            this.timerActivityModel.getGame().addScoreKillPlayer1(1);
            this.updateScorePlayerOne();
        });
    }

    private void playerOneListenersMission()
    {
        this.btnLessPlayerOneMission.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().lessScoreMission(1);
            this.timerActivityModel.getGame().lessScoreMissionPlayer1(1);
            this.updateScorePlayerOne();
        });

        this.btnPlusPlayerOneMission.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur1().addScoreMission(1);
            this.timerActivityModel.getGame().addScoreMissionPlayer1(1);
            this.updateScorePlayerOne();
        });
    }

    private void updateScorePlayerOne()
    {
        this.textViewScorePlayerOneKill.setText(String.valueOf(this.timerActivityModel.getGame().getScoreKillPlayer1()));
        this.textViewScorePlayerOneMission.setText(String.valueOf(this.timerActivityModel.getGame().getScoreMissionPlayer1()));
        this.textViewScorePlayerOne.setText(String.valueOf(this.timerActivityModel.getGame().getScoreGlobalPlayer1()));
    }

    private void playerTwoListeners()
    {
        this.playerTwoListenersKill();

        this.playerTwoListenersMission();
    }

    private void playerTwoListenersKill()
    {
        this.btnLessPlayerTwoKill.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().lessScoreKill(1);
            this.timerActivityModel.getGame().lessScoreKillPlayer2(1);
            this.updateScorePlayerTwo();
        });

        this.btnPlusPlayerTwoKill.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().addScoreKill(1);
            this.timerActivityModel.getGame().addScoreKillPlayer2(1);
            this.updateScorePlayerTwo();
        });
    }

    private void playerTwoListenersMission()
    {
        this.btnLessPlayerTwoMission.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().lessScoreMission(1);
            this.timerActivityModel.getGame().lessScoreMissionPlayer2(1);
            this.updateScorePlayerTwo();
        });

        this.btnPlusPlayerTwoMission.setOnClickListener(t -> {
            this.timerActivityModel.getScoreRoundJoueur2().addScoreMission(1);
            this.timerActivityModel.getGame().addScoreMissionPlayer2(1);
            this.updateScorePlayerTwo();
        });
    }

    private void updateScorePlayerTwo()
    {
        this.textViewScorePlayerTwoKill.setText(String.valueOf(this.timerActivityModel.getGame().getScoreKillPlayer2()));
        this.textViewScorePlayerTwoMission.setText(String.valueOf(this.timerActivityModel.getGame().getScoreMissionPlayer2()));
        this.textViewScorePlayerTwo.setText(String.valueOf(this.timerActivityModel.getGame().getScoreGlobalPlayer2()));
    }

    private void roundListeners()
    {
        this.btnLessRound.setOnClickListener(t -> removeRound());
        this.btnPlusRound.setOnClickListener(t -> this.addRound());
    }

    private void updateDiceStat()
    {
        final DiceTurn current = this.timerActivityModel.getDiceTurn();
        final List<DiceTurn> diceTurns = this.timerActivityModel.getGame().getDiceTurns();

        final DiceCounter diceCounter = new DiceCounter();

        for (DiceTurn diceTurn : diceTurns)
        {
            diceCounter.updateCount(diceTurn);
        }
        diceCounter.updateCount(current);

        this.totalRedPlayerOne.setText(String.valueOf(diceCounter.getTotalAttackPlayer1()));
        this.totalRedPlayerTwo.setText(String.valueOf(diceCounter.getTotalAttackPlayer2()));
        this.totalGreenPlayerOne.setText(String.valueOf(diceCounter.getTotalDefensePlayer1()));
        this.totalGreenPlayerTwo.setText(String.valueOf(diceCounter.getTotalDefensePlayer2()));
        //PLAYER1
        this.scorePlayerOneAttackDicesCrit.setText(diceCounter.getAttackCritPlayer1() + "(" + (diceCounter.getTotalAttackPlayer1() * DiceFace.ATTACK_CRIT.getProba()) + ")");
        this.scorePlayerOneAttackDicesHit.setText(diceCounter.getAttackHitPlayer1() + "(" + (diceCounter.getTotalAttackPlayer1() * DiceFace.ATTACK_HIT.getProba()) + ")");
        this.scorePlayerOneAttackDicesEye.setText(diceCounter.getAttackEyePlayer1() + "(" + (diceCounter.getTotalAttackPlayer1() * DiceFace.ATTACK_EYE.getProba()) + ")");
        this.scorePlayerOneAttackDicesBlank.setText(diceCounter.getAttackBlankPlayer1() + "(" + (diceCounter.getTotalAttackPlayer1() * DiceFace.ATTACK_BLANK.getProba()) + ")");
        this.scorePlayerOneDefenseDicesEvade.setText(diceCounter.getDefenseEvadePlayer1() + "(" + (diceCounter.getTotalDefensePlayer1() * DiceFace.DEFENSE_EVADE.getProba()) + ")");
        this.scorePlayerOneDefenseDicesEye.setText(diceCounter.getDefenseEyePlayer1() + "(" + (diceCounter.getTotalDefensePlayer1() * DiceFace.DEFENSE_EYE.getProba()) + ")");
        this.scorePlayerOneDefenseDicesBlank.setText(diceCounter.getDefenseBlankPlayer1() + "(" + (diceCounter.getTotalDefensePlayer1() * DiceFace.DEFENSE_BLANK.getProba()) + ")");
        //PLAYER2
        this.scorePlayerTwoAttackDicesCrit.setText(diceCounter.getAttackCritPlayer2() + "(" + (diceCounter.getTotalAttackPlayer2() * DiceFace.ATTACK_CRIT.getProba()) + ")");
        this.scorePlayerTwoAttackDicesHit.setText(diceCounter.getAttackHitPlayer2() + "(" + (diceCounter.getTotalAttackPlayer2() * DiceFace.ATTACK_HIT.getProba()) + ")");
        this.scorePlayerTwoAttackDicesEye.setText(diceCounter.getAttackEyePlayer2() + "(" + (diceCounter.getTotalAttackPlayer2() * DiceFace.ATTACK_EYE.getProba()) + ")");
        this.scorePlayerTwoAttackDicesBlank.setText(diceCounter.getAttackBlankPlayer2() + "(" + (diceCounter.getTotalAttackPlayer2() * DiceFace.ATTACK_BLANK.getProba()) + ")");
        this.scorePlayerTwoDefenseDicesEvade.setText(diceCounter.getDefenseEvadePlayer2() + "(" + (diceCounter.getTotalDefensePlayer2() * DiceFace.DEFENSE_EVADE.getProba()) + ")");
        this.scorePlayerTwoDefenseDicesEye.setText(diceCounter.getDefenseEyePlayer2() + "(" + (diceCounter.getTotalDefensePlayer2() * DiceFace.DEFENSE_EYE.getProba()) + ")");
        this.scorePlayerTwoDefenseDicesBlank.setText(diceCounter.getDefenseBlankPlayer2() + "(" + (diceCounter.getTotalDefensePlayer2() * DiceFace.DEFENSE_BLANK.getProba()) + ")");
    }

    private void diceListeners()
    {
        this.btnLessPlayerOneAttackDicesCrit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_CRIT, Player.ONE);
            this.updateDiceStat();
        });
        this.btnPlusPlayerOneAttackDicesCrit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_CRIT, Player.ONE);
            this.updateDiceStat();
        });
        this.btnLessPlayerOneAttackDicesHit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_HIT, Player.ONE);
            this.updateDiceStat();
        });
        this.btnPlusPlayerOneAttackDicesHit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_HIT, Player.ONE);
            this.updateDiceStat();
        });
        this.btnLessPlayerOneAttackDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_EYE, Player.ONE);
            this.updateDiceStat();
        });
        this.btnPlusPlayerOneAttackDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_EYE, Player.ONE);
            this.updateDiceStat();
        });
        this.btnLessPlayerOneAttackDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_BLANK, Player.ONE);
            this.updateDiceStat();
        });
        this.btnPlusPlayerOneAttackDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_BLANK, Player.ONE);
            this.updateDiceStat();
        });
        //DEFENSE
        this.btnLessPlayerOneDefenseDicesEvade.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.DEFENSE_EVADE, Player.ONE);
            this.updateDiceStat();
        });
        this.btnPlusPlayerOneDefenseDicesEvade.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.DEFENSE_EVADE, Player.ONE);
            this.updateDiceStat();
        });
        this.btnLessPlayerOneDefenseDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.DEFENSE_EYE, Player.ONE);
            this.updateDiceStat();
        });
        this.btnPlusPlayerOneDefenseDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.DEFENSE_EYE, Player.ONE);
            this.updateDiceStat();
        });
        this.btnLessPlayerOneDefenseDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.DEFENSE_BLANK, Player.ONE);
            this.updateDiceStat();
        });
        this.btnPlusPlayerOneDefenseDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.DEFENSE_BLANK, Player.ONE);
            this.updateDiceStat();
        });

        //PLAYER 2
        this.btnLessPlayerTwoAttackDicesCrit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_CRIT, Player.TWO);
            this.updateDiceStat();
        });
        this.btnPlusPlayerTwoAttackDicesCrit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_CRIT, Player.TWO);
            this.updateDiceStat();
        });
        this.btnLessPlayerTwoAttackDicesHit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_HIT, Player.TWO);
            this.updateDiceStat();
        });
        this.btnPlusPlayerTwoAttackDicesHit.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_HIT, Player.TWO);
            this.updateDiceStat();
        });
        this.btnLessPlayerTwoAttackDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_EYE, Player.TWO);
            this.updateDiceStat();
        });
        this.btnPlusPlayerTwoAttackDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_EYE, Player.TWO);
            this.updateDiceStat();
        });
        this.btnLessPlayerTwoAttackDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.ATTACK_BLANK, Player.TWO);
            this.updateDiceStat();
        });
        this.btnPlusPlayerTwoAttackDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.ATTACK_BLANK, Player.TWO);
            this.updateDiceStat();
        });
        //DEFENSE
        this.btnLessPlayerTwoDefenseDicesEvade.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.DEFENSE_EVADE, Player.TWO);
            this.updateDiceStat();
        });
        this.btnPlusPlayerTwoDefenseDicesEvade.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.DEFENSE_EVADE, Player.TWO);
            this.updateDiceStat();
        });
        this.btnLessPlayerTwoDefenseDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.DEFENSE_EYE, Player.TWO);
            this.updateDiceStat();
        });
        this.btnPlusPlayerTwoDefenseDicesEye.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.DEFENSE_EYE, Player.TWO);
            this.updateDiceStat();
        });
        this.btnLessPlayerTwoDefenseDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().downDice(DiceFace.DEFENSE_BLANK, Player.TWO);
            this.updateDiceStat();
        });
        this.btnPlusPlayerTwoDefenseDicesBlank.setOnClickListener(t -> {
            this.timerActivityModel.getDiceTurn().upDice(DiceFace.DEFENSE_BLANK, Player.TWO);
            this.updateDiceStat();
        });
    }

    private void removeRound()
    {
        if (this.timerActivityModel.getGame().getRound() > 0)
        {
            this.timerActivityModel.getGame().removeRound();
            this.roundNumber.setText(String.valueOf(this.timerActivityModel.getGame().getRound()));
        }
    }

    private void addRound()
    {
        this.updateRoundDetail();
        this.timerActivityModel.getGame().addRound();
        if (this.timerActivityModel.getGame().getRound() == 12)
        {
            Toast.makeText(TimerActivity.this, "LAST TURN !!", Toast.LENGTH_LONG).show();
        }
        this.roundNumber.setText(String.valueOf(this.timerActivityModel.getGame().getRound()));
    }

    private void updateRoundDetail()
    {
        final Round round = new Round();
        round.setRoundNumber(this.roundNumber.getText().toString());
        round.setFirstPlayer(this.firstPlayerName.getText().toString());
        round.setScorePlayer1(this.timerActivityModel.getScoreRoundJoueur1());
        round.setScorePlayer2(this.timerActivityModel.getScoreRoundJoueur2());
        round.setTime(this.timerActivityModel.getTimeStartRound() - this.timerActivityModel.getTimeToSet());
        this.timerActivityModel.getGame().getRounds().add(round);
        //Reset des scores du round post save
        this.timerActivityModel.setTimeStartRound(this.timerActivityModel.getTimeToSet());
        this.timerActivityModel.setScoreRoundJoueur1(new Score());
        this.timerActivityModel.setScoreRoundJoueur2(new Score());
        //Sauvegarde des dices stats
        this.timerActivityModel.getGame().getDiceTurns().add(this.timerActivityModel.getDiceTurn());
        this.timerActivityModel.setDiceTurn(new DiceTurn());
        this.updateDiceStat();
    }

    /**
     * the trigger for start the timer
     */
    private void startTimer()
    {
        this.timer = (new CountDownTimer(this.timerActivityModel.getTimeToSet(), SECONDES)
        {
            /**
             * trigger every {@fields SECONDES} millisecondes
             * {@inheritDoc}
             */
            @Override
            public void onTick(final long time)
            {
                TimerActivity.this.timerActivityModel.setTimeToSet(time);
                TimerActivity.this.updateTimer();
            }

            /**
             * at the end of the time
             * {@inheritDoc}
             */
            @Override
            public void onFinish()
            {
                Toast.makeText(TimerActivity.this, "TIME OVER !!", Toast.LENGTH_LONG).show();
                TimerActivity.this.playSound();
            }
        }.start());
        this.btnStartStop.setText(this.getString(R.string.stop));
        this.btnStartStop.setBackgroundColor(Color.parseColor(RED));
        this.timerActivityModel.setTimerStart(true);
        if (this.timerActivityModel.getGame().getRound() == 0)
        {
            this.addRound();
        }
    }

    /**
     * the trigger for stop the timer
     */
    private void stopTimer()
    {
        if (timer != null)
        {
            this.timer.cancel();
        }
        this.btnStartStop.setText(this.getString(R.string.start));
        this.btnStartStop.setBackgroundColor(Color.parseColor(GREEN));
        if (timerActivityModel.isTimerStart())
        {
            this.timerActivityModel.setTimerStart(false);
            if (this.ringtoneAlarm.isPlaying())
            {
                this.ringtoneAlarm.stop();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        if (!this.timerActivityModel.isEnd())
        {
            final EndDialogTimer endDialogTimer = new EndDialogTimer(this);
            endDialogTimer.show(this.getSupportFragmentManager(), "dialogHelp");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.updateRoundDetail();
        this.timerActivityModel.getGame().setTimeLeft(this.timerActivityModel.getTimeToSet());
        this.timerActivityModel.getGame().setDate(this.timerActivityModel.getDateFormat().format(new Date()));

        GameService.getInstance().save(this.getBaseContext(), this.timerActivityModel.getGame());

        if (timer != null)
        {
            this.timer.cancel();
        }
        if (this.ringtoneAlarm.isPlaying())
        {
            this.ringtoneAlarm.stop();
        }

        super.onDestroy();
    }

    private void playSound()
    {
        if (this.timerActivityModel.isAlreadyEnd())
        {
            return;
        }
        this.timerActivityModel.setAlreadyEnd(true);
        this.ringtoneAlarm.play();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putSerializable("timerActivityModel", this.timerActivityModel);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        this.timerActivityModel = Try.of(() -> savedInstanceState.getSerializable("timerActivityModel"))
                .map(TimerActivityModel.class::cast)
                .onFailure(throwable -> {
                    Log.e("onRestoreInstanceState", "An error occurred while reloading the game.", throwable);
                    Toast.makeText(this, "An error occurred while reloading the game.", Toast.LENGTH_LONG).show();
                })
                .getOrElse(new TimerActivityModel());

        if (this.timerActivityModel.isTimerStart())
        {
            this.startTimer();
        } else
        {
            this.stopTimer();
        }

        //Init des datas de la page
        this.initDatas();
        //Initialization of the listeners
        this.initListeners();
    }

    public Game getGame()
    {
        return timerActivityModel.getGame();
    }

    public void setEnd(boolean end)
    {
        this.timerActivityModel.setEnd(end);
    }

    public void changeScore(String namePlayer, Ship current, Ship.Statut oldStatut)
    {
        if (oldStatut.equals(Ship.Statut.FULL))
        {
            timerActivityModel.getGame().addScoreKill(namePlayer, current.getPoints() / 2);
        } else if (oldStatut.equals(Ship.Statut.HALF))
        {
            timerActivityModel.getGame().lessScoreKill(namePlayer, current.getPoints() / 2);
            timerActivityModel.getGame().addScoreKill(namePlayer, current.getPoints());
        } else if (oldStatut.equals(Ship.Statut.DEAD))
        {
            timerActivityModel.getGame().lessScoreKill(namePlayer, current.getPoints());
        }
        this.updateScorePlayers();
        this.timerActivityModel.updateScoreRound();
    }

    @Override
    protected void findView()
    {
        this.timeClock = (this.findViewById(R.id.timeClock));
        this.btnStartStop = (this.findViewById(R.id.btnStart));
        this.roundNumber = (this.findViewById(R.id.roundNumber));
        this.btnLessRound = (this.findViewById(R.id.btnLessRound));
        this.btnPlusRound = (this.findViewById(R.id.btnPlusRound));
        this.textViewTimeLeft = (this.findViewById(R.id.textViewTimeLeft));
        this.textViewMission = (this.findViewById(R.id.textViewMission));
        //GLOBAL
        this.playerOne = (this.findViewById(R.id.playerOne));
        this.playerTwo = (this.findViewById(R.id.playerTwo));
        this.playerOneList = (this.findViewById(R.id.playerOneList));
        this.playerTwoList = (this.findViewById(R.id.playerTwoList));
        this.textViewScorePlayerOne = (this.findViewById(R.id.scorePlayerOne));
        this.textViewScorePlayerTwo = (this.findViewById(R.id.scorePlayerTwo));
        //Kill
        this.textViewScorePlayerOneKill = (this.findViewById(R.id.scorePlayerOneKill));
        this.textViewScorePlayerTwoKill = (this.findViewById(R.id.scorePlayerTwoKill));
        this.btnLessPlayerOneKill = (this.findViewById(R.id.btnLessPlayerOneKill));
        this.btnPlusPlayerOneKill = (this.findViewById(R.id.btnPlusPlayerOneKill));
        this.btnLessPlayerTwoKill = (this.findViewById(R.id.btnLessPlayerTwoKill));
        this.btnPlusPlayerTwoKill = (this.findViewById(R.id.btnPlusPlayerTwoKill));
        //Mission
        this.textViewScorePlayerOneMission = (this.findViewById(R.id.scorePlayerOneMission));
        this.textViewScorePlayerTwoMission = (this.findViewById(R.id.scorePlayerTwoMission));
        this.btnLessPlayerOneMission = (this.findViewById(R.id.btnLessPlayerOneMission));
        this.btnPlusPlayerOneMission = (this.findViewById(R.id.btnPlusPlayerOneMission));
        this.btnLessPlayerTwoMission = (this.findViewById(R.id.btnLessPlayerTwoMission));
        this.btnPlusPlayerTwoMission = (this.findViewById(R.id.btnPlusPlayerTwoMission));
        //Dice
        this.linearDiceLayout = (this.findViewById(R.id.linearDiceLayout));
        this.totalRedPlayerOne = (this.findViewById(R.id.totalRedPlayerOne));
        this.totalRedPlayerTwo = (this.findViewById(R.id.totalRedPlayerTwo));
        this.totalGreenPlayerOne = (this.findViewById(R.id.totalGreenPlayerOne));
        this.totalGreenPlayerTwo = (this.findViewById(R.id.totalGreenPlayerTwo));
        this.scorePlayerOneAttackDicesCrit = (this.findViewById(R.id.scorePlayerOneAttackDicesCrit));
        this.scorePlayerTwoAttackDicesCrit = (this.findViewById(R.id.scorePlayerTwoAttackDicesCrit));
        this.scorePlayerOneAttackDicesHit = (this.findViewById(R.id.scorePlayerOneAttackDicesHit));
        this.scorePlayerTwoAttackDicesHit = (this.findViewById(R.id.scorePlayerTwoAttackDicesHit));
        this.scorePlayerOneAttackDicesEye = (this.findViewById(R.id.scorePlayerOneAttackDicesEye));
        this.scorePlayerTwoAttackDicesEye = (this.findViewById(R.id.scorePlayerTwoAttackDicesEye));
        this.scorePlayerOneAttackDicesBlank = (this.findViewById(R.id.scorePlayerOneAttackDicesBlank));
        this.scorePlayerTwoAttackDicesBlank = (this.findViewById(R.id.scorePlayerTwoAttackDicesBlank));
        this.scorePlayerOneDefenseDicesEvade = (this.findViewById(R.id.scorePlayerOneDefenseDicesEvade));
        this.scorePlayerTwoDefenseDicesEvade = (this.findViewById(R.id.scorePlayerTwoDefenseDicesEvade));
        this.scorePlayerOneDefenseDicesEye = (this.findViewById(R.id.scorePlayerOneDefenseDicesEye));
        this.scorePlayerTwoDefenseDicesEye = (this.findViewById(R.id.scorePlayerTwoDefenseDicesEye));
        this.scorePlayerOneDefenseDicesBlank = (this.findViewById(R.id.scorePlayerOneDefenseDicesBlank));
        this.scorePlayerTwoDefenseDicesBlank = (this.findViewById(R.id.scorePlayerTwoDefenseDicesBlank));
        this.btnLessPlayerOneAttackDicesCrit = (this.findViewById(R.id.btnLessPlayerOneAttackDicesCrit));
        this.btnPlusPlayerOneAttackDicesCrit = (this.findViewById(R.id.btnPlusPlayerOneAttackDicesCrit));
        this.btnLessPlayerTwoAttackDicesCrit = (this.findViewById(R.id.btnLessPlayerTwoAttackDicesCrit));
        this.btnPlusPlayerTwoAttackDicesCrit = (this.findViewById(R.id.btnPlusPlayerTwoAttackDicesCrit));
        this.btnLessPlayerOneAttackDicesHit = (this.findViewById(R.id.btnLessPlayerOneAttackDicesHit));
        this.btnPlusPlayerOneAttackDicesHit = (this.findViewById(R.id.btnPlusPlayerOneAttackDicesHit));
        this.btnLessPlayerTwoAttackDicesHit = (this.findViewById(R.id.btnLessPlayerTwoAttackDicesHit));
        this.btnPlusPlayerTwoAttackDicesHit = (this.findViewById(R.id.btnPlusPlayerTwoAttackDicesHit));
        this.btnLessPlayerOneAttackDicesEye = (this.findViewById(R.id.btnLessPlayerOneAttackDicesEye));
        this.btnPlusPlayerOneAttackDicesEye = (this.findViewById(R.id.btnPlusPlayerOneAttackDicesEye));
        this.btnLessPlayerTwoAttackDicesEye = (this.findViewById(R.id.btnLessPlayerTwoAttackDicesEye));
        this.btnPlusPlayerTwoAttackDicesEye = (this.findViewById(R.id.btnPlusPlayerTwoAttackDicesEye));
        this.btnLessPlayerOneAttackDicesBlank = (this.findViewById(R.id.btnLessPlayerOneAttackDicesBlank));
        this.btnPlusPlayerOneAttackDicesBlank = (this.findViewById(R.id.btnPlusPlayerOneAttackDicesBlank));
        this.btnLessPlayerTwoAttackDicesBlank = (this.findViewById(R.id.btnLessPlayerTwoAttackDicesBlank));
        this.btnPlusPlayerTwoAttackDicesBlank = (this.findViewById(R.id.btnPlusPlayerTwoAttackDicesBlank));
        this.btnLessPlayerOneDefenseDicesEvade = (this.findViewById(R.id.btnLessPlayerOneDefenseDicesEvade));
        this.btnPlusPlayerOneDefenseDicesEvade = (this.findViewById(R.id.btnPlusPlayerOneDefenseDicesEvade));
        this.btnLessPlayerTwoDefenseDicesEvade = (this.findViewById(R.id.btnLessPlayerTwoDefenseDicesEvade));
        this.btnPlusPlayerTwoDefenseDicesEvade = (this.findViewById(R.id.btnPlusPlayerTwoDefenseDicesEvade));
        this.btnLessPlayerOneDefenseDicesEye = (this.findViewById(R.id.btnLessPlayerOneDefenseDicesEye));
        this.btnPlusPlayerOneDefenseDicesEye = (this.findViewById(R.id.btnPlusPlayerOneDefenseDicesEye));
        this.btnLessPlayerTwoDefenseDicesEye = (this.findViewById(R.id.btnLessPlayerTwoDefenseDicesEye));
        this.btnPlusPlayerTwoDefenseDicesEye = (this.findViewById(R.id.btnPlusPlayerTwoDefenseDicesEye));
        this.btnLessPlayerOneDefenseDicesBlank = (this.findViewById(R.id.btnLessPlayerOneDefenseDicesBlank));
        this.btnPlusPlayerOneDefenseDicesBlank = (this.findViewById(R.id.btnPlusPlayerOneDefenseDicesBlank));
        this.btnLessPlayerTwoDefenseDicesBlank = (this.findViewById(R.id.btnLessPlayerTwoDefenseDicesBlank));
        this.btnPlusPlayerTwoDefenseDicesBlank = (this.findViewById(R.id.btnPlusPlayerTwoDefenseDicesBlank));

        this.firstPlayer1 = (this.findViewById(R.id.firstPlayer1));
        this.firstPlayer2 = (this.findViewById(R.id.firstPlayer2));
        this.firstPlayerName = (this.findViewById(R.id.firstPlayerName));

        this.linearListLayout = (this.findViewById(R.id.linearListLayout));
        this.linearListLayout1 = (this.findViewById(R.id.linearListLayout1));
        this.linearListLayout2 = (this.findViewById(R.id.linearListLayout2));
        this.listShipPlayer1 = (this.findViewById(R.id.listShipPlayer1));
        this.listShipPlayer2 = (this.findViewById(R.id.listShipPlayer2));
    }

}
