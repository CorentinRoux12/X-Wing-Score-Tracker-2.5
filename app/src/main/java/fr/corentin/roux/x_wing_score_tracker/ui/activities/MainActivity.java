package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Language;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.utils.LocaleHelper;

/**
 * @author Corentin Roux
 * <p>
 * The Main view at the start of the application
 */
public class MainActivity extends AbstractActivity {

    private final Random random = new Random();
    private TextInputEditText timer;
    private Button btnStart;
    private Button btnHistorique;
    private Button btnSetting;
    private Button btnRandom;
    private CheckBox checkHideTimer;
    private CheckBox checkHideTimeLeft;
    private boolean timerHideCheck = false;
    private boolean timeLeftHideCheck = false;
    private String time;
    private Button btnRandomMission;
    private TextView textViewRandomMission;
    private Mission mission;
    private Setting setting;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDatas() {
        this.setting = SettingService.getInstance().getSetting(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initContentView() {
        this.setContentView(R.layout.main_layout);
    }

    /**
     * Pour le changement de langue on doit redemarer l app ...
     * {@inheritDoc}
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        this.setting = SettingService.getInstance().getSetting(newBase);
        super.attachBaseContext(LocaleHelper.checkDefaultLanguage(setting, newBase));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (this.getSharedPreferences("settingChange", Context.MODE_PRIVATE).getBoolean("settingsChange", true)) {
            this.getSharedPreferences("settingChange", Context.MODE_PRIVATE).edit().putBoolean("settingsChange", false).apply();
            this.recreate();
        }
        this.initDefaultValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDefaultValue() {
        this.checkHideTimer.setChecked(false);
        this.checkHideTimeLeft.setChecked(false);
        this.timerHideCheck = false;
        this.timeLeftHideCheck = false;

        if (setting.getEnabledDarkTheme() != null) {
            AppCompatDelegate.setDefaultNightMode(Boolean.TRUE.equals(setting.getEnabledDarkTheme()) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initListeners() {
        this.btnStart.setOnClickListener(t -> {
            if (this.timer != null && this.timer.getText() != null && !this.timer.getText().toString().equals("")) {
                this.time = this.timer.getText().toString();
                this.startTimerActivity();
            } else {
                Toast.makeText(this, "Timer is not correctly set !!", Toast.LENGTH_LONG).show();
            }
        });

        this.btnHistorique.setOnClickListener(t -> {
            final Intent intent = new Intent(this, HistoriqueActivity.class);
            this.startActivity(intent);
        });
        this.btnSetting.setOnClickListener(t -> {
            final Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
        });
        this.btnRandom.setOnClickListener(t -> {
            this.time = this.generateRandomTime();
            this.startTimerActivity();
        });
        this.checkHideTimer.setOnClickListener(t -> this.timerHideCheck = this.checkHideTimer.isChecked());
        this.checkHideTimeLeft.setOnClickListener(t -> this.timeLeftHideCheck = this.checkHideTimeLeft.isChecked());
        this.btnRandomMission.setOnClickListener(t -> {
            this.mission = Mission.parseCode(this.random.nextInt(4) + 1);
            this.textViewRandomMission.setText(this.mission.getLibelle(), TextView.BufferType.SPANNABLE);
            Toast.makeText(this, "Touch the mission for details", Toast.LENGTH_SHORT).show();
        });
        this.textViewRandomMission.setOnClickListener(t -> {
            if (this.mission != null) {
                final Intent intent = new Intent(this, MissionDetailActivity.class);
                intent.putExtra("mission", this.mission.getCode());
                this.startActivity(intent);
            }
        });
    }

    private String generateRandomTime() {
        final long attackDice = this.random.nextInt(8);
        final Setting setting = SettingService.getInstance().getSetting(this);
        int randomTimer = 75;
        try {
            randomTimer = Integer.parseInt(setting.getRandomTime());
            int cpt = Integer.parseInt(setting.getVolatilityTime());
            cpt = Math.abs(cpt);
            final List<Long> defenseDice = new ArrayList<>();
            for (int i = 0; i < cpt; i++) {
                defenseDice.add((long) this.random.nextInt(8));
            }
            randomTimer = this.calculVolatility(attackDice, defenseDice, randomTimer);
            randomTimer = Math.abs(randomTimer);
            return String.valueOf(randomTimer);
        } catch (final Exception e) {
            Log.e(this.getClass().getSimpleName(), "Erreur de parsing de la volatilité.");
            final long defenseDice1 = this.random.nextInt(8);
            final long defenseDice2 = this.random.nextInt(8);
            final long defenseDice3 = this.random.nextInt(8);
            final List<Long> defenseDice = Arrays.asList(defenseDice1, defenseDice2, defenseDice3);
            int basicTime = setting.getRandomTime() != null && !setting.getRandomTime().trim().equals("") ?
                    randomTimer :
                    75;
            basicTime = this.calculVolatility(attackDice, defenseDice, basicTime);
            return String.valueOf(basicTime);
        }
    }

    private int calculVolatility(final long attackDice, final List<Long> defenseDice, int basicTime) {
        //Defense
        //0&1&2 = Blank
        //3&4 = Eyes
        //5-7 = Evades
        if (0L == attackDice || 1L == attackDice) {//0&1 = Blank
            for (final Long valueDefenseDice : defenseDice) {
                basicTime = this.downGradeTime(basicTime, valueDefenseDice);
            }
        } else if (4L <= attackDice) { //4-7 = Hit&Crit
            for (final Long valueDefenseDice : defenseDice) {
                basicTime = this.upgradeTime(basicTime, valueDefenseDice);
            }
        }
        return basicTime;
    }

    private int upgradeTime(int basicTime, final Long valueDefenseDice) {
        if (valueDefenseDice >= 3L) {
            basicTime++;
        }
        return basicTime;
    }

    private int downGradeTime(int basicTime, final Long valueDefenseDice) {
        if (valueDefenseDice >= 3L) {
            basicTime--;
        }
        return basicTime;
    }

    private void startTimerActivity() {
        final Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("hideTimeLeft", this.timeLeftHideCheck);
        intent.putExtra("hideTimer", this.timerHideCheck);
        intent.putExtra("timer", this.time);
        intent.putExtra("mission", this.mission != null ? this.mission : Mission.NO_MISSION);
        this.startActivity(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void findView() {
        this.timer = this.findViewById(R.id.inputTimer);
        this.btnStart = this.findViewById(R.id.btnStart);
        this.btnHistorique = this.findViewById(R.id.btnHistorique);
        this.btnSetting = this.findViewById(R.id.btnSetting);
        this.btnRandom = this.findViewById(R.id.btnRandom);
        this.checkHideTimer = this.findViewById(R.id.checkHideTimer);
        this.btnRandomMission = this.findViewById(R.id.btnRandomMission);
        this.textViewRandomMission = this.findViewById(R.id.textViewRandomMission);
        this.checkHideTimeLeft = this.findViewById(R.id.checkHideTimeLeft);
    }

}