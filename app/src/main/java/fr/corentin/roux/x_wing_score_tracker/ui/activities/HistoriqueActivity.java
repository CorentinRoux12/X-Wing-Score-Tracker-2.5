package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Game;
import fr.corentin.roux.x_wing_score_tracker.services.HistoriqueService;
import fr.corentin.roux.x_wing_score_tracker.ui.adapters.HistoriqueAdapter;

public class HistoriqueActivity extends AppCompatActivity {

    private final HistoriqueService service = HistoriqueService.getInstance();
    private HistoriqueAdapter adapter;
    private ListView listHistorique;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.historique_layout);

        this.findView();

        List<Game> games = this.service.getAllGames(this.getBaseContext());
        if (games == null) {
            games = new ArrayList<>();
        }
        this.adapter = new HistoriqueAdapter(this, games);
        this.listHistorique.setAdapter(this.adapter);
    }

    private void findView() {
        this.listHistorique = this.findViewById(R.id.listHistorique);
    }

    public void deleteGame(final int pos) {
        final List<Game> games = this.service.getAllGames(this.getBaseContext());
        games.remove(pos);
        this.adapter.getGames().remove(pos);
        this.service.saveGames(games, this);
        this.adapter.notifyDataSetChanged();
    }
}
