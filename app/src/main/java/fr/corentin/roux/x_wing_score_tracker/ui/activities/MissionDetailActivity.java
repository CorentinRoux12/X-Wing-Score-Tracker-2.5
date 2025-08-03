package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import com.github.barteksc.pdfviewer.PDFView;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;

public class MissionDetailActivity extends AbstractActivity
{

    private PDFView pdfView;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initContentView()
    {
        this.setContentView(R.layout.mission_layout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void findView()
    {
        this.pdfView = this.findViewById(R.id.missionRules);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDatas()
    {
        final Mission mission = Mission.parseCode((Integer) this.getIntent().getSerializableExtra("mission"));
        if (mission != null)
        {
            this.pdfView.fitToWidth(1);
            this.pdfView.fromAsset("en/scenarios.pdf")
                    .defaultPage(mission.getPage())
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true)
                    .enableAntialiasing(true)
                    .swipeHorizontal(true)
                    .load();
        }
    }

}
