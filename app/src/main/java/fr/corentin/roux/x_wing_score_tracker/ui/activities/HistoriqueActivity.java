package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Games;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.HistoriqueAdapter;

public class HistoriqueActivity extends AppCompatActivity {

    private final HistoriqueService service;
    private HistoriqueAdapter adapter;
    private ListView listHistorique;

    public HistoriqueActivity() {
        this.service = HistoriqueService.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.historique_layout);

        this.findView();

        Games games = this.service.getAllGames(this.getBaseContext());
        if (games == null) {
            games = new Games();
        }
        this.adapter = new HistoriqueAdapter(this, games);
        this.listHistorique.setAdapter(this.adapter);
    }

    private void findView() {
        this.listHistorique = this.findViewById(R.id.listHistorique);
    }

    public void deleteGame(final int pos) {
        final Games games = this.service.getAllGames(this.getBaseContext());
        games.getGames().remove(pos);
        this.adapter.getGames().remove(pos);
        this.service.saveGames(games, this);
        this.adapter.notifyDataSetChanged();
    }
}
