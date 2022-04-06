package fr.corentin.roux.score_tracker.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import fr.corentin.roux.score_tracker.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TimerActivity extends AppCompatActivity {

    private boolean timerStart = false;
    private TextView timeClock;
    private Button btnStartStop;
    private Button btnLessPlayerOne;
    private TextView textViewScorePlayerOne;
    private Button btnPlusPlayerOne;
    private Button btnLessPlayerTwo;
    private TextView textViewScorePlayerTwo;
    private Button btnPlusPlayerTwo;

    private int timeToSet;
    private int timeLeft;
    private CountDownTimer timer;
    private int scorePlayerOne = 0;
    private int scorePlayerTwo = 0;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.timer_layout);

        //TODO calcul a faire
        this.timeToSet = (int) this.getIntent().getSerializableExtra("timer");

        this.findView();

        this.timeLeft = this.timeToSet;
        this.timeClock.setText(this.timeToSet);
        this.timer = new CountDownTimer(this.timeToSet, /*1000 = 1 seconde*/1000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                TimerActivity.this.timeClock.setText(String.valueOf(TimerActivity.this.timeLeft));
                TimerActivity.this.timeLeft--;
            }

            @Override
            public void onFinish() {
                //TODO Alarm Minuterie / autre a faire
                Toast.makeText(TimerActivity.this, "TIME OVER !!", Toast.LENGTH_LONG).show();
            }
        };

        this.btnStartStop.setOnClickListener(t -> {
            if (this.timerStart) {
                //Le timer a commencé => on appuie sur le btn STOP
                // => Stopper le timer
                // => Afficher START
                this.timer.cancel();
                this.btnStartStop.setText("START");
                this.timerStart = false;
            } else {
                //Le timer n'a commencé => on appuie sur le btn START
                // => Start le timer
                // => Afficher STOP
                this.timer.start();
                this.btnStartStop.setText("STOP");
                this.timerStart = true;
            }
        });

        this.btnLessPlayerOne.setOnClickListener(t -> {
            if (this.scorePlayerOne > 0) {
                this.scorePlayerOne--;
                this.textViewScorePlayerOne.setText(this.scorePlayerOne);
            }
        });
        this.btnLessPlayerTwo.setOnClickListener(t -> {
            if (this.scorePlayerTwo > 0) {
                this.scorePlayerTwo--;
                this.textViewScorePlayerTwo.setText(this.scorePlayerTwo);
            }
        });
        this.btnPlusPlayerOne.setOnClickListener(t -> {
            this.scorePlayerOne++;
            this.textViewScorePlayerOne.setText(this.scorePlayerOne);
        });
        this.btnPlusPlayerTwo.setOnClickListener(t -> {
            this.scorePlayerTwo++;
            this.textViewScorePlayerTwo.setText(this.scorePlayerTwo);
        });
    }

    private void findView() {
        this.timeClock = this.findViewById(R.id.timeClock);
        this.btnStartStop = this.findViewById(R.id.btnStart);
        this.btnLessPlayerOne = this.findViewById(R.id.btnLessPlayerOne);
        this.textViewScorePlayerOne = this.findViewById(R.id.scorePlayerOne);
        this.btnPlusPlayerOne = this.findViewById(R.id.btnPlusPlayerOne);
        this.btnLessPlayerTwo = this.findViewById(R.id.btnLessPlayerTwo);
        this.textViewScorePlayerTwo = this.findViewById(R.id.scorePlayerTwo);
        this.btnPlusPlayerTwo = this.findViewById(R.id.btnPlusPlayerTwo);
    }
}
