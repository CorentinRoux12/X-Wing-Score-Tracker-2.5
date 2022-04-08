package fr.corentin.roux.x_wing_score_tracker.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.HistoriqueActivity;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.HistoriqueDetailActivity;
import lombok.Getter;

public class HistoriqueAdapter extends BaseAdapter {
    @Getter
    private final List<Game> games;
    private final Context context;
    private final LayoutInflater inflater;
    private final HistoriqueActivity activity;

    public HistoriqueAdapter(final HistoriqueActivity activity, final List<Game> allGames) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.games = allGames;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.games.size();
    }

    @Override
    public Object getItem(final int position) {
        return this.games.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        @SuppressLint("ViewHolder") final View rowView = this.inflater.inflate(R.layout.historique_adapter_layout, parent, false);
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

    private void initialisationListeners(final ViewHolder holder, final int position) {
        final Game current = (Game) this.getItem(position);
        holder.data.setOnClickListener(v -> {
            final Intent mIntent = new Intent(this.context, HistoriqueDetailActivity.class);
            mIntent.putExtra("game", current);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(mIntent);
        });
        holder.delete.setOnClickListener(v -> {
            this.activity.deleteGame(position);
            this.notifyDataSetChanged();
        });
    }

    private void initialisationData(final ViewHolder holder, final int position) {
        final Game current = (Game) this.getItem(position);
        final StringBuilder score = new StringBuilder();
        score.append(current.getDate())
                .append(" Total Round : ")
                .append(current.getRound() < 10 ? "0" + current.getRound() : current.getRound())
                .append(" | ")
                .append(current.getScorePlayer1() < 10 ? "0" + current.getScorePlayer1() : current.getScorePlayer1())
                .append("-")
                .append(current.getScorePlayer2() < 10 ? "0" + current.getScorePlayer1() : current.getScorePlayer2());

        holder.getData().setText(score);
    }

    public void findViews(final View rowView, final ViewHolder holder) {
        holder.data = rowView.findViewById(R.id.data);
        holder.delete = rowView.findViewById(R.id.delete);
    }

    @Getter
    public static class ViewHolder {
        private TextView data;
        private ImageView delete;
    }
}
