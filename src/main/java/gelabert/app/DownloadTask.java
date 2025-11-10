package gelabert.app;

import javax.swing.SwingWorker;

// Stub minimal para DI01: sustituye por integración real con yt-dlp si quieres probar descargas.
public class DownloadTask extends SwingWorker<Integer, String> {
    @Override protected Integer doInBackground() throws Exception { Thread.sleep(500); return 0; }
}
