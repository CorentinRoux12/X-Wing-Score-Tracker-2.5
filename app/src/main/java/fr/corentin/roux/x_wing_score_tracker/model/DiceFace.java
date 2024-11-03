package fr.corentin.roux.x_wing_score_tracker.model;

public enum DiceFace
{
    ATTACK_BLANK("ATTACK_BLANK", 0.25, "Attack Blank", 4, true),
    ATTACK_EYE("ATTACK_EYE", 0.25, "Attack Eye", 3, true),
    ATTACK_HIT("ATTACK_HIT", 0.375, "Attack Hit", 2, true),
    ATTACK_CRIT("ATTACK_CRIT", 0.125, "Attack Crit", 1, true),
    DEFENSE_BLANK("DEFENSE_BLANK", 0.375, "Defense Blank", 7, false),
    DEFENSE_EYE("DEFENSE_EYE", 0.25, "Defense Eye", 6, false),
    DEFENSE_EVADE("DEFENSE_EVADE", 0.375, "Defense Evade", 5, false);

    private String libelle;

    private Double proba;

    private String printLibelle;

    private Integer ordre;

    private boolean isAttack;

    DiceFace(String libelle, Double proba, String printLibelle, Integer ordre, boolean isAttack)
    {
        this.libelle = libelle;
        this.proba = proba;
        this.printLibelle = printLibelle;
        this.ordre = ordre;
        this.isAttack = isAttack;
    }

    public String getLibelle()
    {
        return libelle;
    }

    public Double getProba()
    {
        return proba;
    }

    public String getPrintLibelle()
    {
        return printLibelle;
    }

    public Integer getOrdre()
    {
        return ordre;
    }

    public boolean isAttack()
    {
        return isAttack;
    }
}