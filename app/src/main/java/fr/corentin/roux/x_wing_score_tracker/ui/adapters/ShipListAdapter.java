package fr.corentin.roux.x_wing_score_tracker.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Player;
import fr.corentin.roux.x_wing_score_tracker.model.Ship;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.TimerActivity;

public class ShipListAdapter extends BaseAdapter {
    private static final String RED = "#9d0208";
    private static final String YELLOW = "#E5E500";
    private static final String GREEN = "#2b9348";

    private final List<Ship> ships;
    private final Context context;
    private final LayoutInflater inflater;
    private final TimerActivity activity;
    private final Player player;

    public ShipListAdapter(TimerActivity activity, Player player) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.player = player;
        this.ships = player.getShips();
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ships.size();
    }

    @Override
    public Object getItem(int position) {
        return ships.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

    private void initialisationListeners(ViewHolder holder, int position) {
        holder.nomShip.setOnClickListener(t -> changeScoreListener(position, holder));
        holder.pointShip.setOnClickListener(t -> changeScoreListener(position, holder));
    }

    private void changeScoreListener(int position, ViewHolder holder) {
        Ship current = (Ship) this.getItem(position);
        Ship.Statut oldStatut = current.changeStatut();
        holder.changeColor(current.getStatut());
        this.activity.changeScoreByOpponentPlayer(player,current, oldStatut);
        this.activity.updateScorePlayers();
        this.notifyDataSetChanged();
    }



    private void initialisationData(ViewHolder holder, int position) {
        final Ship current = (Ship) this.getItem(position);
        holder.getNomShip().setText(current.getName());//.length() < 10 ? current.getName() : current.getName().substring(0,9));
        holder.getPointShip().setText(String.valueOf(current.getPoints()));
        holder.changeColor(current.getStatut());
    }

    public void findViews(final View rowView, final ViewHolder holder) {
        holder.nomShip = rowView.findViewById(R.id.nomShip);
        holder.pointShip = rowView.findViewById(R.id.pointShip);
    }

    public static class ViewHolder {
        private TextView nomShip;
        private Button pointShip;

        public TextView getNomShip() {
            return nomShip;
        }

        public Button getPointShip() {
            return pointShip;
        }

        public void changeColor(Ship.Statut statut) {
            if (statut.equals(Ship.Statut.FULL)) {
                pointShip.setBackgroundColor(Color.parseColor(GREEN));
            } else if (statut.equals(Ship.Statut.HALF)) {
                pointShip.setBackgroundColor(Color.parseColor(YELLOW));
            } else {
                pointShip.setBackgroundColor(Color.parseColor(RED));

            }
        }
    }
}
