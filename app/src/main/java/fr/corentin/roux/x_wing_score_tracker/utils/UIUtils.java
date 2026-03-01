package fr.corentin.roux.x_wing_score_tracker.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Classe utilitaire pour les manipulations d'interface utilisateur (UI).
 * Contient des méthodes pour ajuster dynamiquement les composants graphiques.
 */
public class UIUtils
{
    /**
     * Constructeur privé pour la classe utilitaire.
     */
    private UIUtils()
    {
        //Nothing to handle
    }

    /**
     * Ajuste dynamiquement la hauteur d'une ListView en fonction du nombre et de la taille de ses éléments.
     * Utile lorsque la ListView est placée à l'intérieur d'un ScrollView.
     *
     * @param listView La ListView à redimensionner.
     */
    public static void setListViewHeightBasedOnItems(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null)
        {

            int numberOfItems = listAdapter.getCount();

            // Calcul de la hauteur totale de tous les éléments
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++)
            {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Calcul de la hauteur totale des diviseurs
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Mise à jour des paramètres de mise en page
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
        }
    }
}
