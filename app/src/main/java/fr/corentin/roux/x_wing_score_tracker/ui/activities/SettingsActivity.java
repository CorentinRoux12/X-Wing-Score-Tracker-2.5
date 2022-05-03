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
//    Resources resources;
    private TextInputEditText inputName;
    private TextInputEditText inputOpponent;
    private TextInputEditText inputTime;
    private TextInputEditText inputVolatility;
    //    private Spinner language;
//    private String langue;
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
//                if (item != null) {
//                    //SAVE de la langue
//                    SettingsActivity.this.langue = item.toString();
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
//        this.langue = this.setting.getLanguage();
        this.inputName.setText(this.setting.getName());
        this.inputOpponent.setText(this.setting.getOpponent());
        this.inputTime.setText(this.setting.getRandomTime());
        this.inputVolatility.setText(this.setting.getVolatilityTime());
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
        this.inputTime = this.findViewById(R.id.inputTime);
        this.inputVolatility = this.findViewById(R.id.inputVolatility);
//        this.language = this.findViewById(R.id.language);
        this.inputName = this.findViewById(R.id.inputName);
        this.inputOpponent = this.findViewById(R.id.inputOpponent);
    }

    @Override
    protected void onDestroy() {
//        this.setting.setLanguage(this.langue);
        String time = this.inputTime.getText().toString();
        if ("".equals(time.trim())) {
            time = "75";
        }
        this.setting.setRandomTime(time);
        this.setting.setVolatilityTime(this.inputVolatility.getText().toString());
        this.setting.setName(this.inputName.getText().toString());
        this.setting.setOpponent(this.inputOpponent.getText().toString());
        this.service.save(this, this.setting);

        super.onDestroy();
    }


}
