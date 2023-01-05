package fr.corentin.roux.x_wing_score_tracker.model;

import java.io.Serializable;

public class Setting implements Persistable, Serializable {

    private String name;

    private String opponent;

    private String language;

    private String randomTime = "75";

    private String volatilityTime = "3";

    private Boolean enabledDarkTheme;

    public Setting() {
    }

    public Setting(String name, String opponent, String language, String randomTime, String volatilityTime, Boolean enabledDarkTheme) {
        this.name = name;
        this.opponent = opponent;
        this.language = language;
        this.randomTime = randomTime;
        this.volatilityTime = volatilityTime;
        this.enabledDarkTheme = enabledDarkTheme;
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
}
