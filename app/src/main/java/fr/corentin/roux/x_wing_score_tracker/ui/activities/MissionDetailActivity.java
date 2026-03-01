package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import fr.corentin.roux.x_wing_score_tracker.R;
import fr.corentin.roux.x_wing_score_tracker.model.Mission;
import fr.corentin.roux.x_wing_score_tracker.ui.activities.model.ZoomableImageView;

/**
 * Activité affichant le détail d'une mission à l'aide d'un rendu PDF.
 * Extrait la page correspondante du document de référence des scénarios.
 */
public class MissionDetailActivity extends AbstractActivity
{
    private ZoomableImageView touch;

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
        this.touch = this.findViewById(R.id.pdfImageView);
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
            try
            {
                // 1. Définir le fichier de destination dans le cache de l'app
                File file = new File(getCacheDir(), "scenarios_temp.pdf");
                // 2. Copier l'asset vers ce fichier s'il n'existe pas ou à chaque fois
                try (InputStream inputStream = this.getAssets().open("en/scenarios.pdf");
                     OutputStream outputStream = new FileOutputStream(file))
                {
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1)
                    {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                }
                // 3. Utiliser le fichier avec PdfRenderer
                final ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                final PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                final PdfRenderer.Page page = pdfRenderer.openPage(mission.getPage());
                // Création d'un bitmap pour le rendu de la page
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                // Rendu de la page vers le bitmap
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                // Affichage du bitmap dans l'ImageView zoomable
                this.touch.setMinimumHeight(bitmap.getHeight());
                this.touch.setMinimumWidth(bitmap.getWidth());
                this.touch.setImageBitmap(bitmap);
                page.close();
                pdfRenderer.close();
                parcelFileDescriptor.close();
            } catch (Exception e)
            {
                //Nothing to handle
            }
        }
    }

}
