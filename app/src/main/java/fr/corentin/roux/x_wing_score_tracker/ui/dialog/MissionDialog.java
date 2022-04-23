package fr.corentin.roux.x_wing_score_tracker.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.Locale;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Language;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.TimerActivity;
import lombok.Getter;

@Getter
public class MissionDialog extends DialogFragment {

    private final TimerActivity timerActivity;
    private View view;
    private PDFView pdfView;

    public MissionDialog(final TimerActivity timerActivity) {
        this.timerActivity = timerActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        //Mapping view
        this.view = inflater.inflate(R.layout.mission_dialog, container, false);
        //Mapping fields
        this.findView();
        //Init des datas PDFs
        this.initDatas();
        //Return the generated View
        return this.getView();
    }

    private void initDatas() {
        if (this.timerActivity.getGame().getMission() != null) {
            String resource;
            if (Locale.getDefault().getCountry().toLowerCase().equals(Language.FRENCH.getCodeLanguage())) {
                resource = "fr/";
            } else { // Default Package => English
                resource = "en/";
            }
            resource += this.timerActivity.getGame().getMission().getRessource();
            resource += this.timerActivity.getGame().getMission().getExtension();

            this.pdfView.fromAsset(resource)
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true)
                    .enableAntialiasing(true)
                    .swipeHorizontal(true)
                    .load();
        }
    }

    private void findView() {
        this.pdfView = this.view.findViewById(R.id.missionRules);
    }


}
