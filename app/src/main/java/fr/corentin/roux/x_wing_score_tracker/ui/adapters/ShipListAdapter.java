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

public class ShipListAdapter extends BaseAdapter
{
    private static final String RED = "#9d0208";
    private static final String YELLOW = "#E5E500";
    private static final String GREEN = "#2b9348";

    private final List<Ship> ships;
    private final Context context;
    private final LayoutInflater inflater;
    private final TimerActivity activity;
    private final String player;

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

    @Override
    public int getCount()
    {
        return ships.size();
    }

    @Override
    public Object getItem(int position)
    {
        return ships.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        @SuppressLint("ViewHolder") final View rowView = this.inflater.inflate(R.layout.ship_adapter_layout, parent, false);
        final ViewHolder holder = new ViewHolder();
        //On fait un mapping entre le xml et le java
        this.findViews(rowView, holder);
        //On initialise les champs de la page avec les données du contrat
        this.initialisationData(holder, position);
        //On initialise les listeners de la page
        this.initialisationListeners(holder, position);
        //On return la vue remplie
        return rowView;
    }

    private void initialisationListeners(ViewHolder holder, int position)
    {
        holder.nomShip.setOnClickListener(t -> changeScoreListener(position, holder));
        holder.pointShip.setOnClickListener(t -> changeScoreListener(position, holder));
    }

    private void changeScoreListener(int position, ViewHolder holder)
    {
        Ship current = (Ship) this.getItem(position);
        Ship.Statut oldStatut = current.changeStatut();
        holder.changeColor(current.getStatut());
        this.activity.changeScore(player, current, oldStatut);
        this.activity.updateScorePlayers();
        this.notifyDataSetChanged();
    }

    private void initialisationData(ViewHolder holder, int position)
    {
        final Ship current = (Ship) this.getItem(position);
        holder.getNomShip().setText(current.getName());
        holder.getPointShip().setText(String.valueOf(current.getPoints()));
        holder.changeColor(current.getStatut());
    }

    public void findViews(final View rowView, final ViewHolder holder)
    {
        holder.nomShip = rowView.findViewById(R.id.nomShip);
        holder.pointShip = rowView.findViewById(R.id.pointShip);
    }

    public static class ViewHolder
    {
        private TextView nomShip;
        private Button pointShip;

        public TextView getNomShip()
        {
            return nomShip;
        }

        public Button getPointShip()
        {
            return pointShip;
        }

        public void changeColor(Ship.Statut statut)
        {
            if (statut.equals(Ship.Statut.FULL))
            {
                pointShip.setBackgroundColor(Color.parseColor(GREEN));
            } else if (statut.equals(Ship.Statut.HALF))
            {
                pointShip.setBackgroundColor(Color.parseColor(YELLOW));
            } else
            {
                pointShip.setBackgroundColor(Color.parseColor(RED));

            }
        }
    }
}
