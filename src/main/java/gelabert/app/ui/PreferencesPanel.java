package gelabert.app.ui;

import gelabert.app.Settings;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class PreferencesPanel extends javax.swing.JPanel {
    private final JFrame host;
    private JTextField txtYt, txtFfmpeg, txtSpeed, txtDownloads;
    private JCheckBox chkM3U;
    private JButton btnSave, btnBack, btnChooseDownloads, btnChooseYt, btnChooseFfmpeg;

    public PreferencesPanel(JFrame host) {
        this.host = host;
        initComponents();
        build();
        loadSettings();
    }

    private void build() {
        setLayout(null);
        JLabel t = new JLabel("Preferencias"); t.setBounds(20, 10, 200, 24); add(t);
        JLabel l1 = new JLabel("yt-dlp:"); l1.setBounds(20, 50, 80, 24); add(l1);
        txtYt = new JTextField(Settings.ytDlpPath()); txtYt.setBounds(100,50,480,24); add(txtYt);
        btnChooseYt = new JButton("..."); btnChooseYt.setBounds(590,50,40,24); add(btnChooseYt);
        btnChooseYt.addActionListener(e -> chooseFile(txtYt));
        JLabel l2 = new JLabel("ffmpeg:"); l2.setBounds(20, 90, 80, 24); add(l2);
        txtFfmpeg = new JTextField(Settings.ffmpegPath()); txtFfmpeg.setBounds(100,90,480,24); add(txtFfmpeg);
        btnChooseFfmpeg = new JButton("..."); btnChooseFfmpeg.setBounds(590,90,40,24); add(btnChooseFfmpeg);
        btnChooseFfmpeg.addActionListener(e -> chooseFile(txtFfmpeg));
        JLabel l3 = new JLabel("Descargas:"); l3.setBounds(20, 130, 80, 24); add(l3);
        txtDownloads = new JTextField(Settings.downloadsRoot().toString()); txtDownloads.setBounds(100,130,480,24); add(txtDownloads);
        btnChooseDownloads = new JButton("..."); btnChooseDownloads.setBounds(590,130,40,24); add(btnChooseDownloads);
        btnChooseDownloads.addActionListener(e -> chooseDir(txtDownloads));
        JLabel l4 = new JLabel("Límite velocidad:"); l4.setBounds(20, 170, 100, 24); add(l4);
        txtSpeed = new JTextField(Settings.speedLimit()); txtSpeed.setBounds(130,170,120,24); add(txtSpeed);
        chkM3U = new JCheckBox("Crear .m3u para playlists"); chkM3U.setBounds(20, 210, 250, 24); add(chkM3U);
        btnSave = new JButton("Guardar"); btnSave.setBounds(20, 260, 100, 28); btnSave.addActionListener(this::onSave); add(btnSave);
        btnBack = new JButton("Volver"); btnBack.setBounds(140, 260, 100, 28);
        btnBack.addActionListener(e -> { host.setContentPane(new MainFrame().getContentPane()); host.revalidate(); host.repaint(); }); add(btnBack);
    }

    private void loadSettings() { chkM3U.setSelected(Settings.createM3U()); }
    private void onSave(ActionEvent e) {
        Settings.setYtDlpPath(txtYt.getText().trim());
        Settings.setFfmpegPath(txtFfmpeg.getText().trim());
        Settings.setDownloadsRoot(java.nio.file.Path.of(txtDownloads.getText().trim()));
        Settings.setSpeedLimit(txtSpeed.getText().trim());
        Settings.setCreateM3U(chkM3U.isSelected());
        JOptionPane.showMessageDialog(this, "Preferencias guardadas.");
    }
    private void chooseFile(JTextField target) {
        JFileChooser ch = new JFileChooser();
        if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) target.setText(ch.getSelectedFile().getAbsolutePath());
    }
    private void chooseDir(JTextField target) {
        JFileChooser ch = new JFileChooser(); ch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) target.setText(ch.getSelectedFile().getAbsolutePath());
    }
    private void initComponents() { }
}
