package fr.corentin.roux.x_wing_score_tracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * @author Corentin Roux
 * <p>
 * The Main view at the start of the application
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private TextInputEditText timer;
    private Button btnStart;

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
            if (Objects.nonNull(this.timer) && Objects.nonNull(this.timer.getText()) && !this.timer.getText().toString().equals("")) {
                final Intent intent = new Intent(this, TimerActivity.class);
                intent.putExtra("timer", this.timer.getText().toString());
                this.startActivity(intent);
            } else {
                Toast.makeText(this, "Timer is not correctly set !!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Binding of all the fields XML and the fields JAVA
     */
    private void findView() {
        this.timer = this.findViewById(R.id.inputTimer);
        this.btnStart = this.findViewById(R.id.btnStart);
    }

}