package gelabert.app.service;

import gelabert.app.model.MediaItem;
import gelabert.app.model.MediaType;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class MediaLibraryService {
    private final Path root;
    public MediaLibraryService(Path root) { this.root = root; }
    public java.util.List<String> listSubfolders() throws IOException {
        if (!Files.isDirectory(root)) return java.util.List.of();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(root, p -> Files.isDirectory(p))) {
            java.util.List<String> names = new java.util.ArrayList<>();
            for (Path p : ds) names.add(p.getFileName().toString());
            names.sort(String.CASE_INSENSITIVE_ORDER);
            return names;
        }
    }
    public java.util.List<MediaItem> scanFolder(String subfolder) throws IOException {
        Path base = (subfolder == null || subfolder.isBlank()) ? root : root.resolve(subfolder);
        if (!Files.isDirectory(base)) return java.util.List.of();
        try (var s = Files.list(base)) {
            return s.filter(Files::isRegularFile)
                    .map(this::toMediaItemSafe)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(MediaItem::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        }
    }
    private MediaItem toMediaItemSafe(Path p) {
        try {
            String mime = Files.probeContentType(p);
            if (mime == null) mime = guessByExtension(p);
            BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class);
            long size = attrs.size();
            LocalDateTime ts = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(attrs.lastModifiedTime().toMillis()),
                    ZoneId.systemDefault());
            return new MediaItem(
                    p.getFileName().toString(), p, size, mime, ts, MediaType.fromMime(mime));
        } catch (IOException e) { return null; }
    }
    private String guessByExtension(Path p) {
        String n = p.getFileName().toString().toLowerCase(java.util.Locale.ROOT);
        if (n.endsWith(".mp3"))  return "audio/mpeg";
        if (n.endsWith(".wav"))  return "audio/wav";
        if (n.endsWith(".mp4"))  return "video/mp4";
        if (n.endsWith(".mkv"))  return "video/x-matroska";
        if (n.endsWith(".png"))  return "image/png";
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
        if (n.endsWith(".pdf"))  return "application/pdf";
        if (n.endsWith(".txt"))  return "text/plain";
        return "application/octet-stream";
    }
    public boolean delete(MediaItem item) throws IOException { return Files.deleteIfExists(item.getPath()); }
}
