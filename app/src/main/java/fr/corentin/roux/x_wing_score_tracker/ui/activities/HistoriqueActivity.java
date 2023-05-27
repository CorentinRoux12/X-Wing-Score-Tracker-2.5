package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.widget.ListView;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Games;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.HistoriqueAdapter;

public class HistoriqueActivity extends AbstractActivity {

    private HistoriqueAdapter historiqueAdapter;
    private ListView listHistorique;
    private Games games = null;

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
        this.games = HistoriqueService.getInstance().getAllGames(this.getBaseContext());
        if (this.games == null) {
            this.games = new Games();
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

    public void deleteGame(final int pos) {
        final Games games = HistoriqueService.getInstance().getAllGames(this.getBaseContext());
        games.getGames().remove(pos);
        this.historiqueAdapter.getGames().remove(pos);
        HistoriqueService.getInstance().saveGames(games, this);
        this.historiqueAdapter.notifyDataSetChanged();
    }
}
