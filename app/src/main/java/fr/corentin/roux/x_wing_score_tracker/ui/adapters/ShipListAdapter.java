package fr.corentin.roux.x_wing_score_tracker.ui.adapters;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Ship;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.TimerActivity;
import io.vavr.control.Try;

/**
 * Adaptateur pour la liste des vaisseaux d'un joueur.
 * Permet d'afficher les vaisseaux et de changer leur statut (Intact, Moitié, Détruit) en cliquant dessus,
 * ce qui met à jour automatiquement le score dans la TimerActivity.
 */
public class ShipListAdapter extends BaseAdapter
{
    /** Couleurs RED utilisées pour les différents statuts des vaisseaux. */
    private static final String RED = "#9d0208";
    /** Couleurs YELLOW utilisées pour les différents statuts des vaisseaux. */
    private static final String YELLOW = "#E5E500";
    /** Couleurs GREEN utilisées pour les différents statuts des vaisseaux. */
    private static final String GREEN = "#2b9348";

    private final List<Ship> ships;
    private final Context context;
    private final LayoutInflater inflater;
    private final TimerActivity activity;
    private final String player;

    /**
     * Constructeur.
     *
     * @param activity L'activité {@link TimerActivity} parente.
     * @param name     Nom du joueur associé à cette liste.
     * @param xws      Chaîne JSON représentant la liste des vaisseaux (format XWS).
     */
    public ShipListAdapter(TimerActivity activity, String name, String xws)
    {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.player = name;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        this.ships = Try.of(() -> xws)
                .mapTry(this::extractList)
                .onFailure(throwable -> Toast.makeText(activity, "XWS invalid", Toast.LENGTH_SHORT).show())
                .getOrElse(Collections.emptyList());
    }

    /**
     * Extrait la liste des vaisseaux à partir de la chaîne JSON XWS.
     */
    private List<Ship> extractList(String xws) throws JSONException
    {
        final List<Ship> listPlayer = new ArrayList<>();

        if (xws.isBlank())
        {
            return listPlayer;
        }

        final JSONArray pilots = new JSONArray(xws);
        for (int i = 0; i < pilots.length(); i++)
        {
            final JSONObject pilot = pilots.getJSONObject(i);
            String name;
            try
            {
                name = pilot.getString("name");//e
            } catch (Exception e)
            {
                name = pilot.getString("a");
            }
            int point;
            try
            {
                point = pilot.getInt("points");//f
            } catch (Exception e)
            {
                point = pilot.getInt("b");//f
            }
            listPlayer.add(new Ship(name, point));
        }
        return listPlayer;
    }

    /** {@inheritDoc} */
    @Override
    public int getCount()
    {
        return ships.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object getItem(int position)
    {
        return ships.get(position);
    }

    /** {@inheritDoc} */
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /** {@inheritDoc} */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        @SuppressLint("ViewHolder") final View rowView = this.inflater.inflate(R.layout.ship_adapter_layout, parent, false);
        final ViewHolder holder = new ViewHolder();
        //Mapping XML <-> Java
        this.findViews(rowView, holder);
        //Initialisation des données
        this.initialisationData(holder, position);
        //Initialisation des listeners
        this.initialisationListeners(holder, position);
        return rowView;
    }

    /**
     * Initialise les listeners sur les boutons de chaque vaisseau.
     *
     * @param holder   ViewHolder contenant les vues du vaisseau.
     * @param position Position du vaisseau dans la liste.
     */
    private void initialisationListeners(ViewHolder holder, int position)
    {
        holder.nomShip.setOnClickListener(t -> changeScoreListener(position, holder));
        holder.pointShip.setOnClickListener(t -> changeScoreListener(position, holder));
    }

    /**
     * Gère le clic sur un vaisseau pour changer son statut et mettre à jour le score global.
     */
    private void changeScoreListener(int position, ViewHolder holder)
    {
        Ship current = (Ship) this.getItem(position);
        Ship.Statut oldStatut = current.changeStatut();
        holder.changeColor(current.getStatut());
        this.activity.changeScore(player, current, oldStatut);
        this.activity.updateScorePlayers();
        this.notifyDataSetChanged();
    }

    /**
     * Initialise les données du vaisseau dans la vue.
     *
     * @param holder   ViewHolder contenant les vues du vaisseau.
     * @param position Position du vaisseau dans la liste.
     */
    private void initialisationData(ViewHolder holder, int position)
    {
        final Ship current = (Ship) this.getItem(position);
        holder.getNomShip().setText(current.getName());
        holder.getPointShip().setText(String.valueOf(current.getPoints()));
        holder.changeColor(current.getStatut());
    }

    /**
     * Mapping XML <-> Java.
     *
     * @param rowView La vue générée par le layout.
     * @param holder  ViewHolder contenant les vues du vaisseau.
     */
    public void findViews(final View rowView, final ViewHolder holder)
    {
        holder.nomShip = rowView.findViewById(R.id.nomShip);
        holder.pointShip = rowView.findViewById(R.id.pointShip);
    }

    /**
     * Pattern ViewHolder avec gestion dynamique de la couleur selon le statut du vaisseau.
     */
    public static class ViewHolder
    {
        private TextView nomShip;
        private Button pointShip;

        /**
         * Getter du nom du vaisseau.
         *
         * @return Nom du vaisseau.
         */
        public TextView getNomShip()
        {
            return nomShip;
        }

        /**
         * Getter du bouton de points.
         *
         * @return Bouton de points.
         */
        public Button getPointShip()
        {
            return pointShip;
        }

        /**
         * Change la couleur de fond du bouton de points selon le statut (Intact, Moitié, Détruit).
         *
         * @param statut Statut du vaisseau.
         */
        public void changeColor(Ship.Statut statut)
        {
            this.pointShip.setBackgroundColor(switch (statut)
            {
                case FULL -> Color.parseColor(GREEN);
                case HALF -> Color.parseColor(YELLOW);
                case DEAD -> Color.parseColor(RED);
            });
        }
    }
}
