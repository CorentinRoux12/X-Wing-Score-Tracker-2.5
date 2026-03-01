package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Représente un tour de dés dans une partie.
 * Stocke les statistiques des faces de dés obtenues pour les deux joueurs.
 */
public class DiceTurn implements Persistable, Serializable
{
    private Map<DiceFace, Integer> diceStatsPlayer1 = new EnumMap<>(DiceFace.class);

    private Map<DiceFace, Integer> diceStatsPlayer2 = new EnumMap<>(DiceFace.class);

    /**
     * Constructeur avec les statistiques initiales.
     * @param diceStatsPlayer1 Statistiques du joueur 1.
     * @param diceStatsPlayer2 Statistiques du joueur 2.
     */
    public DiceTurn(Map<DiceFace, Integer> diceStatsPlayer1, Map<DiceFace, Integer> diceStatsPlayer2)
    {
        this.diceStatsPlayer1 = diceStatsPlayer1;
        this.diceStatsPlayer2 = diceStatsPlayer2;
    }

    /**
     * Constructeur par défaut initialisant toutes les faces à zéro.
     */
    public DiceTurn()
    {
        this.diceStatsPlayer1.put(DiceFace.ATTACK_CRIT, 0);
        this.diceStatsPlayer1.put(DiceFace.ATTACK_HIT, 0);
        this.diceStatsPlayer1.put(DiceFace.ATTACK_EYE, 0);
        this.diceStatsPlayer1.put(DiceFace.ATTACK_BLANK, 0);
        this.diceStatsPlayer1.put(DiceFace.DEFENSE_EVADE, 0);
        this.diceStatsPlayer1.put(DiceFace.DEFENSE_EYE, 0);
        this.diceStatsPlayer1.put(DiceFace.DEFENSE_BLANK, 0);
        this.diceStatsPlayer2.put(DiceFace.ATTACK_CRIT, 0);
        this.diceStatsPlayer2.put(DiceFace.ATTACK_HIT, 0);
        this.diceStatsPlayer2.put(DiceFace.ATTACK_EYE, 0);
        this.diceStatsPlayer2.put(DiceFace.ATTACK_BLANK, 0);
        this.diceStatsPlayer2.put(DiceFace.DEFENSE_EVADE, 0);
        this.diceStatsPlayer2.put(DiceFace.DEFENSE_EYE, 0);
        this.diceStatsPlayer2.put(DiceFace.DEFENSE_BLANK, 0);
    }

    /**
     * Calcule le total des dés pour ce tour.
     * @return Un nouvel objet {@link DiceCounter} contenant les totaux.
     */
    public DiceCounter makeTheCount()
    {
        final DiceCounter diceCounter = new DiceCounter();
        diceCounter.updateCount(this);
        return diceCounter;
    }


    public Map<DiceFace, Integer> getDiceStatsPlayer1()
    {
        return diceStatsPlayer1;
    }

    public void setDiceStatsPlayer1(Map<DiceFace, Integer> diceStatsPlayer1)
    {
        this.diceStatsPlayer1 = diceStatsPlayer1;
    }

    public Map<DiceFace, Integer> getDiceStatsPlayer2()
    {
        return diceStatsPlayer2;
    }

    public void setDiceStatsPlayer2(Map<DiceFace, Integer> diceStatsPlayer2)
    {
        this.diceStatsPlayer2 = diceStatsPlayer2;
    }

    /**
     * Incrémente le compteur d'une face de dé pour un joueur.
     * @param diceFace La face à incrémenter.
     * @param player Le joueur concerné.
     */
    public void upDice(DiceFace diceFace, Player player)
    {
        if (player == Player.ONE)
        {
            this.diceStatsPlayer1.merge(diceFace, 1, Integer::sum);
        } else
        {
            this.diceStatsPlayer2.merge(diceFace, 1, Integer::sum);
        }
    }

    /**
     * Décrémente le compteur d'une face de dé pour un joueur (minimum 0).
     * @param diceFace La face à décrémenter.
     * @param player Le joueur concerné.
     */
    public void downDice(DiceFace diceFace, Player player)
    {
        if (player == Player.ONE)
        {
            this.diceStatsPlayer1.merge(diceFace, 1, (oldValue, newValue) -> oldValue != 0 ?
                    oldValue - newValue :
                    oldValue);
        } else
        {
            this.diceStatsPlayer2.merge(diceFace, 1, (oldValue, newValue) -> oldValue != 0 ?
                    oldValue - newValue :
                    oldValue);
        }
    }
}
