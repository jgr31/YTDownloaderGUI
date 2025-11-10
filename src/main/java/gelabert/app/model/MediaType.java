package gelabert.app.model;

public enum MediaType {
    AUDIO, VIDEO, IMAGE, DOCUMENT, OTHER;
    public static MediaType fromMime(String mime) {
        if (mime == null) return OTHER;
        if (mime.startsWith("audio"))  return AUDIO;
        if (mime.startsWith("video"))  return VIDEO;
        if (mime.startsWith("image"))  return IMAGE;
        if (mime.contains("pdf") || mime.contains("text") || mime.contains("msword")
            || mime.contains("officedocument")) return DOCUMENT;
        return OTHER;
    }
}
