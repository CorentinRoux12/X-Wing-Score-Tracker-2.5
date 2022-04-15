package fr.corentin.roux.x_wing_score_tracker.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.TimerActivity;
import lombok.Getter;

@Getter
public class EndDialogTimer extends DialogFragment {

    private final TimerActivity timerActivity;
    private View view;
    private Button btnYesEnd;
    private Button btnNoEnd;

    public EndDialogTimer(final TimerActivity timerActivity) {
        this.timerActivity = timerActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        //Mapping view
        this.view = inflater.inflate(R.layout.end_dialog, container, false);
        //Mapping fields
        this.findView();
        //Init listeners Buttons of the View
        this.listeners();
        //Return the generated View
        return this.getView();
    }


    private void findView() {
        this.btnYesEnd = this.view.findViewById(R.id.btnYesEnd);
        this.btnNoEnd = this.view.findViewById(R.id.btnNoEnd);
    }

    private void listeners() {
        if (this.btnYesEnd != null) {
            //End Game, call of the destroy view
            this.btnYesEnd.setOnClickListener(v -> {
                this.timerActivity.setEnd(true);
                this.timerActivity.onBackPressed();
                this.dismiss();
            });
        }
        if (this.btnNoEnd != null) {
            this.btnNoEnd.setOnClickListener(v -> this.dismiss());
        }
    }

}
