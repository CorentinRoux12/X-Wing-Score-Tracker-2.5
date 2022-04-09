package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;

public class MissionDetailActivity extends AppCompatActivity {
    private PDFView pdfView;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.mission_layout);

        final Mission mission = Mission.parseCode((Integer) this.getIntent().getSerializableExtra("mission"));

        this.findView();

        if (mission != null) {
            this.pdfView.fitToWidth();
            this.pdfView.documentFitsView();
            this.pdfView.fromAsset(mission.getRessource())
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true)
                    .enableAntialiasing(true)
                    .swipeHorizontal(true)
                    .load();
        }
    }


    private void findView() {
        this.pdfView = this.findViewById(R.id.missionRules);
    }
}
