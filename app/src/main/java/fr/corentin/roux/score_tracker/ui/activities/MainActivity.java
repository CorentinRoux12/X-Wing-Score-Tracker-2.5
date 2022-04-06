package fr.corentin.roux.score_tracker.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import fr.corentin.roux.score_tracker.R;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText timer;
    private Button btnStart;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.main_layout);

        this.findView();

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

    private void findView() {
        this.timer = this.findViewById(R.id.inputTimer);
        this.btnStart = this.findViewById(R.id.btnStart);
    }

}