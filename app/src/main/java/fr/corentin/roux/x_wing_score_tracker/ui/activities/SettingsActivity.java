package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;

public class SettingsActivity extends AppCompatActivity {

    private final SettingService service = SettingService.getInstance();
    private TextInputEditText inputName;
    private TextInputEditText inputOpponent;
    private Setting setting;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.settings_layout);

        this.findView();

        this.initData();
    }

    private void initData() {
        this.setting = this.service.getSetting(this);
        this.inputName.setText(this.setting.getName());
        this.inputOpponent.setText(this.setting.getOpponent());
    }

    private void findView() {
        this.inputName = this.findViewById(R.id.inputName);
        this.inputOpponent = this.findViewById(R.id.inputOpponent);
    }


    @Override
    protected void onDestroy() {
        this.setting.setName(this.inputName.getText().toString());
        this.setting.setOpponent(this.inputOpponent.getText().toString());
        this.service.save(this, this.setting);
        super.onDestroy();
    }
}
