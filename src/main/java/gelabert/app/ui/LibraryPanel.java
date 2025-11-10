package gelabert.app.ui;

import gelabert.app.Settings;
import gelabert.app.model.MediaItem;
import gelabert.app.model.MediaTableModel;
import gelabert.app.model.MediaType;
import gelabert.app.service.MediaLibraryService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.util.List;

public class LibraryPanel extends javax.swing.JPanel {
    private final JFrame host;
    private final MediaLibraryService service;
    private final DefaultListModel<String> folderModel = new DefaultListModel<>();
    private final DefaultComboBoxModel<MediaType> typeModel = new DefaultComboBoxModel<>();
    private final MediaTableModel tableModel = new MediaTableModel();
    private TableRowSorter<MediaTableModel> sorter;

    private JList<String> lstFolders;
    private JComboBox<MediaType> cmbType;
    private JTable tblMedia;
    private JTextField txtSearch;
    private JButton btnDelete, btnBack;
    private JLabel lblCount;

    public LibraryPanel(JFrame host) {
        this.host = host;
        this.service = new MediaLibraryService(Settings.downloadsRoot());
        initComponents();
        build();
        loadFolders();
        loadTable(null);
    }

    private void build() {
        setLayout(null);
        JScrollPane spList = new JScrollPane();
        lstFolders = new JList<>(); spList.setViewportView(lstFolders); spList.setBounds(20, 20, 200, 420); add(spList);
        JLabel lType = new JLabel("Tipo:"); lType.setBounds(240, 20, 40, 24); add(lType);
        cmbType = new JComboBox<>(); cmbType.setBounds(280, 20, 160, 24); add(cmbType);
        JLabel lSearch = new JLabel("Buscar:"); lSearch.setBounds(460, 20, 60, 24); add(lSearch);
        txtSearch = new JTextField(); txtSearch.setBounds(520, 20, 260, 24); add(txtSearch);
        JScrollPane spTable = new JScrollPane(); tblMedia = new JTable(); spTable.setViewportView(tblMedia);
        spTable.setBounds(240, 60, 540, 380); add(spTable);
        btnDelete = new JButton("Borrar seleccionado"); btnDelete.setBounds(240, 450, 180, 28); add(btnDelete);
        lblCount = new JLabel("0 elementos"); lblCount.setBounds(440, 450, 120, 28); add(lblCount);
        btnBack = new JButton("Volver"); btnBack.setBounds(700, 450, 80, 28);
        btnBack.addActionListener(e -> { host.setContentPane(new MainFrame().getContentPane()); host.setContentPane(new MainFrame().getContentPane()); host.revalidate(); host.repaint(); }); add(btnBack);

        lstFolders.setModel(folderModel);
        cmbType.setModel(typeModel);
        typeModel.addElement(null); for (MediaType t : MediaType.values()) typeModel.addElement(t);
        tblMedia.setModel(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        tblMedia.setRowSorter(sorter);
        lstFolders.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) loadTable(lstFolders.getSelectedValue()); });
        cmbType.addActionListener(this::onFilterChanged);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });
        btnDelete.addActionListener(this::onDelete);
    }

    private void loadFolders() {
        folderModel.clear();
        try { for (String f : service.listSubfolders()) folderModel.addElement(f); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, "Error listando carpetas: " + e.getMessage()); }
    }

    private void loadTable(String subfolder) {
        try {
            List<MediaItem> items = service.scanFolder(subfolder);
            tableModel.setData(items);
            applyFilters();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error escaneando: " + e.getMessage()); }
    }

    private void onFilterChanged(ActionEvent e) { applyFilters(); }

    private void applyFilters() {
        final String q = txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase();
        final MediaType type = (MediaType) cmbType.getSelectedItem();
        sorter.setRowFilter(new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends MediaTableModel, ? extends Integer> entry) {
                MediaItem m = tableModel.getAt(entry.getIdentifier());
                if (m == null) return false;
                boolean textOk = q.isEmpty()
                        || m.getName().toLowerCase().contains(q)
                        || (m.getMimeType() != null && m.getMimeType().toLowerCase().contains(q));
                boolean typeOk = (type == null) || m.getMediaType() == type;
                return textOk && typeOk;
            }
        });
        lblCount.setText(tblMedia.getRowCount() + " elementos");
    }

    private void onDelete(ActionEvent e) {
        int viewRow = tblMedia.getSelectedRow();
        if (viewRow < 0) { JOptionPane.showMessageDialog(this, "Selecciona un archivo en la tabla."); return; }
        int modelRow = tblMedia.convertRowIndexToModel(viewRow);
        MediaItem item = tableModel.getAt(modelRow);
        int r = JOptionPane.showConfirmDialog(this, "¿Borrar \"" + item.getName() + "\"?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            try {
                boolean ok = new gelabert.app.service.MediaLibraryService(Settings.downloadsRoot()).delete(item);
                if (ok) loadTable(lstFolders.getSelectedValue()); else JOptionPane.showMessageDialog(this, "No se pudo borrar.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage()); }
        }
    }

    private void initComponents() { }
}
