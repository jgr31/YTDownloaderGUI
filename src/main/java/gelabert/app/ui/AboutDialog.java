package gelabert.app.ui;

import javax.swing.*;

public class AboutDialog extends javax.swing.JDialog {
    public AboutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("About");
        setSize(400, 240);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        JLabel lbl = new JLabel("<html><b>Autor:</b> Jordi Gelabert Rosselló<br/>"
                + "<b>Curso:</b> DI 25/26<br/>"
                + "<b>Recursos:</b> yt-dlp, ffmpeg, ...</html>");
        lbl.setBounds(20, 20, 360, 120); add(lbl);
        JButton ok = new JButton("OK"); ok.setBounds(150, 150, 100, 28);
        ok.addActionListener(e -> dispose()); add(ok);
    }
    private void initComponents() { }
}
