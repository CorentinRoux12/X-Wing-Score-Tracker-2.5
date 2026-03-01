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

/**
 * Adaptateur pour la liste de l'historique des parties.
 * Gère l'affichage résumé de chaque partie et les actions de consultation ou suppression.
 */
public class HistoriqueAdapter extends BaseAdapter
{
    private final List<Game> games;
    private final Context context;
    private final LayoutInflater inflater;
    private final HistoriqueActivity activity;

    /**
     * Constructeur.
     *
     * @param activity L'activité {@link HistoriqueActivity} parente.
     * @param allGames La liste des parties à afficher.
     */
    public HistoriqueAdapter(final HistoriqueActivity activity, final List<Game> allGames)
    {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.games = allGames;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /** {@inheritDoc} */
    @Override
    public int getCount()
    {
        return this.games.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object getItem(final int position)
    {
        return this.games.get(position);
    }

    /** {@inheritDoc} */
    @Override
    public long getItemId(final int position)
    {
        return position;
    }

    /** {@inheritDoc} */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        @SuppressLint("ViewHolder") final View rowView = this.inflater.inflate(R.layout.historique_adapter_layout, parent, false);
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
     * Initialise les écouteurs de clics pour consulter le détail ou supprimer une partie.
     */
    private void initialisationListeners(final ViewHolder holder, final int position)
    {
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

    /**
     * Formate et affiche les informations résumées de la partie (date, score, mission).
     */
    private void initialisationData(final ViewHolder holder, final int position)
    {
        final Game current = (Game) this.getItem(position);
        final StringBuilder score = new StringBuilder();
        score.append(" Mission : ")
                .append(current.getMission())
                .append("\n")
                .append(current.getDate())
                .append(" Total Round : ")
                .append(current.getRound() < 10 ? "0" + current.getRound() : current.getRound())
                .append(" | ")
                .append(current.getScoreGlobalPlayer1() < 10 ? "0" + current.getScoreGlobalPlayer1() : current.getScoreGlobalPlayer1())
                .append("-")
                .append(current.getScoreGlobalPlayer2() < 10 ? "0" + current.getScoreGlobalPlayer2() : current.getScoreGlobalPlayer2());

        holder.getData().setText(score);
    }

    /**
     * Mappe les vues du layout de l'adaptateur.
     */
    public void findViews(final View rowView, final ViewHolder holder)
    {
        holder.data = rowView.findViewById(R.id.data);
        holder.delete = rowView.findViewById(R.id.delete);
    }

    /**
     * Pattern ViewHolder pour l'optimisation de la liste.
     */
    public static class ViewHolder
    {
        private TextView data;
        private ImageView delete;

        public TextView getData()
        {
            return data;
        }
    }

    public List<Game> getGames()
    {
        return games;
    }
}
