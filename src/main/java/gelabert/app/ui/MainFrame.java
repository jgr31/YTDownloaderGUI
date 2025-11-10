package gelabert.app.ui;

import gelabert.app.Settings;
import gelabert.app.DownloadTask;
import javax.swing.*;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class MainFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JTextField txtUrl, txtDownloadDir;
    private JTextArea txtOutput;
    private JCheckBox chkAudioOnly;
    private JRadioButton rbMp4, rbMp3;
    private ButtonGroup grpFormat;
    private JButton btnDownload, btnChooseFolder, btnPlayLast;

    public MainFrame() {
        initComponents();
        setTitle("Media Downloader & Library");
        setSize(900, 600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildMainView();
        setContentPane(mainPanel);
        setJMenuBar(buildMenuBar());
    }

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu mFile = new JMenu("File");
        mFile.add(new JMenuItem(new AbstractAction("Exit") {
            @Override public void actionPerformed(ActionEvent e) { dispose(); }
        }));
        JMenu mEdit = new JMenu("Edit");
        mEdit.add(new JMenuItem(new AbstractAction("Preferences") {
            @Override public void actionPerformed(ActionEvent e) {
                setContentPane(new PreferencesPanel(MainFrame.this));
                revalidate(); repaint();
            }
        }));
        JMenu mView = new JMenu("View");
        mView.add(new JMenuItem(new AbstractAction("Downloader") {
            @Override public void actionPerformed(ActionEvent e) {
                setContentPane(mainPanel); revalidate(); repaint();
            }
        }));
        mView.add(new JMenuItem(new AbstractAction("Library") {
            @Override public void actionPerformed(ActionEvent e) {
                setContentPane(new LibraryPanel(MainFrame.this)); revalidate(); repaint();
            }
        }));
        JMenu mHelp = new JMenu("Help");
        mHelp.add(new JMenuItem(new AbstractAction("About") {
            @Override public void actionPerformed(ActionEvent e) {
                AboutDialog dlg = new AboutDialog(MainFrame.this, true);
                dlg.setLocationRelativeTo(MainFrame.this);
                dlg.setVisible(true);
            }
        }));
        mb.add(mFile); mb.add(mEdit); mb.add(mView); mb.add(mHelp);
        return mb;
    }

    private void buildMainView() {
        mainPanel = new JPanel(null);
        JLabel lblUrl = new JLabel("URL:"); lblUrl.setBounds(20, 20, 80, 24); mainPanel.add(lblUrl);
        txtUrl = new JTextField(); txtUrl.setBounds(100, 20, 600, 24); mainPanel.add(txtUrl);
        btnDownload = new JButton("Descargar"); btnDownload.setBounds(720, 20, 140, 24);
        btnDownload.addActionListener(this::onDownload); mainPanel.add(btnDownload);
        JLabel lblFmt = new JLabel("Formato:"); lblFmt.setBounds(20, 60, 80, 24); mainPanel.add(lblFmt);
        rbMp4 = new JRadioButton("MP4"); rbMp3 = new JRadioButton("MP3");
        grpFormat = new ButtonGroup(); grpFormat.add(rbMp4); grpFormat.add(rbMp3); rbMp4.setSelected(true);
        rbMp4.setBounds(100,60,60,24); rbMp3.setBounds(160,60,60,24); mainPanel.add(rbMp4); mainPanel.add(rbMp3);
        chkAudioOnly = new JCheckBox("Solo audio"); chkAudioOnly.setBounds(240,60,120,24); mainPanel.add(chkAudioOnly);
        JLabel lblDir = new JLabel("Descargas:"); lblDir.setBounds(20, 100, 80, 24); mainPanel.add(lblDir);
        txtDownloadDir = new JTextField(Settings.downloadsRoot().toString()); txtDownloadDir.setBounds(100,100,520,24); mainPanel.add(txtDownloadDir);
        btnChooseFolder = new JButton("Elegir..."); btnChooseFolder.setBounds(630,100,90,24);
        btnChooseFolder.addActionListener(e -> {
            JFileChooser ch = new JFileChooser(); ch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (ch.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                Settings.setDownloadsRoot(ch.getSelectedFile().toPath());
                txtDownloadDir.setText(Settings.downloadsRoot().toString());
            }
        }); mainPanel.add(btnChooseFolder);
        btnPlayLast = new JButton("Reproducir último"); btnPlayLast.setBounds(730,100,130,24);
        btnPlayLast.addActionListener(e -> playLast()); mainPanel.add(btnPlayLast);
        txtOutput = new JTextArea(); JScrollPane sp = new JScrollPane(txtOutput); sp.setBounds(20, 140, 840, 400); mainPanel.add(sp);
    }

    private void onDownload(ActionEvent e) {
        String url = txtUrl.getText().trim();
        if (url.isEmpty()) { JOptionPane.showMessageDialog(this, "Introduce una URL"); return; }
        txtOutput.setText("");
        DownloadTask task = new DownloadTask();
        task.execute();
        JOptionPane.showMessageDialog(this, "Descarga simulada (stub). Integra yt-dlp si quieres probar real.");
    }

    private void playLast() {
        String path = Settings.lastDownloaded();
        if (path == null || path.isBlank()) { JOptionPane.showMessageDialog(this, "Aún no hay archivo descargado."); return; }
        try { Desktop.getDesktop().open(new File(path)); }
        catch (IOException ex) { JOptionPane.showMessageDialog(this, "No se pudo abrir: " + ex.getMessage()); }
    }


    private void initComponents() { /* NetBeans placeholder */ }
}
