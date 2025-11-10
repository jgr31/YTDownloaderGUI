package gelabert.app.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class MediaTableModel extends AbstractTableModel {
    private final String[] columns = {"Nombre", "Tamaño (MB)", "MIME", "Fecha"};
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private List<MediaItem> data = new ArrayList<>();
    public void setData(List<MediaItem> items) { this.data = new ArrayList<>(items); fireTableDataChanged(); }
    public MediaItem getAt(int row) { return (row >= 0 && row < data.size()) ? data.get(row) : null; }
    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int c) { return columns[c]; }
    @Override public Object getValueAt(int row, int col) {
        MediaItem m = data.get(row);
        return switch (col) {
            case 0 -> m.getName();
            case 1 -> String.format("%.2f", m.getSizeBytes() / (1024.0 * 1024.0));
            case 2 -> m.getMimeType();
            case 3 -> fmt.format(m.getDownloadedAt());
            default -> "";
        };
    }
    @Override public Class<?> getColumnClass(int c) { return String.class; }
}
