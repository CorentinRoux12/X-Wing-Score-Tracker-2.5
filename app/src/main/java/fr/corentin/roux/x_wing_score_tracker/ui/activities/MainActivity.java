package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import fr.corentin.roux.x_wing_score_tracker.R;

/**
 * @author Corentin Roux
 * <p>
 * The Main view at the start of the application
 */
public class MainActivity extends AppCompatActivity {

    private TextInputEditText timer;
    private Button btnStart;
    private Button btnHistorique;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.main_layout);

        this.findView();

        this.initListeners();
    }

    /**
     * Initialization of the listeners for each interaction in the view
     */
    private void initListeners() {
        this.btnStart.setOnClickListener(t -> {
            if (this.timer != null && this.timer.getText() != null && !this.timer.getText().toString().equals("")) {
                final Intent intent = new Intent(this, TimerActivity.class);
                intent.putExtra("timer", this.timer.getText().toString());
                this.startActivity(intent);
            } else {
                Toast.makeText(this, "Timer is not correctly set !!", Toast.LENGTH_LONG).show();
            }
        });

        this.btnHistorique.setOnClickListener(t -> {
            final Intent intent = new Intent(this, HistoriqueActivity.class);
            this.startActivity(intent);
        });
    }

    /**
     * Binding of all the fields XML and the fields JAVA
     */
    private void findView() {
        this.timer = this.findViewById(R.id.inputTimer);
        this.btnStart = this.findViewById(R.id.btnStart);
        this.btnHistorique = this.findViewById(R.id.btnHistorique);
    }

}