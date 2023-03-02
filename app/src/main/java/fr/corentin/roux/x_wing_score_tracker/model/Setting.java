package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Setting implements Persistable, Serializable {

    private String name;

    private String opponent;

    private String language;

    private String randomTime = "75";

    private String volatilityTime = "3";

    private Boolean enabledDarkTheme = Boolean.FALSE;

    private String pathRingTone = "";

   private List<Ship> listPlayer1 = new ArrayList<>();

   private List<Ship> listPlayer2 = new ArrayList<>();

    public Setting() {
    }

    public Setting(String name, String opponent, String language, String randomTime, String volatilityTime, Boolean enabledDarkTheme, String pathRingTone, List<Ship> listPlayer1, List<Ship> listPlayer2) {
        this.name = name;
        this.opponent = opponent;
        this.language = language;
        this.randomTime = randomTime;
        this.volatilityTime = volatilityTime;
        this.enabledDarkTheme = enabledDarkTheme;
        this.pathRingTone = pathRingTone;
        this.listPlayer1 = listPlayer1;
        this.listPlayer2 = listPlayer2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRandomTime() {
        return randomTime;
    }

    public void setRandomTime(String randomTime) {
        this.randomTime = randomTime;
    }

    public String getVolatilityTime() {
        return volatilityTime;
    }

    public void setVolatilityTime(String volatilityTime) {
        this.volatilityTime = volatilityTime;
    }

    public Boolean getEnabledDarkTheme() {
        return enabledDarkTheme;
    }

    public void setEnabledDarkTheme(Boolean enabledDarkTheme) {
        this.enabledDarkTheme = enabledDarkTheme;
    }

    public String getPathRingTone() {
        return pathRingTone;
    }

    public void setPathRingTone(String pathRingTone) {
        this.pathRingTone = pathRingTone;
    }

    public List<Ship> getListPlayer1() {
        return listPlayer1;
    }

    public void setListPlayer1(List<Ship> listPlayer1) {
        this.listPlayer1 = listPlayer1;
    }

    public List<Ship> getListPlayer2() {
        return listPlayer2;
    }

    public void setListPlayer2(List<Ship> listPlayer2) {
        this.listPlayer2 = listPlayer2;
    }
}
