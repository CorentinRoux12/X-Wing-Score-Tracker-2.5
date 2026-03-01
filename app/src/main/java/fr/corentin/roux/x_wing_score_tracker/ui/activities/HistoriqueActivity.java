package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.services.GameService;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.HistoriqueAdapter;

/**
 * Activité affichant l'historique de toutes les parties enregistrées.
 * Permet de visualiser la liste des parties passées et d'en supprimer.
 */
public class HistoriqueActivity extends AbstractActivity {

    private HistoriqueAdapter historiqueAdapter;
    private ListView listHistorique;
    private List<Game> games =  new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initContentView() {
        this.setContentView(R.layout.historique_layout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void findView() {
        this.listHistorique = this.findViewById(R.id.listHistorique);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDatas() {
        this.games = GameService.getInstance().getAll(this.getBaseContext());
        if (this.games == null) {
            this.games = new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initAdapters() {
        this.historiqueAdapter = new HistoriqueAdapter(this, games);
        this.listHistorique.setAdapter(this.historiqueAdapter);
    }

    /**
     * Supprime une partie de la liste et de la base de données.
     * @param pos La position de la partie dans l'adaptateur.
     */
    public void deleteGame(final int pos) {
        final List<Game> gamesSaved = GameService.getInstance().getAll(this.getBaseContext());
        Game gameToRemove = gamesSaved.get(pos);
        GameService.getInstance().delete(this, gameToRemove);
        this.historiqueAdapter.getGames().remove(pos);
        this.historiqueAdapter.notifyDataSetChanged();
    }
}
