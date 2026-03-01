package fr.corentin.roux.x_wing_score_tracker.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import java.util.Optional;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.TimerActivity;

/**
 * Boîte de dialogue de confirmation pour terminer une partie.
 * S'affiche généralement lorsque l'utilisateur souhaite quitter ou terminer le chronomètre.
 */
public class EndDialogTimer extends DialogFragment
{

    /** L'activité parente gérant le timer. */
    private final TimerActivity timerActivity;
    /** La vue générée par le layout. */
    private View view;
    /** Bouton YES de la vue. */
    private Button btnYesEnd;
    /** Bouton NON de la vue. */
    private Button btnNoEnd;

    /**
     * Constructeur.
     *
     * @param timerActivity L'activité parente gérant le timer.
     */
    public EndDialogTimer(final TimerActivity timerActivity)
    {
        this.timerActivity = timerActivity;
    }

    /** {@inheritDoc} */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
    {
        //Mapping view
        this.view = inflater.inflate(R.layout.end_dialog, container, false);
        //Mapping fields
        this.findView();
        //Init listeners Buttons of the View
        this.listeners();
        //Return the generated View
        return this.getView();
    }


    /**
     * Initialise les vues à partir du layout.
     */
    private void findView()
    {
        this.btnYesEnd = this.view.findViewById(R.id.btnYesEnd);
        this.btnNoEnd = this.view.findViewById(R.id.btnNoEnd);
    }

    /**
     * Initialise les écouteurs d'événements sur les boutons.
     */
    private void listeners()
    {
        Optional.ofNullable(this.btnYesEnd)
                .ifPresent(button -> button.setOnClickListener(view -> endTimerActivity()));

        Optional.ofNullable(this.btnNoEnd)
                .ifPresent(button -> button.setOnClickListener(view -> this.dismiss()));
    }

    /**
     * Termine l'activité du timer.
     */
    private void endTimerActivity()
    {
        //End Game, call of the destroy view
        this.timerActivity.setEnd(true);
        this.timerActivity.onBackPressed();
        this.dismiss();
    }

    /** {@inheritDoc} */
    @Override
    public View getView()
    {
        return this.view;
    }

}
