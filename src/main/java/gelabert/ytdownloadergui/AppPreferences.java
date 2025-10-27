package gelabert.ytdownloadergui;

import java.util.prefs.Preferences;

/**
 * Lightweight wrapper around {@link Preferences} to persist app settings.
 * This keeps the prototype simple and avoids manual file IO.
 */
public class AppPreferences {

    private static final Preferences PREFS = Preferences.userRoot().node("YTDownloaderGUI");

    public static String getTempPath() {
        return PREFS.get("tempPath", System.getProperty("java.io.tmpdir"));
    }

    public static void setTempPath(String path) {
        PREFS.put("tempPath", path);
    }

    public static boolean isCreateM3U() {
        return PREFS.getBoolean("createM3U", true);
    }

    public static void setCreateM3U(boolean v) {
        PREFS.putBoolean("createM3U", v);
    }

    /** KB/s; 0 means unlimited */
    public static int getSpeedLimitKBs() {
        return PREFS.getInt("speedLimitKBs", 0);
    }

    public static void setSpeedLimitKBs(int v) {
        PREFS.putInt("speedLimitKBs", v);
    }

    public static String getYtDlpPath() {
        return PREFS.get("ytDlpPath", "yt-dlp"); // expect in PATH by default
    }

    public static void setYtDlpPath(String path) {
        PREFS.put("ytDlpPath", path);
    }

    public static String getLastOutputDir() {
        return PREFS.get("lastOutputDir", System.getProperty("user.home"));
    }

    public static void setLastOutputDir(String dir) {
        PREFS.put("lastOutputDir", dir);
    }

    public static String getLastDownloadedFile() {
        return PREFS.get("lastDownloadedFile", "");
    }

    public static void setLastDownloadedFile(String path) {
        PREFS.put("lastDownloadedFile", path);
    }
}