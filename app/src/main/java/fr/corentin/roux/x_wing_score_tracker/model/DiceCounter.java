package fr.corentin.roux.x_wing_score_tracker.model;

public class DiceCounter
{

    private Integer totalAttackPlayer1 = 0;
    private Integer totalDefensePlayer1 = 0;
    private Integer totalAttackPlayer2 = 0;
    private Integer totalDefensePlayer2 = 0;

    private Integer attackCritPlayer1 = 0;
    private Integer attackHitPlayer1 = 0;
    private Integer attackEyePlayer1 = 0;
    private Integer attackBlankPlayer1 = 0;
    private Integer defenseEvadePlayer1 = 0;
    private Integer defenseEyePlayer1 = 0;
    private Integer defenseBlankPlayer1 = 0;

    private Integer attackCritPlayer2 = 0;
    private Integer attackHitPlayer2 = 0;
    private Integer attackEyePlayer2 = 0;
    private Integer attackBlankPlayer2 = 0;
    private Integer defenseEvadePlayer2 = 0;
    private Integer defenseEyePlayer2 = 0;
    private Integer defenseBlankPlayer2 = 0;


    public void updateCount(DiceTurn diceTurn)
    {
        this.attackCritPlayer1 += diceTurn.getDiceStatsPlayer1().get(DiceFace.ATTACK_CRIT);
        this.attackHitPlayer1 += diceTurn.getDiceStatsPlayer1().get(DiceFace.ATTACK_HIT);
        this.attackEyePlayer1 += diceTurn.getDiceStatsPlayer1().get(DiceFace.ATTACK_EYE);
        this.attackBlankPlayer1 += diceTurn.getDiceStatsPlayer1().get(DiceFace.ATTACK_BLANK);
        this.defenseEvadePlayer1 += diceTurn.getDiceStatsPlayer1().get(DiceFace.DEFENSE_EVADE);
        this.defenseEyePlayer1 += diceTurn.getDiceStatsPlayer1().get(DiceFace.DEFENSE_EYE);
        this.defenseBlankPlayer1 += diceTurn.getDiceStatsPlayer1().get(DiceFace.DEFENSE_BLANK);

        this.attackCritPlayer2 += diceTurn.getDiceStatsPlayer2().get(DiceFace.ATTACK_CRIT);
        this.attackHitPlayer2 += diceTurn.getDiceStatsPlayer2().get(DiceFace.ATTACK_HIT);
        this.attackEyePlayer2 += diceTurn.getDiceStatsPlayer2().get(DiceFace.ATTACK_EYE);
        this.attackBlankPlayer2 += diceTurn.getDiceStatsPlayer2().get(DiceFace.ATTACK_BLANK);
        this.defenseEvadePlayer2 += diceTurn.getDiceStatsPlayer2().get(DiceFace.DEFENSE_EVADE);
        this.defenseEyePlayer2 += diceTurn.getDiceStatsPlayer2().get(DiceFace.DEFENSE_EYE);
        this.defenseBlankPlayer2 += diceTurn.getDiceStatsPlayer2().get(DiceFace.DEFENSE_BLANK);


        this.totalAttackPlayer1 = this.attackCritPlayer1 + this.attackHitPlayer1 + this.attackEyePlayer1 + this.attackBlankPlayer1;
        this.totalDefensePlayer1 = this.defenseEvadePlayer1 + this.defenseEyePlayer1 + this.defenseBlankPlayer1;
        this.totalAttackPlayer2 = this.attackCritPlayer2 + this.attackHitPlayer2 + this.attackEyePlayer2 + this.attackBlankPlayer2;
        this.totalDefensePlayer2 = this.defenseEvadePlayer2 + this.defenseEyePlayer2 + this.defenseBlankPlayer2;
    }
    //TODO a faire plus tard avec des champs spécialisé dessus
    // final Integer totalAttackPlayer1 = attackCritPlayer1 + attackHitPlayer1 + attackEyePlayer1 + attackBlankPlayer1 + defenseEvadePlayer1 + defenseEyePlayer1 + defenseBlankPlayer1;
    //        final Integer totalAttackPlayer2 = attackCritPlayer2 + attackHitPlayer2 + attackEyePlayer2 + attackBlankPlayer2 + defenseEvadePlayer2 + defenseEyePlayer2 + defenseBlankPlayer2;


    public void append(DiceCounter diceCounter)
    {
        this.attackCritPlayer1 += diceCounter.getAttackCritPlayer1();
        this.attackHitPlayer1 += diceCounter.getAttackHitPlayer1();
        this.attackEyePlayer1 += diceCounter.getAttackEyePlayer1();
        this.attackBlankPlayer1 += diceCounter.getAttackBlankPlayer1();
        this.defenseEvadePlayer1 += diceCounter.getDefenseEvadePlayer1();
        this.defenseEyePlayer1 += diceCounter.getDefenseEyePlayer1();
        this.defenseBlankPlayer1 += diceCounter.getDefenseBlankPlayer1();

        this.attackCritPlayer2 += diceCounter.getAttackCritPlayer2();
        this.attackHitPlayer2 += diceCounter.getAttackHitPlayer2();
        this.attackEyePlayer2 += diceCounter.getAttackEyePlayer2();
        this.attackBlankPlayer2 += diceCounter.getAttackBlankPlayer2();
        this.defenseEvadePlayer2 += diceCounter.getDefenseEvadePlayer2();
        this.defenseEyePlayer2 += diceCounter.getDefenseEyePlayer2();
        this.defenseBlankPlayer2 += diceCounter.getDefenseBlankPlayer2();


        this.totalAttackPlayer1 = this.attackCritPlayer1 + this.attackHitPlayer1 + this.attackEyePlayer1 + this.attackBlankPlayer1;
        this.totalDefensePlayer1 = this.defenseEvadePlayer1 + this.defenseEyePlayer1 + this.defenseBlankPlayer1;
        this.totalAttackPlayer2 = this.attackCritPlayer2 + this.attackHitPlayer2 + this.attackEyePlayer2 + this.attackBlankPlayer2;
        this.totalDefensePlayer2 = this.defenseEvadePlayer2 + this.defenseEyePlayer2 + this.defenseBlankPlayer2;
    }

    public Integer getTotalAttackPlayer1()
    {
        return totalAttackPlayer1;
    }

    public Integer getTotalDefensePlayer1()
    {
        return totalDefensePlayer1;
    }

    public Integer getTotalAttackPlayer2()
    {
        return totalAttackPlayer2;
    }

    public Integer getTotalDefensePlayer2()
    {
        return totalDefensePlayer2;
    }

    public Integer getAttackCritPlayer1()
    {
        return attackCritPlayer1;
    }

    public Integer getAttackHitPlayer1()
    {
        return attackHitPlayer1;
    }

    public Integer getAttackEyePlayer1()
    {
        return attackEyePlayer1;
    }

    public Integer getAttackBlankPlayer1()
    {
        return attackBlankPlayer1;
    }

    public Integer getDefenseEvadePlayer1()
    {
        return defenseEvadePlayer1;
    }

    public Integer getDefenseEyePlayer1()
    {
        return defenseEyePlayer1;
    }

    public Integer getDefenseBlankPlayer1()
    {
        return defenseBlankPlayer1;
    }

    public Integer getAttackCritPlayer2()
    {
        return attackCritPlayer2;
    }

    public Integer getAttackHitPlayer2()
    {
        return attackHitPlayer2;
    }

    public Integer getAttackEyePlayer2()
    {
        return attackEyePlayer2;
    }

    public Integer getAttackBlankPlayer2()
    {
        return attackBlankPlayer2;
    }

    public Integer getDefenseEvadePlayer2()
    {
        return defenseEvadePlayer2;
    }

    public Integer getDefenseEyePlayer2()
    {
        return defenseEyePlayer2;
    }

    public Integer getDefenseBlankPlayer2()
    {
        return defenseBlankPlayer2;
    }


}