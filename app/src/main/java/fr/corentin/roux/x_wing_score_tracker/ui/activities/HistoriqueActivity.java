package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.services.GameService;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.HistoriqueAdapter;

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

    public void deleteGame(final int pos) {
        final List<Game> games = GameService.getInstance().getAll(this.getBaseContext());
        Game gameToRemove = games.get(pos);
        GameService.getInstance().delete(this, gameToRemove);
        this.historiqueAdapter.getGames().remove(pos);
//        GameService.getInstance().save(this,games);
        this.historiqueAdapter.notifyDataSetChanged();
    }
}
