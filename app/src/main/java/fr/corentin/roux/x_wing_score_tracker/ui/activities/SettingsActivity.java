package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Language;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.model.Ship;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.utils.AdapterViewUtils;
import fr.corentin.roux.x_wing_score_tracker.utils.LocaleHelper;
import io.vavr.control.Try;
import xyz.aprildown.ultimatemusicpicker.MusicPickerListener;
import xyz.aprildown.ultimatemusicpicker.UltimateMusicPicker;

@SuppressLint("SetTextI18n")
public class SettingsActivity extends AbstractActivity implements MusicPickerListener {

    private final SettingService service = SettingService.getInstance();

    private TextInputEditText inputName;
    private TextInputEditText inputOpponent;
    private TextInputEditText inputTime;
    private TextInputEditText inputVolatility;
    private Button darkModeBtn;
    private Button alarmeSong;
    private Spinner language;
    private String langue;
    private Boolean enabledDarkMode;
    private String pathRingTone;
    private Setting setting;

    private Button myListImport;
    private Button opponentListImport;

    private Button resetOpponentListImport;
    private Button resetMyListImport;

    /** Permet de set une liste de ship dans les settings joueur 1 */
    private final Consumer<List<Ship>> setListPlayer1 = s -> this.setting.setListPlayer1(s);

    /** Permet de set une liste de ship dans les settings joueur 2 */
    private final Consumer<List<Ship>> setListPlayer2 = s -> this.setting.setListPlayer2(s);

    private final BiConsumer<Consumer<List<Ship>>, String> setListPlayers = (settingList, player) -> Try.of(() -> (ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
            .mapTry(this::extractList)
            .andThenTry(settingList::accept)
            .onSuccess(ships -> Toast.makeText(this, "XWS save for : " + player, Toast.LENGTH_SHORT).show())
            .onFailure(throwable -> Toast.makeText(this, "XWS invalid", Toast.LENGTH_SHORT).show());

    @Override
    protected void initContentView() {
        this.setContentView(R.layout.settings_layout);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        this.setting = this.service.getSetting(newBase);
        super.attachBaseContext(LocaleHelper.checkDefaultLanguage(setting, newBase));
    }

    @Override
    public void onBackPressed() {
        this.getSharedPreferences("settingChange", Context.MODE_PRIVATE).edit().putBoolean("settingsChange", true).apply();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        this.saveSettings();
        super.onDestroy();
    }

    @Override
    protected void initDatas() {
        this.setting = this.service.getSetting(this);

        this.formatLanguage();
        this.langue = this.setting.getLanguage();
        this.inputName.setText(this.setting.getName());
        this.inputOpponent.setText(this.setting.getOpponent());
        this.inputTime.setText(this.setting.getRandomTime());
        this.inputVolatility.setText(this.setting.getVolatilityTime());

        this.enabledDarkMode = setting.getEnabledDarkTheme();
        this.pathRingTone = setting.getPathRingTone();
        this.darkModeBtn.setText("Dark Mode : " + (Boolean.TRUE.equals(enabledDarkMode) ? "Yes" : "No"));
    }

    @Override
    protected void initListeners() {
        this.darkModeBtn.setOnClickListener(t -> {
            if (enabledDarkMode == setting.getEnabledDarkTheme()) {
                this.enabledDarkMode = !this.enabledDarkMode;
                if (Boolean.TRUE.equals(this.enabledDarkMode)) {
                    this.setting.setEnabledDarkTheme(Boolean.TRUE);
                    reloadDarkMode();
                } else {
                    this.setting.setEnabledDarkTheme(Boolean.FALSE);
                    reloadDarkMode();
                }
            }
        });

        this.alarmeSong.setOnClickListener(click -> Try.of(UltimateMusicPicker::new)
                .map(alarm -> alarm.windowTitle("Alarm Selection"))
                .map(UltimateMusicPicker::removeSilent)
                .map(alarm -> alarm.streamType(AudioManager.STREAM_ALARM))
                .map(UltimateMusicPicker::ringtone)
                .map(UltimateMusicPicker::notification)
                .map(UltimateMusicPicker::alarm)
                .map(UltimateMusicPicker::music)
                .andThenTry(alarm -> alarm.goWithDialog(getSupportFragmentManager()))
                .onFailure(throwable -> Toast.makeText(this, "An error arrive during the alarm selection, restart the app and if need, contact the developper for correction.", Toast.LENGTH_LONG).show()));

        this.language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long id) {
                final Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    SettingsActivity.this.langue = item.toString();
                    if (setting != null && !langue.equals(setting.getLanguage())) {
                        SettingsActivity.this.saveSettings();
                        SettingsActivity.this.startSettingsActivity();
                    }
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                Log.d(AdapterViewUtils.class.getSimpleName(), "On Nothing Selected call");
            }
        });

        this.myListImport.setOnClickListener(click -> setListPlayers.accept(setListPlayer1, inputName.getText().toString()));
        this.opponentListImport.setOnClickListener(click -> setListPlayers.accept(setListPlayer2, inputOpponent.getText().toString()));


        this.resetOpponentListImport.setOnClickListener(click -> {
            this.setting.setListPlayer2(Collections.emptyList());
            Toast.makeText(this, "XWS reset for : " + inputOpponent.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        this.resetMyListImport.setOnClickListener(t -> {
            this.setting.setListPlayer1(Collections.emptyList());
            Toast.makeText(this, "XWS reset for : " + inputName.getText().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    private List<Ship> extractList(ClipboardManager clipboardManager) throws JSONException {
        final List<Ship> listPlayer = new ArrayList<>();
        final String json = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
        final JSONArray pilots = new JSONObject(json).getJSONArray("pilots");
        for (int i = 0; i < pilots.length(); i++) {
            JSONObject pilot = pilots.getJSONObject(i);
            String name = pilot.getString("id");
            int point = pilot.getInt("points");
            listPlayer.add(new Ship(name, point));
        }
        return listPlayer;
    }

    private void formatLanguage() {
        //Code Ihm stocké ici
        final Language langue = Language.parseCodeIhm(this.setting.getLanguage());
        final ArrayAdapter<CharSequence> adapterConst = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
        adapterConst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.language.setAdapter(adapterConst);
        if (langue != null) {
            final int spinnerPosition = adapterConst.getPosition(langue.getCodeIhm());
            this.language.setSelection(spinnerPosition);
        }
    }

    private void reloadDarkMode() {
        Toast.makeText(this, "Don't click to fast my young apprentice.", Toast.LENGTH_SHORT).show();
        AppCompatDelegate.setDefaultNightMode(this.enabledDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void startSettingsActivity() {
        Toast.makeText(this, "Recreate Incomming", Toast.LENGTH_SHORT).show();
        this.recreate();
//        final Intent intent = new Intent(this, SettingsActivity.class);
//        this.startActivity(intent);
    }

    @Override
    protected void findView() {
        this.inputTime = this.findViewById(R.id.inputTime);
        this.inputVolatility = this.findViewById(R.id.inputVolatility);
        this.language = this.findViewById(R.id.language);
        this.inputName = this.findViewById(R.id.inputName);
        this.inputOpponent = this.findViewById(R.id.inputOpponent);
        this.darkModeBtn = this.findViewById(R.id.darkModeBtn);
        this.alarmeSong = this.findViewById(R.id.setAlarme);
        this.myListImport = this.findViewById(R.id.myListImport);
        this.opponentListImport = this.findViewById(R.id.opponentListImport);
        this.resetOpponentListImport = this.findViewById(R.id.resetOpponentListImport);
        this.resetMyListImport = this.findViewById(R.id.resetMyListImport);
    }

    private void saveSettings() {
        String time = this.inputTime.getText().toString();
        if ("".equals(time.trim())) {
            time = "75";
        }
        this.setting.setLanguage(this.langue);
        this.setting.setRandomTime(time);
        this.setting.setVolatilityTime(this.inputVolatility.getText().toString());
        this.setting.setName(this.inputName.getText().toString());
        this.setting.setOpponent(this.inputOpponent.getText().toString());
        this.setting.setEnabledDarkTheme(this.enabledDarkMode);
        this.setting.setPathRingTone(this.pathRingTone);
        this.service.save(this, this.setting);
    }


    @Override
    public void onMusicPick(@NonNull Uri uri, @NonNull String s) {
        this.pathRingTone = uri.toString();
    }

    @Override
    public void onPickCanceled() {
    }
}
