package fr.corentin.roux.x_wing_score_tracker.utils;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * Classe utilitaire pour la gestion des événements de sélection dans les adaptateurs de vue.
 * Fournit une implémentation par défaut pour {@link AdapterView.OnItemSelectedListener}.
 */
public class AdapterViewUtils implements AdapterView.OnItemSelectedListener
{
    /** {@inheritDoc} */
    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id)
    {
        Log.d(AdapterViewUtils.class.getSimpleName(), "On Item Selected call");
    }

    /** {@inheritDoc} */
    @Override
    public void onNothingSelected(final AdapterView<?> parent)
    {
        Log.d(AdapterViewUtils.class.getSimpleName(), "On Nothing Selected call");
    }
}
