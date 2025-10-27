package gelabert.ytdownloadergui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main window (fixed size, not resizable, null layout via manual placement).
 * Contains a swappable center panel for Preferences.
 */
public class Mainframe extends javax.swing.JFrame implements PreferencesPanel.Listener {

    private static final Logger logger = Logger.getLogger(Mainframe.class.getName());

    // Main panel widgets
    private JPanel mainPanel;
    private JTextField urlField;
    private JTextField outputDirField;
    private JButton browseOutputBtn;
    private JButton downloadBtn;
    private JButton openLastBtn;
    private JCheckBox audioOnlyCb;
    private JRadioButton rbMP3;
    private JRadioButton rbMP4;
    private ButtonGroup formatGroup;
    private JTextArea logArea;

    // Preferences view
    private PreferencesPanel preferencesPanel;

    public Mainframe() {
        initComponents();
        initMainPanel();
        setJMenuBar(buildMenuBar());
        setResizable(false);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("YT Downloader GUI");
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(null); // null layout at frame-level per spec
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            @Override public void actionPerformed(ActionEvent e) { dispose(); }
        });
        file.add(exit);

        JMenu edit = new JMenu("Edit");
        JMenuItem prefs = new JMenuItem(new AbstractAction("Preferences") {
            @Override public void actionPerformed(ActionEvent e) { showPreferencesPanel(); }
        });
        edit.add(prefs);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem(new AbstractAction("About") {
            @Override public void actionPerformed(ActionEvent e) {
                new AboutDialog(Mainframe.this).setVisible(true);
            }
        });
        help.add(about);

        mb.add(file);
        mb.add(edit);
        mb.add(help);
        return mb;
    }

    private void initMainPanel() {
        mainPanel = new JPanel(null); // null layout per spec
        mainPanel.setBounds(0, 0, getWidth(), getHeight());

        JLabel urlLabel = new JLabel("Video/Playlist URL:");
        urlLabel.setBounds(20, 20, 200, 24);
        mainPanel.add(urlLabel);

        urlField = new JTextField();
        urlField.setBounds(160, 20, 620, 24);
        mainPanel.add(urlField);

        JLabel outLabel = new JLabel("Output folder:");
        outLabel.setBounds(20, 60, 120, 24);
        mainPanel.add(outLabel);

        outputDirField = new JTextField(AppPreferences.getLastOutputDir());
        outputDirField.setBounds(160, 60, 520, 24);
        mainPanel.add(outputDirField);

        browseOutputBtn = new JButton("Browse...");
        browseOutputBtn.setBounds(690, 60, 90, 24);
        browseOutputBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                outputDirField.setText(chooser.getSelectedFile().getAbsolutePath());
                AppPreferences.setLastOutputDir(outputDirField.getText());
            }
        });
        mainPanel.add(browseOutputBtn);

        audioOnlyCb = new JCheckBox("Audio only (extract with ffmpeg)");
        audioOnlyCb.setBounds(20, 100, 260, 24);
        mainPanel.add(audioOnlyCb);

        rbMP3 = new JRadioButton("MP3");
        rbMP3.setBounds(300, 100, 60, 24);
        rbMP4 = new JRadioButton("MP4", true);
        rbMP4.setBounds(360, 100, 60, 24);
        formatGroup = new ButtonGroup();
        formatGroup.add(rbMP3);
        formatGroup.add(rbMP4);
        mainPanel.add(rbMP3);
        mainPanel.add(rbMP4);

        downloadBtn = new JButton("Download");
        downloadBtn.setBounds(20, 140, 120, 30);
        downloadBtn.addActionListener(e -> startDownload());
        mainPanel.add(downloadBtn);

        openLastBtn = new JButton("Open last file");
        openLastBtn.setBounds(160, 140, 150, 30);
        openLastBtn.addActionListener(e -> openLastDownloaded());
        mainPanel.add(openLastBtn);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane sp = new JScrollPane(logArea);
        sp.setBounds(20, 190, 940, 340);
        mainPanel.add(sp);

        getContentPane().add(mainPanel);
        repaint();
    }

    private void showPreferencesPanel() {
        if (preferencesPanel == null) {
            preferencesPanel = new PreferencesPanel();
            preferencesPanel.setListener(this);
            preferencesPanel.setBounds(0, 0, getWidth(), getHeight());
        }
        getContentPane().removeAll();
        getContentPane().add(preferencesPanel);
        repaint();
        revalidate();
    }

    public void showMainPanel() {
        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        repaint();
        revalidate();
    }

    private void openLastDownloaded() {
        String path = AppPreferences.getLastDownloadedFile();
        if (path == null || path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No file downloaded yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to open: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startDownload() {
        String url = urlField.getText().trim();
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please paste a URL.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String outDir = outputDirField.getText().trim();
        if (outDir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an output folder.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        File out = new File(outDir);
        if (!out.exists()) out.mkdirs();

        AppPreferences.setLastOutputDir(outDir);

        boolean audioOnly = audioOnlyCb.isSelected();
        boolean toMP3 = rbMP3.isSelected();

        int limit = AppPreferences.getSpeedLimitKBs();
        String yt = AppPreferences.getYtDlpPath();
        String temp = AppPreferences.getTempPath();
        boolean writeM3U = AppPreferences.isCreateM3U();

        List<String> cmd = new ArrayList<>();
        cmd.add(yt);
        cmd.add(url);
        cmd.add("--no-progress");
        cmd.add("--newline"); // progress lines
        cmd.add("-o");
        cmd.add(outDir + File.separator + "%(title)s.%(ext)s");
        cmd.add("--ffmpeg-location"); cmd.add(findFfmpegFallback());
        if (audioOnly) {
            cmd.add("--extract-audio");
            if (toMP3) { cmd.add("--audio-format"); cmd.add("mp3"); }
            cmd.add("--no-keep-video");
        } else {
            // Prefer mp4 if available
            if (rbMP4.isSelected()) {
                cmd.add("-f"); cmd.add("mp4/bestvideo[ext=mp4]+bestaudio[ext=m4a]/best");
            }
        }
        if (limit > 0) {
            // yt-dlp uses --limit-rate BYTES/s; we convert KB/s -> K
            cmd.add("--limit-rate"); cmd.add(limit + "K");
        }
        // Use temp dir
        cmd.add("--paths"); cmd.add("temp:" + temp);

        logArea.setText("Command: " + String.join(" ", cmd) + "\n\n");

        downloadBtn.setEnabled(false);
        AtomicReference<String> lastOutput = new AtomicReference<>("");
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override protected Void doInBackground() throws Exception {
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectErrorStream(true);
                Process p = pb.start();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        publish(line);
                        // Heuristic: remember last "Destination:" or "Merging formats into" line as the output
                        if (line.toLowerCase().contains("destination:") || line.toLowerCase().contains("merging formats into")) {
                            int idx = line.indexOf(":");
                            if (idx > 0) {
                                String candidate = line.substring(idx + 1).trim();
                                lastOutput.set(candidate);
                            }
                        }
                    }
                }
                p.waitFor();
                return null;
            }

            @Override protected void process(java.util.List<String> chunks) {
                for (String l : chunks) {
                    logArea.append(l + "\n");
                }
            }

            @Override protected void done() {
                downloadBtn.setEnabled(true);
                String path = lastOutput.get();
                if (path != null && !path.isEmpty()) {
                    AppPreferences.setLastDownloadedFile(path);
                    logArea.append("\nSaved: " + path + "\n");
                    if (writeM3U) {
                        try {
                            writeSimpleM3U(out, path);
                        } catch (IOException ex) {
                            logger.log(Level.WARNING, "Failed to write m3u: " + ex.getMessage(), ex);
                        }
                    }
                    int opt = JOptionPane.showConfirmDialog(Mainframe.this, "Open in default player?", "Download complete", JOptionPane.YES_NO_OPTION);
                    if (opt == JOptionPane.YES_OPTION) {
                        openLastDownloaded();
                    }
                } else {
                    JOptionPane.showMessageDialog(Mainframe.this, "Download finished. (Output file not detected; check log.)",
                            "Done", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void writeSimpleM3U(File folder, String singlePath) throws IOException {
        File m3u = new File(folder, "playlist.m3u");
        List<String> lines = new ArrayList<>();
        lines.add("#EXTM3U");
        lines.add(singlePath);
        Files.write(m3u.toPath(), lines, StandardCharsets.UTF_8);
        logArea.append("Wrote M3U: " + m3u.getAbsolutePath() + "\n");
    }

    /** Best-effort: return ffmpeg binary folder if available in PATH, else empty string. */
    private String findFfmpegFallback() {
        // Let yt-dlp find ffmpeg if it's in PATH; empty string is tolerated.
        return "";
    }

    // PreferencesPanel.Listener implementation
    @Override public void onBackRequested() { showMainPanel(); }
    @Override public void onApplyRequested() { /* nothing else to do */ }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Mainframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(() -> new Mainframe().setVisible(true));
    }
}