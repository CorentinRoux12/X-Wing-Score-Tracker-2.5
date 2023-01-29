package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Language;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.utils.AdapterViewUtils;
import fr.corentin.roux.x_wing_score_tracker.utils.LocaleHelper;
import xyz.aprildown.ultimatemusicpicker.MusicPickerListener;
import xyz.aprildown.ultimatemusicpicker.UltimateMusicPicker;

@SuppressLint("SetTextI18n")
public class SettingsActivity extends AppCompatActivity implements MusicPickerListener {

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.settings_layout);

        this.findView();
        //Init des datas de la vue
        this.initData();
        //Call des listeners les init
        this.listeners();
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

    private void initData() {
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

    private void listeners() {
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

        this.alarmeSong.setOnClickListener(t -> {
           new UltimateMusicPicker()
                    // Picker activity action bar title or dialog title
                    .windowTitle("Alarm Selection")

                    // Add a extra default item
                    //.defaultUri(uri)
                    // Add a default item and change the default item name("Default" is used otherwise)
                   //.defaultTitleAndUri("My default name", uri)

                    // There's a "silent" item by default, use this line to remove it.
                    .removeSilent()

                    // Select this uri
                   //.selectUri(uri)

                    // Add some other music items(from R.raw or app's asset)
                   //.additional("Myself Music", uri)
                   //.additional("Another Music", uri)

                    // Music preview stream type(AudioManager.STREAM_MUSIC is used by default)
                    .streamType(AudioManager.STREAM_ALARM)

                    // Show different kinds of system ringtones. Calling order determines their display order.
                    .ringtone()
                    .notification()
                    .alarm()
                    // Show music files from external storage. Requires READ_EXTERNAL_STORAGE permission.
                    .music()

                    // Show a picker dialog
                    .goWithDialog(getSupportFragmentManager());
            // Or show a picker activity
            //.goWithActivity(this, 0, MusicPickerActivity::class.java)
        });

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

    private void findView() {
        this.inputTime = this.findViewById(R.id.inputTime);
        this.inputVolatility = this.findViewById(R.id.inputVolatility);
        this.language = this.findViewById(R.id.language);
        this.inputName = this.findViewById(R.id.inputName);
        this.inputOpponent = this.findViewById(R.id.inputOpponent);
        this.darkModeBtn = this.findViewById(R.id.darkModeBtn);
        this.alarmeSong = this.findViewById(R.id.setAlarme);
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
