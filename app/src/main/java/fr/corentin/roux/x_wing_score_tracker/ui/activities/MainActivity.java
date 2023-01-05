package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;

/**
 * @author Corentin Roux
 * <p>
 * The Main view at the start of the application
 */
public class MainActivity extends AppCompatActivity {

    private final Random random = new Random();
    private final SettingService settingService = SettingService.getInstance();
    //    @Getter
//    private LocaleManager localeManager;
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.main_layout);
        this.setting = this.settingService.getSetting(this);

//        this.initDarkMode();
        this.findView();

        this.initListeners();

        this.initDefaultValue();
    }

    //Pour le changement de langue on doit redemarer l app ...
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(checkDefaultLanguage(newBase));
    }

    private Context checkDefaultLanguage(Context context) {
        this.setting = this.settingService.getSetting(context);

        Language language;
        if (this.setting.getLanguage() == null) {
            language = Language.ENGLISH;
        } else {
            language = Language.parseCodeIhm(this.setting.getLanguage());
        }
        Locale locale;
        if (language == null) {
            language = Language.ENGLISH;
        }
        switch (language) {
            case FRENCH:
                locale = Locale.FRENCH;
                break;
//            case ENGLISH:
//                locale = Locale.ENGLISH;
//                break;
            default:
                locale = Locale.ENGLISH;
                break;
        }
        Locale.setDefault(locale);

        final Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        if (setting.getEnabledDarkTheme() != null) {
            AppCompatDelegate.setDefaultNightMode(Boolean.TRUE.equals(setting.getEnabledDarkTheme()) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        }

        return context.createConfigurationContext(configuration);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.initDefaultValue();
    }

    private void initDefaultValue() {
        this.checkHideTimer.setChecked(false);
        this.checkHideTimeLeft.setChecked(false);
        this.timerHideCheck = false;
        this.timeLeftHideCheck = false;
    }

    /**
     * Initialization of the listeners for each interaction in the view
     */
    private void initListeners() {
        this.btnStart.setOnClickListener(t -> {
            if (this.timer != null && this.timer.getText() != null && !this.timer.getText().toString().equals("")) {
                this.time = this.timer.getText().toString();
                this.startTimerActivity();
            } else {
                Toast.makeText(this, "Timer is not correctly set !!", Toast.LENGTH_LONG).show();
            }
        });

        this.btnHistorique.setOnClickListener(t -> this.startHistoriqueActivity());
        this.btnSetting.setOnClickListener(t -> this.startSettingsActivity());
        this.btnRandom.setOnClickListener(t -> {
            this.time = this.generateRandomTime();
            this.startTimerActivity();
        });
        this.checkHideTimer.setOnClickListener(t -> this.timerHideCheck = this.checkHideTimer.isChecked());
        this.checkHideTimeLeft.setOnClickListener(t -> this.timeLeftHideCheck = this.checkHideTimeLeft.isChecked());
        this.btnRandomMission.setOnClickListener(t -> this.generateRandomMission());
        this.textViewRandomMission.setOnClickListener(t -> this.startMissionDetailActivity());
    }

    private void startSettingsActivity() {
        final Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }

    private void startMissionDetailActivity() {
        if (this.mission != null) {
            final Intent intent = new Intent(this, MissionDetailActivity.class);
            intent.putExtra("mission", this.mission.getCode());
            this.startActivity(intent);
        }
    }

    private void generateRandomMission() {
        this.mission = Mission.parseCode(this.random.nextInt(4) + 1);
        this.textViewRandomMission.setText(this.mission.getLibelle(), TextView.BufferType.SPANNABLE);
        Toast.makeText(this, "Touch the mission for details", Toast.LENGTH_SHORT).show();
    }

    private String generateRandomTime() {
        final long attackDice = this.random.nextInt(8);
        final Setting setting = this.settingService.getSetting(this);
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

    private void startHistoriqueActivity() {
        final Intent intent = new Intent(this, HistoriqueActivity.class);
        this.startActivity(intent);
    }

    /**
     * Binding of all the fields XML and the fields JAVA
     */
    private void findView() {
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