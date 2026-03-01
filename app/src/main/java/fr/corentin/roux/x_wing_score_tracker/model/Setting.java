package fr.corentin.roux.x_wing_score_tracker.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Entité représentant les paramètres de l'application.
 * Stocke les préférences utilisateur telles que la langue, le thème, le temps de jeu et les listes de vaisseaux.
 */
@Entity
public class Setting implements Persistable, Serializable
{

    @PrimaryKey//(autoGenerate = true)
    private Integer id = 1;

    private String name;

    private String opponent;

    private String language;

    private String randomTime = "75";

    private String volatilityTime = "3";

    private Boolean enabledDarkTheme = Boolean.FALSE;

    private String pathRingTone = "";

    private String listPlayer1 = "";

    private String listPlayer2 = "";

    private Boolean diceCounter = Boolean.FALSE;

    private Boolean lowResolutionMode = Boolean.FALSE;

    /**
     * Constructeur par défaut.
     */
    public Setting()
    {
    }

    /**
     * Constructeur avec tous les champs (utilisé par Room via @Ignore).
     */
    @Ignore
    public Setting(Integer id, String name, String opponent, String language, String randomTime, String volatilityTime, Boolean enabledDarkTheme, String pathRingTone, String listPlayer1, String listPlayer2, Boolean diceCounter, Boolean lowResolutionMode)
    {
        this.id = id;
        this.name = name;
        this.opponent = opponent;
        this.language = language;
        this.randomTime = randomTime;
        this.volatilityTime = volatilityTime;
        this.enabledDarkTheme = enabledDarkTheme;
        this.pathRingTone = pathRingTone;
        this.listPlayer1 = listPlayer1;
        this.listPlayer2 = listPlayer2;
        this.diceCounter = diceCounter;
        this.lowResolutionMode = lowResolutionMode;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOpponent()
    {
        return opponent;
    }

    public void setOpponent(String opponent)
    {
        this.opponent = opponent;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getRandomTime()
    {
        return randomTime;
    }

    public void setRandomTime(String randomTime)
    {
        this.randomTime = randomTime;
    }

    public String getVolatilityTime()
    {
        return volatilityTime;
    }

    public void setVolatilityTime(String volatilityTime)
    {
        this.volatilityTime = volatilityTime;
    }

    public Boolean getEnabledDarkTheme()
    {
        return enabledDarkTheme;
    }

    public void setEnabledDarkTheme(Boolean enabledDarkTheme)
    {
        this.enabledDarkTheme = enabledDarkTheme;
    }

    public String getPathRingTone()
    {
        return pathRingTone;
    }

    public void setPathRingTone(String pathRingTone)
    {
        this.pathRingTone = pathRingTone;
    }

    public String getListPlayer1()
    {
        return listPlayer1;
    }

    public void setListPlayer1(String listPlayer1)
    {
        this.listPlayer1 = listPlayer1;
    }

    public String getListPlayer2()
    {
        return listPlayer2;
    }

    public void setListPlayer2(String listPlayer2)
    {
        this.listPlayer2 = listPlayer2;
    }

    public Boolean getDiceCounter()
    {
        return diceCounter;
    }

    public void setDiceCounter(Boolean diceCounter)
    {
        this.diceCounter = diceCounter;
    }

    public Boolean getLowResolutionMode()
    {
        return lowResolutionMode;
    }

    public void setLowResolutionMode(Boolean lowResolutionMode)
    {
        this.lowResolutionMode = lowResolutionMode;
    }
}
