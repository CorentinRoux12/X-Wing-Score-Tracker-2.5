package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Setting;
import fr.corentin.roux.x_wing_score_tracker.services.SettingService;

public class SettingsActivity extends AppCompatActivity {

    private final SettingService service = SettingService.getInstance();
    //    Context context;
//    private Resources resources;
    private TextInputEditText inputName;
    private TextInputEditText inputOpponent;
    //    private Spinner language;
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
//        this.listeners();
    }

//    private void listeners() {
//        this.language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long id) {
//                final Object item = adapterView.getItemAtPosition(position);
//                Log.e(AdapterViewUtils.class.getSimpleName(), "NICE !!!");
//                String itemString = "";
//                if (item != null) {
//                    itemString = item.toString();
//
//                    //SAVE de la langue
//                    SettingsActivity.this.setting.setLanguage(itemString);
//                    //TODO Force reload du system avec la langue
//
//                    final Locale locale = new Locale(Language.parseCodeIhm(itemString).getCodeLanguage());
//                    Locale.setDefault(locale);
//                    final Configuration config = new Configuration();
//                    config.locale = locale;
//                    SettingsActivity.this.getBaseContext().getResources().updateConfiguration(config, SettingsActivity.this.getBaseContext().getResources().getDisplayMetrics());
//                    SettingsActivity.this.setContentView(R.layout.settings_layout);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(final AdapterView<?> parent) {
//                Log.d(AdapterViewUtils.class.getSimpleName(), "On Nothing Selected call");
//            }
//        });
//    }

    private void initData() {
        this.setting = this.service.getSetting(this);

//        this.formatLanguage();
        this.inputName.setText(this.setting.getName());
        this.inputOpponent.setText(this.setting.getOpponent());
    }

//    private void formatLanguage() {
//        //Code Ihm stocké ici
//        final Language langue = Language.parseCodeIhm(this.setting.getLanguage());
//        final ArrayAdapter<CharSequence> adapterConst = ArrayAdapter.createFromResource(this, R.array.language, android.R.layout.simple_spinner_item);
//        adapterConst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        this.language.setAdapter(adapterConst);
//        if (langue != null) {
//            final int spinnerPosition = adapterConst.getPosition(langue.getCodeIhm());
//            this.language.setSelection(spinnerPosition);
//        }
//    }

    private void findView() {
//        this.language = this.findViewById(R.id.language);
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
