package gelabert.app.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class MediaItem {
    private final String name;
    private final Path path;
    private final long sizeBytes;
    private final String mimeType;
    private final LocalDateTime downloadedAt;
    private final MediaType mediaType;

    public MediaItem(String name, Path path, long sizeBytes, String mimeType,
                     LocalDateTime downloadedAt, MediaType mediaType) {
        this.name = name;
        this.path = path;
        this.sizeBytes = sizeBytes;
        this.mimeType = mimeType;
        this.downloadedAt = downloadedAt;
        this.mediaType = mediaType;
    }
    public String getName() { return name; }
    public Path getPath() { return path; }
    public long getSizeBytes() { return sizeBytes; }
    public String getMimeType() { return mimeType; }
    public LocalDateTime getDownloadedAt() { return downloadedAt; }
    public MediaType getMediaType() { return mediaType; }
    @Override public String toString() { return name; }
}
