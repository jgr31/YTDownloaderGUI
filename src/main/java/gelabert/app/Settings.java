package gelabert.app;

import java.nio.file.Path;
import java.util.prefs.Preferences;

public class Settings {
    private static final Preferences PREF = Preferences.userRoot().node("gelabert.di01di02");
    public static String get(String k, String def) { return PREF.get(k, def); }
    public static void set(String k, String v) { PREF.put(k, v); }
    public static Path downloadsRoot() {
        String def = Path.of(System.getProperty("user.home"), "Downloads").toString();
        return Path.of(get("downloadsRoot", def));
    }
    public static void setDownloadsRoot(Path p) { set("downloadsRoot", p.toString()); }
    public static String ytDlpPath() { return get("ytDlpPath", "yt-dlp"); }
    public static void setYtDlpPath(String p) { set("ytDlpPath", p); }
    public static String ffmpegPath() { return get("ffmpegPath", "ffmpeg"); }
    public static void setFfmpegPath(String p) { set("ffmpegPath", p); }
    public static boolean createM3U() { return Boolean.parseBoolean(get("createM3U", "false")); }
    public static void setCreateM3U(boolean v) { set("createM3U", Boolean.toString(v)); }
    public static String speedLimit() { return get("speedLimit", ""); }
    public static void setSpeedLimit(String v) { set("speedLimit", v); }
    public static String lastDownloaded() { return get("lastDownloaded", ""); }
    public static void setLastDownloaded(String v) { set("lastDownloaded", v); }
}
