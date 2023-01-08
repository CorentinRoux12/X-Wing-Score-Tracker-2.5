package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Language;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;
import fr.corentin.roux.x_wing_score_tracker.utils.AdapterViewUtils;

@SuppressLint("SetTextI18n")
public class SettingsActivity extends AppCompatActivity {

    private final SettingService service = SettingService.getInstance();

    private TextInputEditText inputName;
    private TextInputEditText inputOpponent;
    private TextInputEditText inputTime;
    private TextInputEditText inputVolatility;
    private Button darkModeBtn;
    private Spinner language;
    private String langue;
    private Setting setting;
    private Boolean enabledDarkMode;

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

    private void listeners() {
        this.darkModeBtn.setOnClickListener(t -> {
            this.enabledDarkMode = !this.enabledDarkMode;

            if (Boolean.TRUE.equals(this.enabledDarkMode)) {
                this.setting.setEnabledDarkTheme(Boolean.TRUE);
                reloadDarkMode();
            } else {
                this.setting.setEnabledDarkTheme(Boolean.FALSE);
                reloadDarkMode();
            }
        });

        this.language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long id) {
                final Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    //SAVE de la langue
                    SettingsActivity.this.langue = item.toString();
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                Log.d(AdapterViewUtils.class.getSimpleName(), "On Nothing Selected call");
            }
        });
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
        this.darkModeBtn.setText("Dark Mode : " + (Boolean.TRUE.equals(enabledDarkMode) ? "Yes" : "No"));
    }

    private void reloadDarkMode() {
//        this.onNightModeChanged(this.enabledDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        Toast.makeText(this, "Don't click to fast my young apprentice.", Toast.LENGTH_LONG).show();
        AppCompatDelegate.setDefaultNightMode(this.enabledDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
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
            Toast.makeText(this, "Restart Application to apply settings.", Toast.LENGTH_LONG).show();
        }
    }

    private void findView() {
        this.inputTime = this.findViewById(R.id.inputTime);
        this.inputVolatility = this.findViewById(R.id.inputVolatility);
        this.language = this.findViewById(R.id.language);
        this.inputName = this.findViewById(R.id.inputName);
        this.inputOpponent = this.findViewById(R.id.inputOpponent);
        this.darkModeBtn = this.findViewById(R.id.darkModeBtn);
    }

    @Override
    protected void onDestroy() {
        String time = this.inputTime.getText().toString();
        if ("".equals(time.trim())) {
            time = "75";
        }
        this.setting.setLanguage(this.langue);
        this.setting.setRandomTime(time);
        this.setting.setVolatilityTime(this.inputVolatility.getText().toString());
        this.setting.setName(this.inputName.getText().toString());
        this.setting.setOpponent(this.inputOpponent.getText().toString());
        this.setting.setEnabledDarkTheme(enabledDarkMode);
        this.service.save(this, this.setting);

        super.onDestroy();
    }


}
