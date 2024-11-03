package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.DiceCounter;
import fr.corentin.roux.x_wing_score_tracker.model.DiceFace;
import fr.corentin.roux.x_wing_score_tracker.model.DiceTurn;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.model.Round;
import fr.corentin.roux.x_wing_score_tracker.model.Score;
import io.vavr.control.Try;

public class HistoriqueDetailActivity extends AbstractActivity
{

    private static final int MINUTES = 60000;
    private static final int SECONDES = 1000;
    private TextView finalDate;
    private TextView finalRound;
    private TextView finalTime;
    private TextView finalScore;
    private TextView details;
    private TextView mission;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initContentView()
    {
        this.setContentView(R.layout.historique_detail_layout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void findView()
    {
        this.finalDate = this.findViewById(R.id.finalDate);
        this.finalRound = this.findViewById(R.id.finalRound);
        this.finalTime = this.findViewById(R.id.finalTime);
        this.finalScore = this.findViewById(R.id.finalScore);
        this.details = this.findViewById(R.id.details);
        this.mission = this.findViewById(R.id.mission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDatas()
    {
        Game current = (Game) this.getIntent().getSerializableExtra("game");
        final StringBuilder time = generateTimerAffichage(current.getTimeLeft());
        final StringBuilder finalScore = new StringBuilder();

        finalScore.append(current.getNamePlayer1())
                .append(" ")
                .append(current.getScoreGlobalPlayer1())
                .append(" - ")
                .append(current.getScoreGlobalPlayer2())
                .append(" ")
                .append(current.getNamePlayer2());

        final StringBuilder historique = new StringBuilder();

        int scoreTempJ1 = 0;
        int scoreTempJ2 = 0;

        final List<Round> rounds = Try.of(current::getRoundsJson)
                .mapTry(this::extractRounds)
                .onFailure(throwable -> Toast.makeText(this, "Error while loading rounds details.", Toast.LENGTH_SHORT).show())
                .getOrElse(Collections.emptyList());

        final List<DiceTurn> diceTurns = Try.of(current::getDiceJson)
                .mapTry(this::extractDiceStat)
                .onFailure(throwable -> Toast.makeText(this, "Error while loading dice stats details.", Toast.LENGTH_SHORT).show())
                .getOrElse(Collections.emptyList());

        if (!diceTurns.isEmpty())
        {
            //Préparation du resultat des dices global
            DiceCounter result = new DiceCounter();
            diceTurns.stream()
                    .map(DiceTurn::makeTheCount)
                    .forEach(result::append);

            finalScore
                    .append("\n\n")
                    .append("DICE:").append("\n")
                    .append("  ").append(current.getNamePlayer1().toUpperCase()).append("\n")
                    .append("    - ").append("Total Attack : ").append(result.getTotalAttackPlayer1()).append("\n")
                    .append("    - ").append(DiceFace.ATTACK_CRIT.getPrintLibelle()).append(" : ").append(result.getAttackCritPlayer1()).append(" (").append((result.getTotalAttackPlayer1() * DiceFace.ATTACK_CRIT.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.ATTACK_HIT.getPrintLibelle()).append(" : ").append(result.getAttackHitPlayer1()).append(" (").append((result.getTotalAttackPlayer1() * DiceFace.ATTACK_HIT.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.ATTACK_EYE.getPrintLibelle()).append(" : ").append(result.getAttackEyePlayer1()).append(" (").append((result.getTotalAttackPlayer1() * DiceFace.ATTACK_EYE.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.ATTACK_BLANK.getPrintLibelle()).append(" : ").append(result.getAttackBlankPlayer1()).append(" (").append((result.getTotalAttackPlayer1() * DiceFace.ATTACK_BLANK.getProba())).append(")").append("\n")
                    .append("    - ").append("Total Defense : ").append(result.getTotalDefensePlayer1()).append("\n")
                    .append("    - ").append(DiceFace.DEFENSE_EVADE.getPrintLibelle()).append(" : ").append(result.getDefenseEvadePlayer1()).append(" (").append((result.getTotalDefensePlayer1() * DiceFace.DEFENSE_EVADE.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.DEFENSE_EYE.getPrintLibelle()).append(" : ").append(result.getDefenseEyePlayer1()).append(" (").append((result.getTotalDefensePlayer1() * DiceFace.DEFENSE_EYE.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.DEFENSE_BLANK.getPrintLibelle()).append(" : ").append(result.getDefenseBlankPlayer1()).append(" (").append((result.getTotalDefensePlayer1() * DiceFace.DEFENSE_BLANK.getProba())).append(")").append("\n")
                    .append("  ").append(current.getNamePlayer2().toUpperCase()).append("\n")
                    .append("    - ").append("Total Attack : ").append(result.getTotalAttackPlayer2()).append("\n")
                    .append("    - ").append(DiceFace.ATTACK_CRIT.getPrintLibelle()).append(" : ").append(result.getAttackCritPlayer2()).append(" (").append((result.getTotalAttackPlayer2() * DiceFace.ATTACK_CRIT.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.ATTACK_HIT.getPrintLibelle()).append(" : ").append(result.getAttackHitPlayer2()).append(" (").append((result.getTotalAttackPlayer2() * DiceFace.ATTACK_HIT.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.ATTACK_EYE.getPrintLibelle()).append(" : ").append(result.getAttackEyePlayer2()).append(" (").append((result.getTotalAttackPlayer2() * DiceFace.ATTACK_EYE.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.ATTACK_BLANK.getPrintLibelle()).append(" : ").append(result.getAttackBlankPlayer2()).append(" (").append((result.getTotalAttackPlayer2() * DiceFace.ATTACK_BLANK.getProba())).append(")").append("\n")
                    .append("    - ").append("Total Defense : ").append(result.getTotalDefensePlayer2()).append("\n")
                    .append("    - ").append(DiceFace.DEFENSE_EVADE.getPrintLibelle()).append(" : ").append(result.getDefenseEvadePlayer2()).append(" (").append((result.getTotalDefensePlayer2() * DiceFace.DEFENSE_EVADE.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.DEFENSE_EYE.getPrintLibelle()).append(" : ").append(result.getDefenseEyePlayer2()).append(" (").append((result.getTotalDefensePlayer2() * DiceFace.DEFENSE_EYE.getProba())).append(")").append("\n")
                    .append("    - ").append(DiceFace.DEFENSE_BLANK.getPrintLibelle()).append(" : ").append(result.getDefenseBlankPlayer2()).append(" (").append((result.getTotalDefensePlayer2() * DiceFace.DEFENSE_BLANK.getProba())).append(")").append("\n");
        }

        int cpt = 0;
        for (final Round round : rounds)
        {
            scoreTempJ1 += round.getScorePlayer1().getScoreGlobal();
            scoreTempJ2 += round.getScorePlayer2().getScoreGlobal();
            int finalCpt = cpt;
            final DiceTurn currentDiceTurn = Try.of(() -> diceTurns.get(finalCpt))
                    .getOrNull();
            cpt += 1;

            historique
                    .append("----------------------------------------------------\n")
                    .append("ROUND : ").append(round.getRoundNumber()).append("\n")
                    .append("Duration of the round : ").append(generateTimerAffichage(round.getTime())).append("\n")
                    .append("First Player : ").append(round.getFirstPlayer()).append("\n")
                    .append("Score Global : \n")
                    .append("  - ").append(current.getNamePlayer1().toUpperCase()).append(" : ").append(scoreTempJ1).append("\n")
                    .append("  - ").append(current.getNamePlayer2().toUpperCase()).append(" : ").append(scoreTempJ2).append("\n")
                    .append("Scores of the round : \n")
                    .append("  ").append(current.getNamePlayer1().toUpperCase()).append("\n")
                    .append("    - Global ").append(round.getScorePlayer1().getScoreGlobal()).append("\n")
                    .append("      - Kill ").append(round.getScorePlayer1().getScoreKill()).append("\n")
                    .append("      - Mission ").append(round.getScorePlayer1().getScoreMission()).append("\n")
                    .append("  ").append(current.getNamePlayer2().toUpperCase()).append("\n")
                    .append("    - Global ").append(round.getScorePlayer2().getScoreGlobal()).append("\n")
                    .append("      - Kill ").append(round.getScorePlayer2().getScoreKill()).append("\n")
                    .append("      - Mission ").append(round.getScorePlayer2().getScoreMission()).append("\n");

            if (currentDiceTurn != null)
            {
                historique.append("Dice:\n");
                final int totalAttackPlayer1 = currentDiceTurn.getDiceStatsPlayer1().entrySet().stream().filter(t -> t.getKey().isAttack()).mapToInt(Map.Entry::getValue).sum();
                final int totalDefensePlayer1 = currentDiceTurn.getDiceStatsPlayer1().entrySet().stream().filter(t -> t.getKey().isAttack()).mapToInt(Map.Entry::getValue).sum();
                historique.append("  ").append(current.getNamePlayer1().toUpperCase()).append("\n");
                currentDiceTurn.getDiceStatsPlayer1().entrySet().stream()
                        .sorted((a, b) -> a.getKey().getOrdre() > b.getKey().getOrdre() ? 1 : -1)
                        .forEach(diceFaceIntegerEntry -> historique.append("    - ").append(diceFaceIntegerEntry.getKey().getPrintLibelle()).append(" : ").append(diceFaceIntegerEntry.getValue())
                                .append(" (").append(((diceFaceIntegerEntry.getKey().isAttack() ? totalAttackPlayer1 : totalDefensePlayer1) * diceFaceIntegerEntry.getKey().getProba())).append(")")
                                .append("\n"));

                final int totalAttackPlayer2 = currentDiceTurn.getDiceStatsPlayer2().entrySet().stream().filter(t -> t.getKey().isAttack()).mapToInt(Map.Entry::getValue).sum();
                final int totalDefensePlayer2 = currentDiceTurn.getDiceStatsPlayer2().entrySet().stream().filter(t -> t.getKey().isAttack()).mapToInt(Map.Entry::getValue).sum();
                historique.append("  ").append(current.getNamePlayer2().toUpperCase()).append("\n");
                currentDiceTurn.getDiceStatsPlayer2().entrySet().stream()
                        .sorted((a, b) -> a.getKey().getOrdre() > b.getKey().getOrdre() ? 1 : -1)
                        .forEach(diceFaceIntegerEntry -> historique.append("    - ").append(diceFaceIntegerEntry.getKey().getPrintLibelle()).append(" : ").append(diceFaceIntegerEntry.getValue())
                                .append(" (").append(((diceFaceIntegerEntry.getKey().isAttack() ? totalAttackPlayer2 : totalDefensePlayer2) * diceFaceIntegerEntry.getKey().getProba())).append(")")
                                .append("\n"));
            }

        }

        this.finalDate.setText(current.getDate());
        this.finalRound.setText((String.valueOf(current.getRound())));
        this.finalTime.setText(time.toString());
        this.finalScore.setText(finalScore.toString());
        this.details.setText(historique.toString());
        this.mission.setText(current.getMission());
    }

    private StringBuilder generateTimerAffichage(long timeLeft)
    {
        final int minutes = generateMinutes(timeLeft);
        final int secondes = generateSecondes(timeLeft);

        final StringBuilder time = new StringBuilder()
                .append(minutes)
                .append(":");
        if (secondes < 10)
        {
            time.append("0");
        }
        time.append(secondes);
        return time;
    }

    private int generateMinutes(final long time)
    {
        return (int) time / MINUTES;
    }

    private int generateSecondes(final long time)
    {
        return (int) time % MINUTES / SECONDES;
    }

    private List<DiceTurn> extractDiceStat(String dicesStats) throws JSONException
    {
        final List<DiceTurn> diceTurns = new ArrayList<>();
        final JSONArray roundArray = new JSONArray(dicesStats);
        for (int i = 0; i < roundArray.length(); i++)
        {
            final JSONObject diceStats = roundArray.getJSONObject(i);

            // PLAYER 1
            JSONObject diceStatsPlayer1;
            try
            {
                diceStatsPlayer1 = diceStats.getJSONObject("diceStatsPlayer1");//h
            } catch (Exception e)
            {
                diceStatsPlayer1 = diceStats.getJSONObject("b");
            }
            Map<DiceFace, Integer> playerOneDiceStat = extractStat(diceStatsPlayer1);
            // PLAYER 2
            JSONObject diceStatsPlayer2;
            try
            {
                diceStatsPlayer2 = diceStats.getJSONObject("diceStatsPlayer2");//h
            } catch (Exception e)
            {
                diceStatsPlayer2 = diceStats.getJSONObject("c");
            }
            final Map<DiceFace, Integer> playerTwoDiceStat = extractStat(diceStatsPlayer2);

            diceTurns.add(new DiceTurn(playerOneDiceStat, playerTwoDiceStat));
        }

        return diceTurns;
    }

    private Map<DiceFace, Integer> extractStat(JSONObject diceStatsPlayer1) throws JSONException
    {
        int ATTACK_EYE;
        try
        {
            ATTACK_EYE = diceStatsPlayer1.getInt("ATTACK_EYE");//h
        } catch (Exception e)
        {
            ATTACK_EYE = diceStatsPlayer1.getInt("b");
        }
        int ATTACK_BLANK;
        try
        {
            ATTACK_BLANK = diceStatsPlayer1.getInt("ATTACK_BLANK");//h
        } catch (Exception e)
        {
            ATTACK_BLANK = diceStatsPlayer1.getInt("c");
        }
        int ATTACK_CRIT;
        try
        {
            ATTACK_CRIT = diceStatsPlayer1.getInt("ATTACK_CRIT");//h
        } catch (Exception e)
        {
            ATTACK_CRIT = diceStatsPlayer1.getInt("h");
        }
        int ATTACK_HIT;
        try
        {
            ATTACK_HIT = diceStatsPlayer1.getInt("ATTACK_HIT");//h
        } catch (Exception e)
        {
            ATTACK_HIT = diceStatsPlayer1.getInt("d");
        }
        //DEFENSE PLAYER 1
        int DEFENSE_EYE;
        try
        {
            DEFENSE_EYE = diceStatsPlayer1.getInt("DEFENSE_EYE");//h
        } catch (Exception e)
        {
            DEFENSE_EYE = diceStatsPlayer1.getInt("f");
        }
        int DEFENSE_BLANK;
        try
        {
            DEFENSE_BLANK = diceStatsPlayer1.getInt("DEFENSE_BLANK");//h
        } catch (Exception e)
        {
            DEFENSE_BLANK = diceStatsPlayer1.getInt("g");
        }
        int DEFENSE_EVADE;
        try
        {
            DEFENSE_EVADE = diceStatsPlayer1.getInt("DEFENSE_EVADE");//h
        } catch (Exception e)
        {
            DEFENSE_EVADE = diceStatsPlayer1.getInt("e");
        }

        final Map<DiceFace, Integer> map = new HashMap<>();

        map.put(DiceFace.ATTACK_BLANK, ATTACK_BLANK);
        map.put(DiceFace.ATTACK_EYE, ATTACK_EYE);
        map.put(DiceFace.ATTACK_HIT, ATTACK_HIT);
        map.put(DiceFace.ATTACK_CRIT, ATTACK_CRIT);
        map.put(DiceFace.DEFENSE_BLANK, DEFENSE_BLANK);
        map.put(DiceFace.DEFENSE_EYE, DEFENSE_EYE);
        map.put(DiceFace.DEFENSE_EVADE, DEFENSE_EVADE);

        return map;
    }


    private List<Round> extractRounds(String roundsJson) throws JSONException
    {
        final List<Round> rounds = new ArrayList<>();
        final JSONArray roundArray = new JSONArray(roundsJson);
        for (int i = 0; i < roundArray.length(); i++)
        {
            final JSONObject round = roundArray.getJSONObject(i);
            //Round Number
            String roundNumber;
            try
            {
                roundNumber = round.getString("roundNumber");//h
            } catch (Exception e)
            {
                roundNumber = round.getString("b");
            }
            //SCORE P1
            JSONObject scoreP1;
            try
            {
                scoreP1 = round.getJSONObject("scorePlayer1");//h
            } catch (Exception e)
            {
                scoreP1 = round.getJSONObject("c");
            }
            int scoreGP1;
            try
            {
                scoreGP1 = scoreP1.getInt("scoreGlobal");//h
            } catch (Exception e)
            {
                scoreGP1 = scoreP1.getInt("b");
            }
            int scoreKP1;
            try
            {
                scoreKP1 = scoreP1.getInt("scoreKill");//h
            } catch (Exception e)
            {
                scoreKP1 = scoreP1.getInt("c");
            }
            int scoreMP1;
            try
            {
                scoreMP1 = scoreP1.getInt("scoreMission");//h
            } catch (Exception e)
            {
                scoreMP1 = scoreP1.getInt("d");
            }
            final Score scorePlayer1 = new Score(scoreGP1, scoreKP1, scoreMP1);
            //SCORE P2
            JSONObject scoreP2;
            try
            {
                scoreP2 = round.getJSONObject("scorePlayer2");//h
            } catch (Exception e)
            {
                scoreP2 = round.getJSONObject("d");
            }
            int scoreGP2;
            try
            {
                scoreGP2 = scoreP2.getInt("scoreGlobal");//h
            } catch (Exception e)
            {
                scoreGP2 = scoreP2.getInt("b");
            }
            int scoreKP2;
            try
            {
                scoreKP2 = scoreP2.getInt("scoreKill");//h
            } catch (Exception e)
            {
                scoreKP2 = scoreP2.getInt("c");
            }
            int scoreMP2;
            try
            {
                scoreMP2 = scoreP2.getInt("scoreMission");//h
            } catch (Exception e)
            {
                scoreMP2 = scoreP2.getInt("d");
            }
            final Score scorePlayer2 = new Score(scoreGP2, scoreKP2, scoreMP2);
            //FP
            String firstPlayer;
            try
            {
                firstPlayer = round.getString("firstPlayer");//h
            } catch (Exception e)
            {
                firstPlayer = round.getString("e");
            }
            //Time
            int time;
            try
            {
                time = round.getInt("time");//i
            } catch (Exception e)
            {
                time = round.getInt("f");
            }
            rounds.add(new Round(roundNumber, scorePlayer1, scorePlayer2, firstPlayer, time));
        }
        return rounds;
    }
}
